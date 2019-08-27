package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit;

import android.util.SparseArray;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.MessageWriter;

import java.nio.charset.StandardCharsets;

public class FitMessage {
    private final FitMessageDefinition definition;
    private final SparseArray<Object> fieldValues = new SparseArray<>();

    public FitMessage(FitMessageDefinition definition) {
        this.definition = definition;
    }

    public void setField(int fieldNumber, Object value) {
        fieldValues.append(fieldNumber, value);
    }

    public void writeToMessage(MessageWriter writer) {
        writer.writeByte(definition.localMessageID);
        for (FitMessageFieldDefinition fieldDefinition : definition.fieldDefinitions) {
            final Object value = fieldValues.get(fieldDefinition.fieldNumber, fieldDefinition.defaultValue);
            writeFitValueToMessage(writer, value, fieldDefinition.fieldType, fieldDefinition.fieldSize);
        }
    }

    private static void writeFitValueToMessage(MessageWriter writer, Object value, FitFieldBaseType type, int size) {
        switch (type) {
            case ENUM:
            case SINT8:
            case UINT8:
            case SINT16:
            case UINT16:
            case SINT32:
            case UINT32:
            case UINT8Z:
            case UINT16Z:
            case UINT32Z:
            case BYTE:
                writeFitNumberToMessage(writer, (Integer) value, size);
                break;
            case SINT64:
            case UINT64:
            case UINT64Z:
                writeFitNumberToMessage(writer, (Long) value, size);
                break;
            case STRING:
                writeFitStringToMessage(writer, (String) value, size);
                break;
            // TODO: Float data types
            default:
                throw new IllegalArgumentException("Unable to write value of type " + type);
        }
    }

    private static void writeFitStringToMessage(MessageWriter writer, String value, int size) {
        final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        int valueSize = Math.min(bytes.length, size - 1);
        writer.writeBytes(bytes, 0, valueSize);
        for (int i = valueSize; i < size; ++i) {
            writer.writeByte(0);
        }
    }

    private static void writeFitNumberToMessage(MessageWriter writer, long value, int size) {
        switch (size) {
            case 1:
                writer.writeByte((int) value);
                break;
            case 2:
                writer.writeShort((int) value);
                break;
            case 4:
                writer.writeInt((int) value);
                break;
            case 8:
                writer.writeLong(value);
                break;
            default:
                throw new IllegalArgumentException("Unable to write number of size " + size);
        }
    }
}
