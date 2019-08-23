package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

// Notes: not really optimized; does a lot of (re)allocation, might use more static buffers, I guess… And code cleanup as well.
public class GfdiPacketParser {
    private static final Logger LOG = LoggerFactory.getLogger(GfdiPacketParser.class);

    private static final long BUFFER_TIMEOUT = 1500L;
    private static final byte[] EMPTY_BUFFER = new byte[0];
    private static final byte[] BUFFER_FRAMING = new byte[1];

    private byte[] buffer = EMPTY_BUFFER;
    private byte[] packet;
    private byte[] packetBuffer;
    private int bufferPos;
    private long lastUpdate;
    private boolean insidePacket;

    public void reset() {
        buffer = EMPTY_BUFFER;
        bufferPos = 0;
        insidePacket = false;
        packet = null;
        packetBuffer = EMPTY_BUFFER;
    }

    public void receivedBytes(byte[] bytes) {
        final long now = System.currentTimeMillis();
        if ((now - lastUpdate) > BUFFER_TIMEOUT) {
            reset();
        }
        lastUpdate = now;
        final int bufferSize = buffer.length;
        buffer = Arrays.copyOf(buffer, bufferSize + bytes.length);
        System.arraycopy(bytes, 0, buffer, bufferSize, bytes.length);
        parseBuffer();
    }

    public byte[] retrievePacket() {
        final byte[] resultPacket = packet;
        packet = null;
        return resultPacket;
    }

    private void parseBuffer() {
        if (packet != null) {
            // packet is waiting, unable to parse more
            return;
        }
        if (bufferPos >= buffer.length) {
            // nothing to parse
            return;
        }
        final boolean startOfPacket = !insidePacket;
        if (startOfPacket) {
            byte b;
            while (bufferPos < buffer.length && (b = buffer[bufferPos++]) != 0) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Unexpected non-zero byte while looking for framing: {}", Integer.toHexString(b));
                }
            }
            if (bufferPos >= buffer.length) {
                // nothing to parse
                return;
            }
            insidePacket = true;
        }
        while(true) {
            int chunkSize = -1;
            int chunkStart = bufferPos;
            int pos = bufferPos;
            while (pos < buffer.length && ((chunkSize = buffer[pos++]) == 0) && startOfPacket) {
                // skip repeating framing bytes (?)
                bufferPos = pos;
                chunkStart = pos;
            }
            if (startOfPacket && pos >= buffer.length) {
                // incomplete framing, needs to wait for more data and try again
                buffer = BUFFER_FRAMING;
                bufferPos = 0;
                insidePacket = false;
                return;
            }
            assert chunkSize >= 0;
            if (chunkSize == 0) {
                // end of packet
                // drop the last zero
                packet = Arrays.copyOf(packetBuffer, packetBuffer.length - 1);
                packetBuffer = EMPTY_BUFFER;
                insidePacket = false;

                if (bufferPos == buffer.length - 1) {
                    buffer = EMPTY_BUFFER;
                    bufferPos = 0;
                } else {
                    // TODO: Realloc buffer down
                    ++bufferPos;
                }
                return;
            }
            if (chunkStart + chunkSize > buffer.length) {
                // incomplete chunk, needs to wait for more data
                return;
            }

            // completed chunk
            final int packetPos = packetBuffer.length;
            packetBuffer = Arrays.copyOf(packetBuffer, packetPos + chunkSize);
            System.arraycopy(buffer, chunkStart + 1, packetBuffer, packetPos, chunkSize - 1);
            packetBuffer[packetBuffer.length - 1] = 0;

            bufferPos = chunkStart + chunkSize;
        }
    }
}
