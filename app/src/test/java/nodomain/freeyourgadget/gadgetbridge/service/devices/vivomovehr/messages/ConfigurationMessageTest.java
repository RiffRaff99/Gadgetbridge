package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages;

import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.GarminCapability;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class ConfigurationMessageTest {
    @Test
    public void parsePacket() {
        final ConfigurationMessage configurationMessage = ConfigurationMessage.parsePacket(new byte[]{(byte) 0x11, (byte) 0x00, (byte) 0xba, (byte) 0x13, (byte) 0x0a, (byte) 0xf9, (byte) 0x03, (byte) 0x00, (byte) 0x24, (byte) 0x02, (byte) 0x4c, (byte) 0x10, (byte) 0x00, (byte) 0x80, (byte) 0x00, (byte) 0x43, (byte) 0x26});
        final Set<GarminCapability> garminCapabilities = GarminCapability.setFromBinary(configurationMessage.configurationPayload);
        assertNotNull(garminCapabilities);
    }
}
