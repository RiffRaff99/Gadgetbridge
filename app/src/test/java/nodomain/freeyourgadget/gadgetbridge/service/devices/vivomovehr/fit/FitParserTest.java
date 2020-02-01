package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit;

import nodomain.freeyourgadget.gadgetbridge.util.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class FitParserTest {
    @Test
    public void parseFitFile() throws IOException {
        final FitParser fitParser = new FitParser(FitMessageDefinitions.ALL_DEFINITIONS);
        for (File file : new File("c:\\Temp\\fit\\").listFiles()) {
            dumpFitFile(file, fitParser);
        }
    }

    @Test
    public void d() throws IOException {
        dumpFitFile(new File("y:\\_3rdparty\\Garmin-Vivoactive\\DEVICE.FIT"), new FitParser(FitMessageDefinitions.ALL_DEFINITIONS));
    }

    private static void dumpFitFile(File file, FitParser fitParser) throws IOException {
        System.out.println(" ******* " + file.getName());
        final byte[] fitBytes = FileUtils.readAll(new FileInputStream(file), Long.MAX_VALUE);
        final List<FitMessage> fitData = fitParser.parseFitFile(fitBytes);
        assertNotNull(fitData);
        for (FitMessage message : fitData) {
            System.out.println(message);
        }
    }
}
