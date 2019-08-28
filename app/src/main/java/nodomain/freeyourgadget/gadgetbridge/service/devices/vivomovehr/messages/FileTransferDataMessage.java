package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages;

public class FileTransferDataMessage {
    public final int flags;
    public final int crc;
    public final int dataOffset;
    public final byte[] data;

    public FileTransferDataMessage(int flags, int crc, int dataOffset, byte[] data) {
        this.flags = flags;
        this.crc = crc;
        this.dataOffset = dataOffset;
        this.data = data;
    }

    public static FileTransferDataMessage parsePacket(byte[] packet) {
        final MessageReader reader = new MessageReader(packet, 4);

        final int flags = reader.readByte();
        final int crc = reader.readShort();
        final int dataOffset = reader.readInt();
        final int dataSize = packet.length - 13;
        final byte[] data = reader.readBytes(dataSize);

        return new FileTransferDataMessage(flags, crc, dataOffset, data);
    }
}
