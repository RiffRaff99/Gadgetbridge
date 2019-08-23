package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr;

import org.junit.Test;

import static org.junit.Assert.*;

public class GfdiPacketParserTest {
    @Test
    public void testParse() {
        final GfdiPacketParser parser = new GfdiPacketParser();
        parser.receivedBytes(new byte[]{(byte) 0x00, (byte) 0x02, (byte) 0x21, (byte) 0x04, (byte) 0xa0, (byte) 0x13, (byte) 0x6f, (byte) 0x0b, (byte) 0xd4, (byte) 0x0a, (byte) 0xe8, (byte) 0xa5, (byte) 0xb2, (byte) 0xed, (byte) 0xc2, (byte) 0x01, (byte) 0x08, (byte) 0x02, (byte) 0x0e, (byte) 0x0c});
        assertNull(parser.retrievePacket());
        parser.receivedBytes(new byte[]{(byte) 0x76, (byte) 0xc3, (byte) 0xad, (byte) 0x76, (byte) 0x6f, (byte) 0x6d, (byte) 0x6f, (byte) 0x76, (byte) 0x65, (byte) 0x20, (byte) 0x48, (byte) 0x52, (byte) 0x03, (byte) 0x87, (byte) 0xd4, (byte) 0x00});
        final byte[] packet = parser.retrievePacket();
        assertNotNull(packet);
        assertArrayEquals(new byte[]{(byte) 0x21, (byte) 0x00, (byte) 0xa0, (byte) 0x13, (byte) 0x6f, (byte) 0x00, (byte) 0xd4, (byte) 0x0a, (byte) 0xe8, (byte) 0xa5, (byte) 0xb2, (byte) 0xed, (byte) 0xc2, (byte) 0x01, (byte) 0x08, (byte) 0x02, (byte) 0x00, (byte) 0x0c, (byte) 0x76, (byte) 0xc3, (byte) 0xad, (byte) 0x76, (byte) 0x6f, (byte) 0x6d, (byte) 0x6f, (byte) 0x76, (byte) 0x65, (byte) 0x20, (byte) 0x48, (byte) 0x52, (byte) 0x00, (byte) 0x87, (byte) 0xd4}, packet);
    }
}
