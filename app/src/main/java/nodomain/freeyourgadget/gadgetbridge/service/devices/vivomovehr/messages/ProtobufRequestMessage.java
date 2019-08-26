package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages;

import nodomain.freeyourgadget.gadgetbridge.devices.vivomovehr.VivomoveConstants;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.BinaryUtils;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.ChecksumCalculator;

public class ProtobufRequestMessage {
    public final byte[] packet;

    public ProtobufRequestMessage(int requestId, int dataOffset, int totalProtobufLength, int protobufDataLength, byte[] messageBytes) {
        final MessageWriter writer = new MessageWriter();
        writer.writeShort(0); // packet size will be filled below
        writer.writeShort(VivomoveConstants.MESSAGE_PROTOBUF_REQUEST);
        writer.writeShort(requestId);
        writer.writeInt(dataOffset);
        writer.writeInt(totalProtobufLength);
        writer.writeInt(protobufDataLength);
        writer.writeBytes(messageBytes);
        writer.writeShort(0); // CRC will be filled below
        final byte[] packet = writer.getBytes();
        BinaryUtils.writeShort(packet, 0, packet.length);
        BinaryUtils.writeShort(packet, packet.length - 2, ChecksumCalculator.computeCrc(packet, 0, packet.length - 2));
        this.packet = packet;
    }
}
