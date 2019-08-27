package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit;

import android.util.SparseArray;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.MessageWriter;

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
                writeFitNumberToMessage(writer, (Integer) value, size);
                break;
            default:
                throw new IllegalArgumentException("Unable to write value of type " + type);
        }
    }

    private static void writeFitNumberToMessage(MessageWriter writer, int value, int size) {
        switch (size) {
            case 1:
                writer.writeByte(value);
                break;
            case 2:
                writer.writeShort(value);
                break;
            case 4:
                writer.writeInt(value);
                break;
            default:
                throw new IllegalArgumentException("Unable to write number of size " + size);
        }
    }
}
