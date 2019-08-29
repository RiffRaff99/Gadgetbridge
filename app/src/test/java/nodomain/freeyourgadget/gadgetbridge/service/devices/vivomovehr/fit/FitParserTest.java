package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit;

import nodomain.freeyourgadget.gadgetbridge.util.FileUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class FitParserTest {
    @Test
    public void parseFitFile() throws IOException {
        final byte[] fitBytes = FileUtils.readAll(new FileInputStream("c:\\Temp\\fit\\vivomovehr-1.fit"), Long.MAX_VALUE);
        final List<FitMessage> fitData = new FitParser(FitMessageDefinitions.ALL_DEFINITIONS).parseFitFile(fitBytes);
        assertNotNull(fitData);
    }
}
