package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit;

import nodomain.freeyourgadget.gadgetbridge.entities.VivomoveHrActivitySample;
import nodomain.freeyourgadget.gadgetbridge.util.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class FitImporterTest {
    @Test
    public void processFitData() throws IOException {
        final FitImportProcessor processor = new TestProcessor();
        final FitImporter fitImporter = new FitImporter();

        final FitParser fitParser = new FitParser(FitMessageDefinitions.ALL_DEFINITIONS);
        for (File file : new File("c:\\Temp\\fit\\").listFiles()) {
            final byte[] fitBytes = FileUtils.readAll(new FileInputStream(file), Long.MAX_VALUE);
            final List<FitMessage> fitData = fitParser.parseFitFile(fitBytes);
            assertNotNull(fitData);

            fitImporter.processFitData(fitData, processor);
        }
    }

    private static class TestProcessor implements FitImportProcessor {
        @Override
        public void onSample(VivomoveHrActivitySample sample) {
            System.out.println(sample);
        }
    }
}
