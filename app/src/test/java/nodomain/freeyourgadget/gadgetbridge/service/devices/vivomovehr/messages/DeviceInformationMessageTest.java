package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages;

import org.junit.Test;

import static org.junit.Assert.*;

public class DeviceInformationMessageTest {
    @Test
    public void parsePacket() {
        final DeviceInformationMessage deviceInformationMessage = DeviceInformationMessage.parsePacket(new byte[]{(byte) 0x21, (byte) 0x00, (byte) 0xa0, (byte) 0x13, (byte) 0x6f, (byte) 0x00, (byte) 0xd4, (byte) 0x0a, (byte) 0xe8, (byte) 0xa5, (byte) 0xb2, (byte) 0xed, (byte) 0xc2, (byte) 0x01, (byte) 0x08, (byte) 0x02, (byte) 0x00, (byte) 0x0c, (byte) 0x76, (byte) 0xc3, (byte) 0xad, (byte) 0x76, (byte) 0x6f, (byte) 0x6d, (byte) 0x6f, (byte) 0x76, (byte) 0x65, (byte) 0x20, (byte) 0x48, (byte) 0x52, (byte) 0x00, (byte) 0x87, (byte) 0xd4});
        assertEquals(111, deviceInformationMessage.protocolVersion);
        assertEquals(2772, deviceInformationMessage.productNumber);
        assertEquals("3987908072", deviceInformationMessage.unitNumber);
        assertEquals(450, deviceInformationMessage.softwareVersion);
        assertEquals("4.50", deviceInformationMessage.getSoftwareVersionStr());
        assertEquals(520, deviceInformationMessage.maxPacketSize);
        assertEquals("", deviceInformationMessage.bluetoothFriendlyName);
        assertEquals("vívomove HR", deviceInformationMessage.deviceName);
        assertEquals("", deviceInformationMessage.deviceModel);
    }
}
