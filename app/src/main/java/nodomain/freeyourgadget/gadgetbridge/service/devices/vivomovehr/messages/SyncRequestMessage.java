package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages;

public class SyncRequestMessage {
    public final int option;
    public final byte[] fileTypes;

    public SyncRequestMessage(int option, byte[] fileTypes) {
        this.option = option;
        this.fileTypes = fileTypes;
    }

    public static SyncRequestMessage parsePacket(byte[] packet) {
        final MessageReader reader = new MessageReader(packet, 4);
        final int option = reader.readByte();
        final int fileTypeCount = reader.readByte();
        final byte[] fileTypes = reader.readBytes(fileTypeCount);

        return new SyncRequestMessage(option, fileTypes);
    }
}
