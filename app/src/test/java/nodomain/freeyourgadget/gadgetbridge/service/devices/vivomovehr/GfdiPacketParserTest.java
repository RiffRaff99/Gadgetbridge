package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr;

import nodomain.freeyourgadget.gadgetbridge.devices.vivomovehr.VivomoveConstants;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.DeviceInformationResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.util.GB;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class GfdiPacketParserTest {
    @Test
    public void testParse() {
        final GfdiPacketParser parser = new GfdiPacketParser();
        parser.receivedBytes(new byte[]{(byte) 0x00, (byte) 0x02, (byte) 0x21, (byte) 0x04, (byte) 0xa0, (byte) 0x13, (byte) 0x6f, (byte) 0x0b, (byte) 0xd4, (byte) 0x0a, (byte) 0xe8, (byte) 0xa5, (byte) 0xb2, (byte) 0xed, (byte) 0xc2, (byte) 0x01, (byte) 0x08, (byte) 0x02, (byte) 0x0e, (byte) 0x0c});
        assertNull(parser.retrievePacket());
        parser.receivedBytes(new byte[]{(byte) 0x76, (byte) 0xc3, (byte) 0xad, (byte) 0x76, (byte) 0x6f, (byte) 0x6d, (byte) 0x6f, (byte) 0x76, (byte) 0x65, (byte) 0x20, (byte) 0x48, (byte) 0x52, (byte) 0x03, (byte) 0x87, (byte) 0xd4, (byte) 0x00});
        byte[] packet = parser.retrievePacket();
        assertNotNull(packet);
        assertArrayEquals(new byte[]{(byte) 0x21, (byte) 0x00, (byte) 0xa0, (byte) 0x13, (byte) 0x6f, (byte) 0x00, (byte) 0xd4, (byte) 0x0a, (byte) 0xe8, (byte) 0xa5, (byte) 0xb2, (byte) 0xed, (byte) 0xc2, (byte) 0x01, (byte) 0x08, (byte) 0x02, (byte) 0x00, (byte) 0x0c, (byte) 0x76, (byte) 0xc3, (byte) 0xad, (byte) 0x76, (byte) 0x6f, (byte) 0x6d, (byte) 0x6f, (byte) 0x76, (byte) 0x65, (byte) 0x20, (byte) 0x48, (byte) 0x52, (byte) 0x00, (byte) 0x87, (byte) 0xd4}, packet);
        assertNull(parser.retrievePacket());

        //parser.receivedBytes(new byte[]{(byte) 0x00, (byte) 0x02, (byte) 0x2d, (byte) 0x05, (byte) 0x88, (byte) 0x13, (byte) 0xa0, (byte) 0x13, (byte) 0x02, (byte) 0x70, (byte) 0x05, (byte) 0xff, (byte) 0xff, (byte) 0xce, (byte) 0x56, (byte) 0x01, (byte) 0x02, (byte) 0x9a, (byte) 0x01, (byte) 0x1c, (byte) 0x40, (byte) 0x0b, (byte) 0x58, (byte) 0x5a, (byte) 0x31, (byte) 0x20, (byte) 0x43, (byte) 0x6f, (byte) 0x6d, (byte) 0x70, (byte) 0x61, (byte) 0x63, (byte) 0x74, (byte) 0x04, (byte) 0x53, (byte) 0x6f, (byte) 0x6e, (byte) 0x79, (byte) 0x05, (byte) 0x47, (byte) 0x38, (byte) 0x34, (byte) 0x34, (byte) 0x31, (byte) 0x01, (byte) 0xd6, (byte) 0xca, (byte) 0x00});
        parser.receivedBytes(new byte[]{(byte) 0x00, (byte) 0x02, (byte) 0x2d, (byte) 0x05, (byte) 0x88, (byte) 0x13, (byte) 0xa0, (byte) 0x13, (byte) 0x02, (byte) 0x70, (byte) 0x05, (byte) 0xff, (byte) 0xff, (byte) 0x39, (byte) 0x30, (byte) 0x01, (byte) 0x02, (byte) 0x9a, (byte) 0x01, (byte) 0x1c, (byte) 0x40, (byte) 0x0b, (byte) 0x58, (byte) 0x5a, (byte) 0x31, (byte) 0x20, (byte) 0x43, (byte) 0x6f, (byte) 0x6d, (byte) 0x70, (byte) 0x61, (byte) 0x63, (byte) 0x74, (byte) 0x04, (byte) 0x53, (byte) 0x6f, (byte) 0x6e, (byte) 0x79, (byte) 0x05, (byte) 0x47, (byte) 0x38, (byte) 0x34, (byte) 0x34, (byte) 0x31, (byte) 0x01, (byte) 0xc3, (byte) 0x7b, (byte) 0x00});
        packet = parser.retrievePacket();
        assertNotNull(packet);
        packet = parser.retrievePacket();
        assertNull(packet);

        parser.receivedBytes(new byte[]{
                (byte) 0x00, (byte) 0x05, (byte) 0x03, (byte) 0x02, (byte) 0x8c, (byte) 0x13, (byte) 0x03, (byte) 0x6b, (byte) 0xb0, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0xff, (byte) 0x3c, (byte) 0x3f, (byte) 0x78, (byte) 0x6d, (byte) 0x6c, (byte) 0x20, (byte) 0x76,
                (byte) 0x65, (byte) 0x72, (byte) 0x73, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x3d, (byte) 0x22, (byte) 0x31, (byte) 0x2e, (byte) 0x30, (byte) 0x22, (byte) 0x20, (byte) 0x65, (byte) 0x6e, (byte) 0x63, (byte) 0x6f, (byte) 0x64, (byte) 0x69, (byte) 0x6e,
                (byte) 0x67, (byte) 0x3d, (byte) 0x22, (byte) 0x55, (byte) 0x54, (byte) 0x46, (byte) 0x2d, (byte) 0x38, (byte) 0x22, (byte) 0x3f, (byte) 0x3e, (byte) 0x3c, (byte) 0x44, (byte) 0x65, (byte) 0x76, (byte) 0x69, (byte) 0x63, (byte) 0x65, (byte) 0x20, (byte) 0x78,
                (byte) 0x6d, (byte) 0x6c, (byte) 0x6e, (byte) 0x73, (byte) 0x3d, (byte) 0x22, (byte) 0x68, (byte) 0x74, (byte) 0x74, (byte) 0x70, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x77, (byte) 0x77, (byte) 0x77, (byte) 0x2e, (byte) 0x67, (byte) 0x61, (byte) 0x72,
                (byte) 0x6d, (byte) 0x69, (byte) 0x6e, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x2f, (byte) 0x78, (byte) 0x6d, (byte) 0x6c, (byte) 0x73, (byte) 0x63, (byte) 0x68, (byte) 0x65, (byte) 0x6d, (byte) 0x61, (byte) 0x73, (byte) 0x2f, (byte) 0x47,
                (byte) 0x61, (byte) 0x72, (byte) 0x6d, (byte) 0x69, (byte) 0x6e, (byte) 0x44, (byte) 0x65, (byte) 0x76, (byte) 0x69, (byte) 0x63, (byte) 0x65, (byte) 0x2f, (byte) 0x76, (byte) 0x32, (byte) 0x22, (byte) 0x20, (byte) 0x78, (byte) 0x6d, (byte) 0x6c, (byte) 0x6e,
                (byte) 0x73, (byte) 0x3a, (byte) 0x78, (byte) 0x73, (byte) 0x69, (byte) 0x3d, (byte) 0x22, (byte) 0x68, (byte) 0x74, (byte) 0x74, (byte) 0x70, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x77, (byte) 0x77, (byte) 0x77, (byte) 0x2e, (byte) 0x77, (byte) 0x33,
                (byte) 0x2e, (byte) 0x6f, (byte) 0x72, (byte) 0x67, (byte) 0x2f, (byte) 0x32, (byte) 0x30, (byte) 0x30, (byte) 0x31, (byte) 0x2f, (byte) 0x58, (byte) 0x4d, (byte) 0x4c, (byte) 0x53, (byte) 0x63, (byte) 0x68, (byte) 0x65, (byte) 0x6d, (byte) 0x61, (byte) 0x2d,
                (byte) 0x69, (byte) 0x6e, (byte) 0x73, (byte) 0x74, (byte) 0x61, (byte) 0x6e, (byte) 0x63, (byte) 0x65, (byte) 0x22, (byte) 0x20, (byte) 0x78, (byte) 0x73, (byte) 0x69, (byte) 0x3a, (byte) 0x73, (byte) 0x63, (byte) 0x68, (byte) 0x65, (byte) 0x6d, (byte) 0x61,
                (byte) 0x4c, (byte) 0x6f, (byte) 0x63, (byte) 0x61, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x3d, (byte) 0x22, (byte) 0x68, (byte) 0x74, (byte) 0x74, (byte) 0x70, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x77, (byte) 0x77, (byte) 0x77,
                (byte) 0x2e, (byte) 0x67, (byte) 0x61, (byte) 0x72, (byte) 0x6d, (byte) 0x69, (byte) 0x6e, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x2f, (byte) 0x78, (byte) 0x6d, (byte) 0x6c, (byte) 0x73, (byte) 0x63, (byte) 0x68, (byte) 0x65, (byte) 0x6d,
                (byte) 0x61, (byte) 0x73, (byte) 0x2f, (byte) 0x47, (byte) 0x61, (byte) 0x72, (byte) 0x6d, (byte) 0x69, (byte) 0x6e, (byte) 0x44, (byte) 0x65, (byte) 0x76, (byte) 0x69, (byte) 0x63, (byte) 0x65, (byte) 0x2f, (byte) 0x76, (byte) 0x32, (byte) 0x20, (byte) 0x68,
                (byte) 0x74, (byte) 0x74, (byte) 0x70, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x77, (byte) 0x77, (byte) 0x77, (byte) 0x2e, (byte) 0x67, (byte) 0x61, (byte) 0x72, (byte) 0x6d, (byte) 0x69, (byte) 0x6e, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d,
                (byte) 0x2f, (byte) 0x78, (byte) 0x6d, (byte) 0x6c, (byte) 0x73, (byte) 0x63, (byte) 0x68, (byte) 0xfb, (byte) 0x65, (byte) 0x6d, (byte) 0x61, (byte) 0x73, (byte) 0x2f, (byte) 0x47, (byte) 0x61, (byte) 0x72, (byte) 0x6d, (byte) 0x69, (byte) 0x6e, (byte) 0x44,
                (byte) 0x65, (byte) 0x76, (byte) 0x69, (byte) 0x63, (byte) 0x65, (byte) 0x76, (byte) 0x32, (byte) 0x2e, (byte) 0x78, (byte) 0x73, (byte) 0x64, (byte) 0x22, (byte) 0x3e, (byte) 0x0a, (byte) 0x3c, (byte) 0x4d, (byte) 0x6f, (byte) 0x64, (byte) 0x65, (byte) 0x6c,
                (byte) 0x3e, (byte) 0x3c, (byte) 0x50, (byte) 0x61, (byte) 0x72, (byte) 0x74, (byte) 0x4e, (byte) 0x75, (byte) 0x6d, (byte) 0x62, (byte) 0x65, (byte) 0x72, (byte) 0x3e, (byte) 0x30, (byte) 0x30, (byte) 0x36, (byte) 0x2d, (byte) 0x42, (byte) 0x32, (byte) 0x37,
                (byte) 0x37, (byte) 0x32, (byte) 0x2d, (byte) 0x30, (byte) 0x30, (byte) 0x3c, (byte) 0x2f, (byte) 0x50, (byte) 0x61, (byte) 0x72, (byte) 0x74, (byte) 0x4e, (byte) 0x75, (byte) 0x6d, (byte) 0x62, (byte) 0x65, (byte) 0x72, (byte) 0x3e, (byte) 0x3c, (byte) 0x53,
                (byte) 0x6f, (byte) 0x66, (byte) 0x74, (byte) 0x77, (byte) 0x61, (byte) 0x72, (byte) 0x65, (byte) 0x56, (byte) 0x65, (byte) 0x72, (byte) 0x73, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x3e, (byte) 0x34, (byte) 0x35, (byte) 0x30, (byte) 0x3c, (byte) 0x2f,
                (byte) 0x53, (byte) 0x6f, (byte) 0x66, (byte) 0x74, (byte) 0x77, (byte) 0x61, (byte) 0x72, (byte) 0x65, (byte) 0x56, (byte) 0x65, (byte) 0x72, (byte) 0x73, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x3e, (byte) 0x3c, (byte) 0x44, (byte) 0x65, (byte) 0x73,
                (byte) 0x63, (byte) 0x72, (byte) 0x69, (byte) 0x70, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x3e, (byte) 0x76, (byte) 0xc3, (byte) 0xad, (byte) 0x76, (byte) 0x6f, (byte) 0x6d, (byte) 0x6f, (byte) 0x76, (byte) 0x65, (byte) 0x20, (byte) 0x48,
                (byte) 0x52, (byte) 0x3c, (byte) 0x2f, (byte) 0x44, (byte) 0x65, (byte) 0x73, (byte) 0x63, (byte) 0x72, (byte) 0x69, (byte) 0x70, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x3e, (byte) 0x3c, (byte) 0x2f, (byte) 0x4d, (byte) 0x6f, (byte) 0x64,
                (byte) 0x65, (byte) 0x6c, (byte) 0x3e, (byte) 0x3c, (byte) 0x49, (byte) 0x64, (byte) 0x3e, (byte) 0x33, (byte) 0x39, (byte) 0x38, (byte) 0x37, (byte) 0x39, (byte) 0x30, (byte) 0x38, (byte) 0x30, (byte) 0x37, (byte) 0x32, (byte) 0x3c, (byte) 0x2f, (byte) 0x49,
                (byte) 0x64, (byte) 0x3e, (byte) 0x3c, (byte) 0x4d, (byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0x53, (byte) 0x74, (byte) 0x6f, (byte) 0x72, (byte) 0x61, (byte) 0x67, (byte) 0x65, (byte) 0x4d, (byte) 0x6f, (byte) 0x64, (byte) 0x65, (byte) 0x3e, (byte) 0x0a,
                (byte) 0x3c, (byte) 0x44, (byte) 0x61, (byte) 0x74, (byte) 0x61, (byte) 0x54, (byte) 0x79, (byte) 0x70, (byte) 0x65, (byte) 0x3e, (byte) 0x3c, (byte) 0x4e, (byte) 0x61, (byte) 0x6d, (byte) 0x65, (byte) 0x3e, (byte) 0x46, (byte) 0x49, (byte) 0x54, (byte) 0x42,
                (byte) 0x69, (byte) 0x6e, (byte) 0x61, (byte) 0x72, (byte) 0x79, (byte) 0x3c, (byte) 0x2f, (byte) 0x4e, (byte) 0x61, (byte) 0x6d, (byte) 0x65, (byte) 0x3e, (byte) 0x3c, (byte) 0x46, (byte) 0x69, (byte) 0x6c, (byte) 0x65, (byte) 0x3e, (byte) 0x3c, (byte) 0x53,
                (byte) 0x70, (byte) 0x65, (byte) 0x63, (byte) 0x69, (byte) 0x66, (byte) 0x69, (byte) 0x63, (byte) 0x61, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x3e, (byte) 0x3c, (byte) 0x49, (byte) 0x64, (byte) 0x28, (byte) 0x0b, (byte) 0x00
        });
        packet = parser.retrievePacket();
        assertNotNull(packet);
        packet = parser.retrievePacket();
        assertNull(packet);

        parser.receivedBytes(new byte[]{(byte) 0x00, (byte) 0x05, (byte) 0xad, (byte) 0x01, (byte) 0x8c, (byte) 0x13, (byte) 0x03, (byte) 0xbe, (byte) 0x4f, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x03, (byte) 0x90, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01});
        packet = parser.retrievePacket();
        assertNull(packet);
    }

    @Test
    public void testWrap() {
        byte[] packet = GfdiPacketParser.wrapMessageToPacket(new byte[]{(byte) 0x21, (byte) 0x00, (byte) 0xa0, (byte) 0x13, (byte) 0x6f, (byte) 0x00, (byte) 0xd4, (byte) 0x0a, (byte) 0xe8, (byte) 0xa5, (byte) 0xb2, (byte) 0xed, (byte) 0xc2, (byte) 0x01, (byte) 0x08, (byte) 0x02, (byte) 0x00, (byte) 0x0c, (byte) 0x76, (byte) 0xc3, (byte) 0xad, (byte) 0x76, (byte) 0x6f, (byte) 0x6d, (byte) 0x6f, (byte) 0x76, (byte) 0x65, (byte) 0x20, (byte) 0x48, (byte) 0x52, (byte) 0x00, (byte) 0x87, (byte) 0xd4});

        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x02, (byte) 0x21, (byte) 0x04, (byte) 0xa0, (byte) 0x13, (byte) 0x6f, (byte) 0x0b, (byte) 0xd4, (byte) 0x0a, (byte) 0xe8, (byte) 0xa5, (byte) 0xb2, (byte) 0xed, (byte) 0xc2, (byte) 0x01, (byte) 0x08, (byte) 0x02, (byte) 0x0e, (byte) 0x0c, (byte) 0x76, (byte) 0xc3, (byte) 0xad, (byte) 0x76, (byte) 0x6f, (byte) 0x6d, (byte) 0x6f, (byte) 0x76, (byte) 0x65, (byte) 0x20, (byte) 0x48, (byte) 0x52, (byte) 0x03, (byte) 0x87, (byte) 0xd4, (byte) 0x00}, packet);
    }

    @Test
    public void testRoundtrip() {
        final DeviceInformationResponseMessage message = new DeviceInformationResponseMessage(VivomoveConstants.STATUS_ACK, 112, -1, VivomoveConstants.GADGETBRIDGE_UNIT_NUMBER, 123, 16384, "BT", "Test", "Model", 1);
        testRoundtrip(message.packet);

        final byte[] longPacket = new byte[500];
        Arrays.fill(longPacket, (byte) 0xAB);
        testRoundtrip(longPacket);
    }

    private static void testRoundtrip(byte[] packet) {
        final GfdiPacketParser parser = new GfdiPacketParser();
        final byte[] bytes = GfdiPacketParser.wrapMessageToPacket(packet);
        parser.receivedBytes(bytes);

        System.out.println(GB.hexdump(bytes, 0, bytes.length));

        final byte[] parsedPacket = parser.retrievePacket();
        assertNotNull(parsedPacket);

        assertArrayEquals(packet, parsedPacket);
    }
}
