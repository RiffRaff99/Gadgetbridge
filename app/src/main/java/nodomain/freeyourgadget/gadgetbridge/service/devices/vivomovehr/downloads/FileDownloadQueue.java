package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.downloads;

import nodomain.freeyourgadget.gadgetbridge.devices.vivomovehr.VivomoveConstants;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.ChecksumCalculator;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.VivomoveHrCommunicator;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.DownloadRequestMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.DownloadRequestResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.FileTransferDataMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.FileTransferDataResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class FileDownloadQueue {
    private static final Logger LOG = LoggerFactory.getLogger(FileDownloadQueue.class);

    private final VivomoveHrCommunicator communicator;

    private final Queue<QueueItem> queue = new LinkedList<>();
    private final Set<Integer> queuedFileIndices = new HashSet<>();

    private QueueItem currentlyDownloadingItem;

    public FileDownloadQueue(VivomoveHrCommunicator communicator) {
        this.communicator = communicator;
    }

    public void addToDownloadQueue(int fileIndex, int dataSize) {
        if (queuedFileIndices.contains(fileIndex)) {
            LOG.debug("Ignoring download request of {}, already in queue", fileIndex);
            return;
        }
        queue.add(new QueueItem(fileIndex, dataSize));
        queuedFileIndices.add(fileIndex);
        checkRequestNextDownload();
    }

    private void checkRequestNextDownload() {
        if (currentlyDownloadingItem != null) {
            LOG.debug("Another download is pending");
            return;
        }
        if (queue.isEmpty()) {
            LOG.debug("No download in queue");
            return;
        }
        requestNextDownload();
    }

    public void requestNextDownload() {
        currentlyDownloadingItem = queue.remove();
        final int fileIndex = currentlyDownloadingItem.fileIndex;
        LOG.info("Requesting download of {}", fileIndex);
        queuedFileIndices.remove(fileIndex);
        communicator.sendMessage(new DownloadRequestMessage(fileIndex, 0, 1, 0, 0).packet);
    }

    public void onDownloadRequestResponse(DownloadRequestResponseMessage responseMessage) {
        if (currentlyDownloadingItem == null) {
            LOG.error("Download request response arrived, but nothing is being downloaded");
            return;
        }

        if (responseMessage.status == VivomoveConstants.STATUS_ACK && responseMessage.response == DownloadRequestResponseMessage.RESPONSE_DOWNLOAD_REQUEST_OKAY) {
            LOG.info("Received response for download request of {}: {}/{}, {}B", currentlyDownloadingItem.fileIndex, responseMessage.status, responseMessage.response, responseMessage.fileSize);
            currentlyDownloadingItem.setDataSize(responseMessage.fileSize);
        } else {
            LOG.error("Received error response for download request of {}: {}/{}", currentlyDownloadingItem.fileIndex, responseMessage.status, responseMessage.response);
            currentlyDownloadingItem = null;
        }
    }

    public void onFileTransferData(FileTransferDataMessage dataMessage) {
        final QueueItem currentlyDownloadingItem = this.currentlyDownloadingItem;
        if (currentlyDownloadingItem == null) {
            LOG.error("Download request response arrived, but nothing is being downloaded");
            communicator.sendMessage(new FileTransferDataResponseMessage(VivomoveConstants.STATUS_ACK, FileTransferDataResponseMessage.RESPONSE_ABORT_DOWNLOAD_REQUEST, 0).packet);
            return;
        }

        if (dataMessage.dataOffset < currentlyDownloadingItem.dataOffset) {
            LOG.warn("Ignoring repeated transfer at offset {} of #{}", dataMessage.dataOffset, currentlyDownloadingItem.fileIndex);
            communicator.sendMessage(new FileTransferDataResponseMessage(VivomoveConstants.STATUS_ACK, FileTransferDataResponseMessage.RESPONSE_ERROR_DATA_OFFSET_MISMATCH, currentlyDownloadingItem.dataOffset).packet);
            return;
        }
        if (dataMessage.dataOffset > currentlyDownloadingItem.dataOffset) {
            LOG.warn("Missing data at offset {} when received data at offset {} of #{}", currentlyDownloadingItem.dataOffset, dataMessage.dataOffset, currentlyDownloadingItem.fileIndex);
            communicator.sendMessage(new FileTransferDataResponseMessage(VivomoveConstants.STATUS_ACK, FileTransferDataResponseMessage.RESPONSE_ERROR_DATA_OFFSET_MISMATCH, currentlyDownloadingItem.dataOffset).packet);
            return;
        }

        final int dataCrc = ChecksumCalculator.computeCrc(dataMessage.data, 0, dataMessage.data.length);
        if (dataCrc != dataMessage.crc) {
            LOG.warn("Invalid CRC ({} vs {}) for {}B data @{} of {}", dataCrc, dataMessage.crc, dataMessage.data.length, dataMessage.dataOffset, currentlyDownloadingItem.fileIndex);
            communicator.sendMessage(new FileTransferDataResponseMessage(VivomoveConstants.STATUS_ACK, FileTransferDataResponseMessage.RESPONSE_ERROR_CRC_MISMATCH, currentlyDownloadingItem.dataOffset).packet);
            return;
        }

        LOG.info("Received {}B@{} of {}", dataMessage.data.length, dataMessage.dataOffset, currentlyDownloadingItem.fileIndex);
        currentlyDownloadingItem.appendData(dataMessage.data);
        communicator.sendMessage(new FileTransferDataResponseMessage(VivomoveConstants.STATUS_ACK, FileTransferDataResponseMessage.RESPONSE_TRANSFER_SUCCESSFUL, currentlyDownloadingItem.dataOffset).packet);

        if (currentlyDownloadingItem.dataOffset >= currentlyDownloadingItem.dataSize) {
            LOG.info("Transfer of file #{} complete, {}/{}B downloaded", currentlyDownloadingItem.fileIndex, currentlyDownloadingItem.dataOffset, currentlyDownloadingItem.dataSize);
            this.currentlyDownloadingItem = null;
            checkRequestNextDownload();
            reportCompletedDownload(currentlyDownloadingItem);
        }
    }

    private void reportCompletedDownload(QueueItem downloadedItem) {
        if (downloadedItem.fileIndex == 0) {
            final DirectoryData directoryData = DirectoryData.parse(downloadedItem.data);
            // TODO: report downloaded directory data
        } else {
            // TODO: report downloaded file
        }
    }

    private static class QueueItem {
        public final int fileIndex;
        public int dataSize;
        public int dataOffset;
        public byte[] data;

        public QueueItem(int fileIndex, int dataSize) {
            this.fileIndex = fileIndex;
            this.dataSize = dataSize;
        }

        public void setDataSize(int dataSize) {
            if (this.data != null) throw new IllegalStateException("Data size already set");
            this.dataSize = dataSize;
            this.data = new byte[dataSize];
        }

        public void appendData(byte[] data) {
            System.arraycopy(data, 0, this.data, dataOffset, data.length);
            dataOffset += data.length;
        }
    }
}
