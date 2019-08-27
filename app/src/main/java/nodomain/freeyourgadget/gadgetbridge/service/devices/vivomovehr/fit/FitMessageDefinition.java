package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit;

import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.MessageWriter;

import java.util.Arrays;
import java.util.List;

public class FitMessageDefinition {
    public final int globalMessageID;
    public final int localMessageID;
    public final List<FitMessageFieldDefinition> fieldDefinitions;

    public FitMessageDefinition(int globalMessageID, int localMessageID, FitMessageFieldDefinition... fieldDefinitions) {
        this.globalMessageID = globalMessageID;
        this.localMessageID = localMessageID;
        this.fieldDefinitions = Arrays.asList(fieldDefinitions);
    }

    public void writeToMessage(MessageWriter writer) {
        writer.writeByte(localMessageID | 0x40);
        writer.writeByte(0);
        writer.writeByte(0);
        writer.writeShort(globalMessageID);
        writer.writeByte(fieldDefinitions.size());
        for (FitMessageFieldDefinition fieldDefinition : fieldDefinitions) {
            fieldDefinition.writeToMessage(writer);
        }
    }
}
