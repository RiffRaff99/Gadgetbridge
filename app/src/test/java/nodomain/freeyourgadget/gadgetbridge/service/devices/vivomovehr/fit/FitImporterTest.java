package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit;

import nodomain.freeyourgadget.gadgetbridge.devices.vivomovehr.VivomoveHrSampleProvider;
import nodomain.freeyourgadget.gadgetbridge.entities.VivomoveHrActivitySample;
import nodomain.freeyourgadget.gadgetbridge.util.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertNotNull;

public class FitImporterTest {
    @Test
    public void processFitData() throws IOException {
        final FitImporter fitImporter = new FitImporter();

        final FitParser fitParser = new FitParser(FitMessageDefinitions.ALL_DEFINITIONS);
        for (File file : new File("c:\\Temp\\fit\\").listFiles()) {
            final byte[] fitBytes = FileUtils.readAll(new FileInputStream(file), Long.MAX_VALUE);
            final List<FitMessage> fitData = fitParser.parseFitFile(fitBytes);
            assertNotNull(fitData);

            fitImporter.importFitData(fitData);
        }

        final FitImportProcessor processor = new TestProcessor();
        fitImporter.processImportedData(processor);
    }

    private static class TestProcessor implements FitImportProcessor {
        @Override
        public void onSample(VivomoveHrActivitySample sample) {
            System.out.println(String.format(Locale.ROOT, "%s: %s %d %% (%d steps, HR %d, %d kcal)", new Date(sample.getTimestamp() * 1000L), VivomoveHrSampleProvider.rawKindToString(sample.getRawKind()), sample.getRawIntensity() * 100 / 255, sample.getSteps(), sample.getHeartRate(), sample.getCaloriesBurnt()));
        }
    }
}
