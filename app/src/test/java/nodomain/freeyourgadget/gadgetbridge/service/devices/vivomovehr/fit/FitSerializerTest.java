package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit;

import android.util.SparseArray;
import nodomain.freeyourgadget.gadgetbridge.util.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class FitSerializerTest {
    @Test
    public void test() throws IOException {
        final FitSerializer fitSerializer = new FitSerializer();
        final FitMessage fileIdMessage = new FitMessage(FitMessageDefinitions.DEFINITION_FILE_ID);
        fileIdMessage.setField("serial_number",-307059224);
        fileIdMessage.setField("time_created", -1);
        fileIdMessage.setField("manufacturer", 1);
        fileIdMessage.setField("product", 2772);
        fileIdMessage.setField("number", 0);
        fileIdMessage.setField("type", 12);

        final byte[] serializedBytes = fitSerializer.serializeFitFile(Collections.singletonList(fileIdMessage));
        Files.write(new File("c:\\Temp\\garmin-simple-settings.fit").toPath(), serializedBytes, StandardOpenOption.CREATE);

        final List<FitMessage> parsedMessages = new FitParser(FitMessageDefinitions.ALL_DEFINITIONS).parseFitFile(serializedBytes);
        Assert.assertEquals(1, parsedMessages.size());
    }

    @Test
    public void d() throws IOException {
        roundTripFitFile(new File("c:\\Temp\\garmin\\SETTINGS\\SETTINGS.FIT"), new File("c:\\Temp\\garmin-roundtrip-settings.fit"), new FitParser(FitMessageDefinitions.ALL_DEFINITIONS));
    }

    private static void roundTripFitFile(File inputFile, File outputFile, FitParser fitParser) throws IOException {
        final byte[] fitBytes = FileUtils.readAll(new FileInputStream(inputFile), Long.MAX_VALUE);
        final List<FitMessage> fitData = fitParser.parseFitFile(fitBytes);
        assertNotNull(fitData);
        System.out.println("*** Read data:");
        for(final FitMessage message : fitData) {
            System.out.println(message.definition.globalMessageID);
            System.out.println(message);
        }
//        final SparseArray<FitLocalMessageDefinition> localMessageDefinitionsFromParser = fitParser.getLocalMessageDefinitions();
//        final SparseArray<FitLocalMessageDefinition> localDefinitions = new SparseArray<>(localMessageDefinitionsFromParser.size());
//        for (final FitLocalMessageDefinition definitionFromParser : localMessageDefinitionsFromParser) {
//            localDefinitions.append(definitionFromParser.globalDefinition.localMessageID, new FitLocalMessageDefinition(definitionFromParser.globalDefinition, ));
//        }
        final FitSerializer fitSerializer = new FitSerializer(fitParser.getLocalMessageDefinitions());
        final byte[] serializedBytes = fitSerializer.serializeFitFile(fitData);

        Files.write(outputFile.toPath(), serializedBytes, StandardOpenOption.CREATE);

        System.out.println("*** Reparsing data:");
        final List<FitMessage> reparsedMessages = fitParser.parseFitFile(serializedBytes);
        Assert.assertEquals(fitData.size(), reparsedMessages.size());
    }
}
