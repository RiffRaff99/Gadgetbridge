package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages;

import nodomain.freeyourgadget.gadgetbridge.devices.vivomovehr.VivomoveConstants;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.BinaryUtils;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.ChecksumCalculator;

public class GncsDataSourceMessage {
    public final byte[] packet;

    public GncsDataSourceMessage(byte[] ancsMessage, int dataOffset, int size) {
        final MessageWriter writer = new MessageWriter();
        writer.writeShort(0); // packet size will be filled below
        writer.writeShort(VivomoveConstants.MESSAGE_GNCS_DATA_SOURCE);
        writer.writeShort(ancsMessage.length);
        writer.writeShort(ChecksumCalculator.computeCrc(ancsMessage, dataOffset, size));
        writer.writeShort(dataOffset);
        writer.writeBytes(ancsMessage, dataOffset, size);
        writer.writeShort(0); // CRC will be filled below
        final byte[] packet = writer.getBytes();
        BinaryUtils.writeShort(packet, 0, packet.length);
        BinaryUtils.writeShort(packet, packet.length - 2, ChecksumCalculator.computeCrc(packet, 0, packet.length - 2));
        this.packet = packet;
    }
}
