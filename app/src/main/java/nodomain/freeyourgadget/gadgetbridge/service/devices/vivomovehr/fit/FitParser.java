package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit;

import android.util.SparseArray;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.MessageReader;
import nodomain.freeyourgadget.gadgetbridge.util.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FitParser {
    private static final Logger LOG = LoggerFactory.getLogger(FitParser.class);

    // “.FIT” – magic value indicating a .FIT file
    private static final int FIT_MAGIC = 0x5449462E;

    private static final int FLAG_NORMAL_HEADER = 0x80;
    private static final int FLAG_DEFINITION_MESSAGE = 0x40;
    private static final int FLAG_DEVELOPER_FIELDS = 0x20;
    private static final int MASK_LOCAL_MESSAGE_TYPE = 0x0F;
    private static final int MASK_TIME_OFFSET = 0x1F;
    private static final int MASK_COMPRESSED_LOCAL_MESSAGE_TYPE = 0x60;

    private final SparseArray<FitMessageDefinition> globalMessageDefinitions;
    private final SparseArray<FitLocalMessageDefinition> localMessageDefinitions = new SparseArray<>(16);

    public FitParser(Collection<FitMessageDefinition> knownDefinitions) {
        globalMessageDefinitions = new SparseArray<>(knownDefinitions.size());
        for (FitMessageDefinition definition : knownDefinitions) {
            globalMessageDefinitions.append(definition.globalMessageID, definition);
        }
    }

    public List<FitMessage> parseFitFile(byte[] data) {
        if (data.length < 12) throw new IllegalArgumentException("Too short data");

        final MessageReader reader = new MessageReader(data);
        final List<FitMessage> result = new ArrayList<>();
        while (!reader.isEof()) {
            final int fileHeaderSize = reader.readByte();
            final int protocolVersion = reader.readByte();
            final int profileVersion = reader.readShort();
            final int dataSize = reader.readInt();
            final int dataTypeMagic = reader.readInt();
            final int headerCrc = fileHeaderSize >= 14 ? reader.readShort() : 0;
            if (dataTypeMagic != FIT_MAGIC) {
                throw new IllegalArgumentException("Not a FIT file, data type signature not found");
            }
            if (fileHeaderSize < 12) throw new IllegalArgumentException("Header size too low");
            reader.skip(fileHeaderSize - 14);

            // TODO: Check header CRC

            localMessageDefinitions.clear();

            int lastTimestamp = 0;
            final int end = fileHeaderSize + dataSize;
            while (reader.getPosition() < end) {
                final int recordHeader = reader.readByte();
                final boolean isDefinitionMessage;
                final int localMessageType;
                final int currentTimestamp;
                if ((recordHeader & FLAG_NORMAL_HEADER) == 0) {
                    // normal header
                    isDefinitionMessage = (recordHeader & FLAG_DEFINITION_MESSAGE) != 0;
                    localMessageType = recordHeader & MASK_LOCAL_MESSAGE_TYPE;
                    currentTimestamp = -1;
                } else {
                    // compressed timestamp header
                    final int timestampOffset = recordHeader & MASK_TIME_OFFSET;
                    localMessageType = (recordHeader & MASK_COMPRESSED_LOCAL_MESSAGE_TYPE) >> 4;
                    currentTimestamp = lastTimestamp + timestampOffset;
                    isDefinitionMessage = false;
                    throw new IllegalArgumentException("Compressed timestamps not supported yet");
                }

                if (isDefinitionMessage) {
                    final boolean hasDeveloperFields = (recordHeader & FLAG_DEVELOPER_FIELDS) != 0;
                    final FitLocalMessageDefinition definition = parseDefinitionMessage(reader, hasDeveloperFields);
                    LOG.trace("Defining local message {} to global message {}", localMessageType, definition.globalDefinition.globalMessageID);
                    localMessageDefinitions.put(localMessageType, definition);
                } else {
                    final FitLocalMessageDefinition definition = localMessageDefinitions.get(localMessageType);
                    if (definition == null) {
                        LOG.error("Use of undefined local message {}", localMessageType);
                        throw new IllegalArgumentException("Use of undefined local message " + localMessageType);
                    }
                    final FitMessage dataMessage = new FitMessage(definition.globalDefinition);
                    parseDataMessage(reader, definition, dataMessage);
                    result.add(dataMessage);
                }
            }

            final int fileCrc = reader.readShort();
            // TODO: Check file CRC
        }
        return result;
    }

    private void parseDataMessage(MessageReader reader, FitLocalMessageDefinition localMessageDefinition, FitMessage dataMessage) {
        for (FitLocalFieldDefinition localFieldDefinition : localMessageDefinition.fieldDefinitions) {
            final Object value = readValue(reader, localFieldDefinition);
            if (!localFieldDefinition.baseType.invalidValue.equals(value)) {
                dataMessage.setField(localFieldDefinition.globalDefinition.fieldNumber, value);
            }
        }
    }

    private Object readValue(MessageReader reader, FitLocalFieldDefinition fieldDefinition) {
        switch (fieldDefinition.baseType) {
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
            case SINT64:
            case UINT64:
            case UINT64Z:
                return readFitNumber(reader, fieldDefinition.size, fieldDefinition.globalDefinition.scale, fieldDefinition.globalDefinition.offset);
            case BYTE:
                return fieldDefinition.size == 1 ? reader.readByte() : reader.readBytes(fieldDefinition.size);
            case STRING:
                return readFitString(reader, fieldDefinition.size);
            // TODO: Float data types
            default:
                throw new IllegalArgumentException("Unable to read value of type " + fieldDefinition.baseType);
        }
    }

    private String readFitString(MessageReader reader, int size) {
        final byte[] bytes = reader.readBytes(size);
        final int zero = ArrayUtils.indexOf(bytes, (byte) 0);
        if (zero < 0) {
            LOG.warn("Unterminated string");
            return new String(bytes, StandardCharsets.UTF_8);
        }
        return new String(bytes, 0, zero, StandardCharsets.UTF_8);
    }

    private Object readFitNumber(MessageReader reader, int size, double scale, double offset) {
        if (scale == 0) {
            switch (size) {
                case 1:
                    return reader.readByte();
                case 2:
                    return reader.readShort();
                case 4:
                    return reader.readInt();
                case 8:
                    return reader.readLong();
                default:
                    throw new IllegalArgumentException("Unable to read number of size " + size);
            }
        } else {
            switch (size) {
                case 1:
                    return reader.readByte() / scale + offset;
                case 2:
                    return reader.readShort() / scale + offset;
                case 4:
                    return reader.readInt() / scale + offset;
                case 8:
                    return reader.readLong() / scale + offset;
                default:
                    throw new IllegalArgumentException("Unable to read number of size " + size);
            }
        }
    }

    private FitLocalMessageDefinition parseDefinitionMessage(MessageReader reader, boolean hasDeveloperFields) {
        reader.skip(1);
        final int architecture = reader.readByte();
        final boolean isBigEndian = architecture == 1;
        if (isBigEndian) throw new IllegalArgumentException("Big-endian data not supported yet");
        final int globalMessageType = reader.readShort();
        final FitMessageDefinition messageDefinition = getGlobalDefinition(globalMessageType);

        final int fieldCount = reader.readByte();
        final List<FitLocalFieldDefinition> fields = new ArrayList<>(fieldCount);
        for (int i = 0; i < fieldCount; ++i) {
            final int globalField = reader.readByte();
            final int size = reader.readByte();
            final int baseTypeNum = reader.readByte();
            final FitFieldBaseType baseType = FitFieldBaseType.decodeTypeID(baseTypeNum);

            final FitMessageFieldDefinition globalFieldDefinition = getFieldDefinition(messageDefinition, globalField, size, baseType);

            fields.add(new FitLocalFieldDefinition(globalFieldDefinition, size, baseType));
        }
        if (hasDeveloperFields) {
            final int developerFieldCount = reader.readByte();
            if (developerFieldCount != 0) throw new IllegalArgumentException("Developer fields not supported yet");
        }

        return new FitLocalMessageDefinition(messageDefinition, fields);
    }

    private FitMessageFieldDefinition getFieldDefinition(FitMessageDefinition messageDefinition, int field, int size, FitFieldBaseType baseType) {
        final FitMessageFieldDefinition definition = messageDefinition.getField(field);
        if (definition != null) return definition;

        LOG.warn("Unknown field {} in message {}", field, messageDefinition.globalMessageID);
        // System.out.println(String.format(Locale.ROOT, "Unknown field %d in message %d", field, messageDefinition.globalMessageID));
        final FitMessageFieldDefinition newDefinition = new FitMessageFieldDefinition("unknown_" + field, field, size, baseType, baseType.invalidValue);
        messageDefinition.addField(newDefinition);
        return newDefinition;
    }

    private FitMessageDefinition getGlobalDefinition(int globalMessageType) {
        final FitMessageDefinition messageDefinition = globalMessageDefinitions.get(globalMessageType);
        if (messageDefinition != null) return messageDefinition;

        LOG.warn("Unknown global message {}", globalMessageType);
        // System.out.println(String.format(Locale.ROOT, "Unknown message %d", globalMessageType));
        final FitMessageDefinition newDefinition = new FitMessageDefinition("unknown_" + globalMessageType, globalMessageType, 0);
        globalMessageDefinitions.append(globalMessageType, newDefinition);
        return newDefinition;
    }
}
