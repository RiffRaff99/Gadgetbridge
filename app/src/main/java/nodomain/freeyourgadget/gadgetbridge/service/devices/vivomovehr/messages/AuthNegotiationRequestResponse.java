package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages;

public class AuthNegotiationRequestResponse {
    public final int status;
    public final int response;
    public final int longTermKeyAvailability;
    public final int supportedEncryptionAlgorithms;

    public AuthNegotiationRequestResponse(int status, int response, int longTermKeyAvailability, int supportedEncryptionAlgorithms) {
        this.status = status;
        this.response = response;
        this.longTermKeyAvailability = longTermKeyAvailability;
        this.supportedEncryptionAlgorithms = supportedEncryptionAlgorithms;
    }

    public static AuthNegotiationRequestResponse parsePacket(byte[] packet) {
        final MessageReader reader = new MessageReader(packet, 4);
        final int requestID = reader.readShort();
        final int status = reader.readByte();
        final int response = reader.readByte();
        final int longTermKeyAvailability = reader.readByte();
        final int supportedEncryptionAlgorithms = reader.readInt();

        return new AuthNegotiationRequestResponse(status, response, longTermKeyAvailability, supportedEncryptionAlgorithms);
    }
}
