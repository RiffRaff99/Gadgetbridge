package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr;

import java.nio.charset.StandardCharsets;

public final class BinaryUtils {
    private BinaryUtils() {
    }

    public static int readByte(byte[] array, int offset) {
        return array[offset] & 0xFF;
    }

    public static int readShort(byte[] array, int offset) {
        return (array[offset] & 0xFF) | ((array[offset + 1] & 0xFF) << 8);
    }

    public static int readInt(byte[] array, int offset) {
        return (array[offset] & 0xFF) | ((array[offset + 1] & 0xFF) << 8) | ((array[offset + 2] & 0xFF) << 16) | ((array[offset + 3] & 0xFF) << 24);
    }

    public static String readString(byte[] array, int offset, int size) {
        return new String(array, offset, size, StandardCharsets.UTF_8);
    }
}
