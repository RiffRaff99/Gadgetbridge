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

        for (final FitMessage msg : fitData) {
//            if (msg.definition.globalMessageID == FitMessageDefinitions.FIT_MESSAGE_NUMBER_DEVICE_SETTINGS) {
//                msg.setField("phone_notification_activity_filter", 0);
//                msg.setField("phone_notification_default_filter", 0);
//            }
            System.out.println(msg);
        }

        final FitSerializer fitSerializer = new FitSerializer(fitParser.getLocalMessageDefinitions());
        final byte[] serializedBytes = fitSerializer.serializeFitFile(fitData);

        Files.write(outputFile.toPath(), serializedBytes, StandardOpenOption.CREATE);

        final List<FitMessage> reparsedMessages = fitParser.parseFitFile(serializedBytes);
        Assert.assertEquals(fitData.size(), reparsedMessages.size());
    }
}
