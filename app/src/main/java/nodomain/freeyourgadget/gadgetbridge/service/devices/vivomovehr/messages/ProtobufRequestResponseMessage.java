package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages;

public class ProtobufRequestResponseMessage {
    public final int status;
    public final int requestId;
    public final int dataOffset;
    public final int protobufStatus;
    public final int error;

    public ProtobufRequestResponseMessage(int status, int requestId, int dataOffset, int protobufStatus, int error) {
        this.status = status;
        this.requestId = requestId;
        this.dataOffset = dataOffset;
        this.protobufStatus = protobufStatus;
        this.error = error;
    }

    public static ProtobufRequestResponseMessage parsePacket(byte[] packet) {
        final MessageReader reader = new MessageReader(packet, 4);
        final int requestMessageID = reader.readShort();
        final int status = reader.readByte();
        final int requestID = reader.readShort();
        final int dataOffset = reader.readInt();
        final int protobufStatus = reader.readByte();
        final int error = reader.readByte();

        return new ProtobufRequestResponseMessage(status, requestID, dataOffset, protobufStatus, error);
    }
}
