package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages;

import java.util.Locale;

import static nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.BinaryUtils.*;

public class DeviceInformationMessage {
    public final int protocolVersion;
    public final int productNumber;
    public final String unitNumber;
    public final int softwareVersion;
    public final int maxPacketSize;
    public final String bluetoothFriendlyName;
    public final String deviceName;
    public final String deviceModel;
    // dual-pairing flags & MAC addresses...

    public DeviceInformationMessage(int protocolVersion, int productNumber, String unitNumber, int softwareVersion, int maxPacketSize, String bluetoothFriendlyName, String deviceName, String deviceModel) {
        this.protocolVersion = protocolVersion;
        this.productNumber = productNumber;
        this.unitNumber = unitNumber;
        this.softwareVersion = softwareVersion;
        this.maxPacketSize = maxPacketSize;
        this.bluetoothFriendlyName = bluetoothFriendlyName;
        this.deviceName = deviceName;
        this.deviceModel = deviceModel;
    }

    public static DeviceInformationMessage parsePacket(byte[] packet) {
        final int protocolVersion = readShort(packet, 4);
        final int productNumber = readShort(packet, 6);
        final String unitNumber = Long.toString(readInt(packet, 8) & 0xFFFFFFFFL);
        final int softwareVersion = readShort(packet, 12);
        final int maxPacketSize = readShort(packet, 14);
        final int bluetoothFriendlyNameSize = readByte(packet, 16);
        final String bluetoothFriendlyName = readString(packet, 17, bluetoothFriendlyNameSize);
        final int deviceNameOffset = 17 + bluetoothFriendlyNameSize;
        final int deviceNameSize = readByte(packet, deviceNameOffset);
        final String deviceName = readString(packet, deviceNameOffset + 1, deviceNameSize);
        final int deviceModelOffset = deviceNameOffset + 1 + deviceNameSize;
        final int deviceModelSize = readByte(packet, deviceModelOffset);
        final String deviceModel = readString(packet, deviceModelOffset + 1, deviceModelSize);

        return new DeviceInformationMessage(protocolVersion, productNumber, unitNumber, softwareVersion, maxPacketSize, bluetoothFriendlyName, deviceName, deviceModel);
    }

    public String getSoftwareVersionStr() {
        int softwareVersionMajor = softwareVersion / 100;
        int softwareVersionMinor = softwareVersion % 100;
        return String.format(Locale.ROOT, "%d.%02d", softwareVersionMajor, softwareVersionMinor);
    }
}
