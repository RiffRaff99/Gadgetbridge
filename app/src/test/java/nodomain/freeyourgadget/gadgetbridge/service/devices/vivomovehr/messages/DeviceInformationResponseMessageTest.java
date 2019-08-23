package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages;

import nodomain.freeyourgadget.gadgetbridge.devices.vivomovehr.VivomoveConstants;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeviceInformationResponseMessageTest {
    @Test
    public void testCreate() {
        final DeviceInformationResponseMessage message = new DeviceInformationResponseMessage(VivomoveConstants.STATUS_ACK, 112, -1, VivomoveConstants.GADGETBRIDGE_UNIT_NUMBER, 123, 16384, "BT", "Test", "Model", 1);
        assertNotNull(message.packet);
    }
}
