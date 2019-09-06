package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit;

import nodomain.freeyourgadget.gadgetbridge.devices.vivomovehr.VivomoveHrSampleProvider;
import nodomain.freeyourgadget.gadgetbridge.entities.VivomoveHrActivitySample;
import nodomain.freeyourgadget.gadgetbridge.model.ActivitySample;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.GarminTimeUtils;

import java.util.List;

public class FitImporter {
    public void processFitData(List<FitMessage> messages, FitImportProcessor processor) {
        boolean ohrEnabled = false;
        int softwareVersion = -1;

        int lastTimestamp = 0;
        for (FitMessage message : messages) {
            switch (message.definition.globalMessageID) {
                case FitMessageDefinitions.FIT_MESSAGE_NUMBER_EVENT:
                    //message.getField();
                    break;

                case FitMessageDefinitions.FIT_MESSAGE_NUMBER_SOFTWARE:
                    final Integer versionField = message.getIntegerField("version");
                    if (versionField != null) softwareVersion = versionField;
                    break;

                case FitMessageDefinitions.FIT_MESSAGE_NUMBER_MONITORING_INFO:
                    lastTimestamp = message.getIntegerField("timestamp");
                    break;

                case FitMessageDefinitions.FIT_MESSAGE_NUMBER_MONITORING:
                    lastTimestamp = processMonitoringMessage(message, ohrEnabled, lastTimestamp, processor);
                    break;

                case FitMessageDefinitions.FIT_MESSAGE_NUMBER_OHR_SETTINGS:
                    final Boolean isOhrEnabled = message.getBooleanField("enabled");
                    if (isOhrEnabled != null) ohrEnabled = isOhrEnabled;
                    break;

                case FitMessageDefinitions.FIT_MESSAGE_NUMBER_SLEEP_LEVEL:
                    processSleepLevelMessage(message, processor);
                    break;
            }
        }
    }

    private static void processSleepLevelMessage(FitMessage message, FitImportProcessor processor) {
        final Integer timestamp = message.getIntegerField("timestamp");
        final Integer sleepLevel = message.getIntegerField("sleep_level");

        final VivomoveHrActivitySample sample = new VivomoveHrActivitySample();
        sample.setTimestamp(GarminTimeUtils.garminTimestampToUnixTime(timestamp));

        sample.setCaloriesBurnt(0);
        sample.setHeartRate(ActivitySample.NOT_MEASURED);
        sample.setSteps(0);
        sample.setRawIntensity(0);
        sample.setRawKind(VivomoveHrSampleProvider.RAW_TYPE_KIND_SLEEP | sleepLevel);

        processor.onSample(sample);
    }

    private static int processMonitoringMessage(FitMessage message, boolean ohrEnabled, int lastTimestamp, FitImportProcessor processor) {
        final Integer activityType = message.getIntegerField("activity_type");
        final Double activeCalories = message.getNumericField("active_calories");
        final Integer intensity = message.getIntegerField("intensity");
        final Integer cycles = message.getIntegerField("cycles");
        final Double heartRate = message.getNumericField("heart_rate");
        final Integer timestampFull = message.getIntegerField("timestamp");
        final Integer timestamp16 = message.getIntegerField("timestamp_16");
        final Double activeTime = message.getNumericField("active_time");

        if (timestampFull != null) {
            lastTimestamp = timestampFull;
        } else if (timestamp16 != null) {
            lastTimestamp += (timestamp16 - (lastTimestamp & 0xFFFF)) & 0xFFFF;
        } else {
            // TODO: timestamp_min_8
            throw new IllegalArgumentException("Unsupported timestamp");
        }

        final VivomoveHrActivitySample sample = new VivomoveHrActivitySample();
        sample.setTimestamp(GarminTimeUtils.garminTimestampToUnixTime(lastTimestamp));

        sample.setCaloriesBurnt(activeCalories == null ? ActivitySample.NOT_MEASURED : (int) Math.round(activeCalories));
        //sample.setFloorsClimbed(lastSample.getFloorsClimbed());
        sample.setHeartRate(ohrEnabled && heartRate != null && heartRate > 0 ? (int) Math.round(heartRate) : ActivitySample.NOT_MEASURED);
        sample.setSteps(cycles == null ? ActivitySample.NOT_MEASURED : cycles);
        sample.setRawIntensity(intensity == null ? 0 : intensity);
        sample.setRawKind(activityType == null ? 0 : (VivomoveHrSampleProvider.RAW_TYPE_KIND_ACTIVITY | activityType));

        processor.onSample(sample);
        return lastTimestamp;
    }
}
