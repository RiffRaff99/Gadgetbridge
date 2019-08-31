package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages;

import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.ancs.AncsControlCommand;
import org.junit.Test;

import static org.junit.Assert.*;

public class GncsControlPointMessageTest {
    @Test
    public void parsePacket() {
        final GncsControlPointMessage message = GncsControlPointMessage.parsePacket(new byte[] {
                (byte) 0x16, 0, (byte) 0xaa, (byte) 0x13, 0, (byte) 0x8a, (byte) 0xda,
                (byte) 0x6a, (byte) 0x5d, (byte) 0x05, (byte) 0x01, (byte) 0x13, 0, (byte) 0x02,
                (byte) 0x1f, 0, (byte) 0x03, (byte) 0x3f, 0,
                (byte) 0x04, (byte) 0x5a, (byte) 0xe1
        });
        assertNotNull(message);
    }
}
