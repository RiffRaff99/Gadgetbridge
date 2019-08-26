package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages;

import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.BinaryUtils;

import java.nio.charset.StandardCharsets;

public class MessageReader {
    private final byte[] data;
    private int position;

    public MessageReader(byte[] data) {
        this.data = data;
    }

    public MessageReader(byte[] data, int skipOffset) {
        this.data = data;
        this.position = skipOffset;
    }

    public int readByte() {
        if (position + 1 > data.length) throw new IllegalStateException();
        final int result = BinaryUtils.readByte(data, position);
        ++position;
        return result;
    }

    public int readShort() {
        if (position + 2 > data.length) throw new IllegalStateException();
        final int result = BinaryUtils.readShort(data, position);
        position += 2;
        return result;
    }

    public int readInt() {
        if (position + 4 > data.length) throw new IllegalStateException();
        final int result = BinaryUtils.readInt(data, position);
        position += 4;
        return result;
    }

    public String readString() {
        final int size = readByte();
        if (position + size > data.length) throw new IllegalStateException();
        final String result = new String(data, position, size, StandardCharsets.UTF_8);
        position += size;
        return result;
    }

    public byte[] readBytes(int size) {
        final byte[] result = new byte[size];
        System.arraycopy(data, position, result, 0, size);
        position += size;
        return result;
    }
}
