package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr;

public class ChecksumCalculator {
    public static final int[] CONSTANTS = {0, 52225, 55297, 5120, 61441, 15360, 10240, 58369, 40961, 27648, 30720, 46081, 20480, 39937, 34817, 17408};

    public static int computeCrc(byte[] data, int offset, int length) {
        int crc = 0;
        for (int i = offset; i < offset + length; ++i) {
            int b = data[i];
            crc = (((crc >> 4) & 4095) ^ CONSTANTS[crc & 15]) ^ CONSTANTS[b & 15];
            crc = (((crc >> 4) & 4095) ^ CONSTANTS[crc & 15]) ^ CONSTANTS[(b >> 4) & 15];
        }
        return crc;
    }
}
