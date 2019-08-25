package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages;

import nodomain.freeyourgadget.gadgetbridge.devices.vivomovehr.VivomoveConstants;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.BinaryUtils;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.ChecksumCalculator;

import java.util.Arrays;

public class SetDeviceSettingsMessage {
    public final byte[] packet;

    public ConfigurationMessage(Map<GarminDeviceSetting, Object> settings) {
        if (settings.size() > 255) throw new IllegalArgumentException("Too many settings");

        final MessageWriter writer = new MessageWriter(7 + configurationPayload.length);
        writer.writeShort(0); // packet size will be filled below
        writer.writeShort(VivomoveConstants.MESSAGE_DEVICE_SETTINGS);
        writer.writeByte(settings.size());
		for (Map.Entry<GarminDeviceSetting, Object> settingPair : settings.entries()) {
			final GarminDeviceSetting setting = settingPair.getFirst();
			final Object value = settingPair.getSecond();
			if (value instanceof String) {
				writer.writeString((String)value);
			} else if (value instanceof Integer) {
				writer.writeByte(4);
				writer.writeInt(value);
			} else if (value instanceof Boolean) {
				writer.writeByte(1);
				writer.writeByte(Boolean.TRUE.equals(value) ? 1 : 0);
			} else {
				throw new IllegalArgumentException("Unsupported setting value type " + value);
			}
		}
        writer.writeShort(0); // CRC will be filled below
        final byte[] packet = writer.getBytes();
        BinaryUtils.writeShort(packet, 0, packet.length);
        BinaryUtils.writeShort(packet, packet.length - 2, ChecksumCalculator.computeCrc(packet, 0, packet.length - 2));
        this.packet = packet;
    }
}
