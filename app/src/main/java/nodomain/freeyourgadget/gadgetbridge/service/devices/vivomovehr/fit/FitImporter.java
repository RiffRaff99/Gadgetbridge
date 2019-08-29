package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit;

import nodomain.freeyourgadget.gadgetbridge.GBApplication;
import nodomain.freeyourgadget.gadgetbridge.database.DBHandler;
import nodomain.freeyourgadget.gadgetbridge.database.DBHelper;
import nodomain.freeyourgadget.gadgetbridge.devices.vivomovehr.VivomoveHrSampleProvider;
import nodomain.freeyourgadget.gadgetbridge.entities.DaoSession;
import nodomain.freeyourgadget.gadgetbridge.entities.Device;
import nodomain.freeyourgadget.gadgetbridge.entities.User;
import nodomain.freeyourgadget.gadgetbridge.impl.GBDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FitImporter {
    private static final Logger LOG = LoggerFactory.getLogger(FitImporter.class);

    private final GBDevice gbDevice;

    public FitImporter(GBDevice gbDevice) {
        this.gbDevice = gbDevice;
    }

    public void processFitData(List<FitMessage> messages) {
        try (DBHandler dbHandler = GBApplication.acquireDB()) {
            final DaoSession session = dbHandler.getDaoSession();

            final Device device = DBHelper.getDevice(gbDevice, session);
            final User user = DBHelper.getUser(session);
            final VivomoveHrSampleProvider provider = new VivomoveHrSampleProvider(gbDevice, session);

            for (FitMessage message : messages) {
                /*
                switch (message.definition.globalMessageID) {
                    case FitMessageDefinitions.FIT_MESSAGE_NUMBER_FILE_ID:
                }
                final VivomoveHrActivitySample sample = new VivomoveHrActivitySample();
                sample.setDevice(device);
                sample.setUser(user);
                sample.setTimestamp(timestampInSeconds);
                sample.setProvider(provider);

                sample.setCaloriesBurnt(lastSample.getCaloriesBurnt());
                sample.setFloorsClimbed(lastSample.getFloorsClimbed());
                sample.setHeartRate(lastSample.getHeartRate());
                sample.setSteps(lastSample.getSteps());
                sample.setRawIntensity(ActivitySample.NOT_MEASURED);
                sample.setRawKind(ActivityKind.TYPE_ACTIVITY); // to make it visible in the charts TODO: add a MANUAL kind for that?

                LOG.debug("Publishing sample");
                provider.addGBActivitySample(sample);
                 */
            }
        } catch (Exception e) {
            LOG.error("Error saving real-time activity data", e);
        }
    }
}
