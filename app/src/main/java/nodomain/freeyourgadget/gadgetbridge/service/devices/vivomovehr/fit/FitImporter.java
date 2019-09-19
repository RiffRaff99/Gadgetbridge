package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit;

import android.util.SparseIntArray;
import nodomain.freeyourgadget.gadgetbridge.devices.vivomovehr.VivomoveHrSampleProvider;
import nodomain.freeyourgadget.gadgetbridge.entities.VivomoveHrActivitySample;
import nodomain.freeyourgadget.gadgetbridge.model.ActivitySample;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.GarminTimeUtils;

import java.util.List;

public class FitImporter {
    public void importFitData(List<FitMessage> messages) {
        boolean ohrEnabled = false;
        int softwareVersion = -1;

        int lastTimestamp = 0;
        final SparseIntArray lastCycles = new SparseIntArray();

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
                    lastTimestamp = processMonitoringMessage(message, ohrEnabled, lastTimestamp, lastCycles);
                    break;

                case FitMessageDefinitions.FIT_MESSAGE_NUMBER_OHR_SETTINGS:
                    final Boolean isOhrEnabled = message.getBooleanField("enabled");
                    if (isOhrEnabled != null) ohrEnabled = isOhrEnabled;
                    break;

                case FitMessageDefinitions.FIT_MESSAGE_NUMBER_SLEEP_LEVEL:
                    processSleepLevelMessage(messageú;
                    break;

                case FitMessageDefinitions.DEFINITION_MONITORING_HR_DATA:
                    processHrDataMessage(message);
                    break;

                case FitMessageDefinitions.FIT_MESSAGE_NUMBER_STRESS_LEVEL:
                    processStressLevelMessage(message);
                    break;

                case FitMessageDefinitions.FIT_MESSAGE_NUMBER_MAX_MET_DATA:
                    processMaxMetDataMessage(message);
                    break;
            }
        }
    }

    public void processImportedData(FitImportProcessor processor) {

    }

    private static void processSleepLevelMessage(FitMessage message, FitImportProcessor processor) {
        final Integer timestamp = message.getIntegerField("timestamp");
        final Integer sleepLevel = message.getIntegerField("sleep_level");

        final VivomoveHrActivitySample sample = new VivomoveHrActivitySample();
        sample.setTimestamp(GarminTimeUtils.garminTimestampToUnixTime(timestamp));

        sample.setHeartRate(ActivitySample.NOT_MEASURED);
        sample.setSteps(ActivitySample.NOT_MEASURED);
        sample.setCaloriesBurnt(ActivitySample.NOT_MEASURED);
        sample.setRawIntensity((4 - sleepLevel) * 40);
        sample.setRawKind(VivomoveHrSampleProvider.RAW_TYPE_KIND_SLEEP | sleepLevel);

        processor.onSample(sample);
    }

    private static int processMonitoringMessage(FitMessage message, boolean ohrEnabled, int lastTimestamp, SparseIntArray lastCycles) {
        final Integer activityType = message.getIntegerField("activity_type");
        final Double activeCalories = message.getNumericField("active_calories");
        final Integer intensity = message.getIntegerField("intensity");
        final Integer cycles = message.getIntegerField("cycles");
        final Double heartRate = message.getNumericField("heart_rate");
        final Integer timestampFull = message.getIntegerField("timestamp");
        final Integer timestamp16 = message.getIntegerField("timestamp_16");
        final Double activeTime = message.getNumericField("active_time");

        final int activityTypeOrDefault = activityType == null ? 0 : activityType;

        final int lastCycleCount = lastCycles.get(activityTypeOrDefault);
        final Integer currentCycles = cycles == null ? null : cycles < lastCycleCount ? cycles : cycles - lastCycleCount;
        if (currentCycles != null) {
            lastCycles.put(activityTypeOrDefault, cycles);
        }

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

        if (ohrEnabled && (heartRate == null || heartRate <= 0)) {
            sample.setRawKind(VivomoveHrSampleProvider.RAW_NOT_WORN);
            sample.setCaloriesBurnt(ActivitySample.NOT_MEASURED);
            //sample.setFloorsClimbed(ActivitySample.NOT_MEASURED);
            sample.setHeartRate(ActivitySample.NOT_MEASURED);
            sample.setSteps(ActivitySample.NOT_MEASURED);
            sample.setRawIntensity(ActivitySample.NOT_MEASURED);
        } else {
            sample.setCaloriesBurnt(activeCalories == null ? ActivitySample.NOT_MEASURED : (int) Math.round(activeCalories));
            //sample.setFloorsClimbed(lastSample.getFloorsClimbed());
            sample.setHeartRate(ohrEnabled ? (int) Math.round(heartRate) : ActivitySample.NOT_MEASURED);
            sample.setSteps(currentCycles == null ? ActivitySample.NOT_MEASURED : currentCycles);
            sample.setRawIntensity(intensity == null ? 0 : intensity);
            sample.setRawKind(activityType == null ? 0 : (VivomoveHrSampleProvider.RAW_TYPE_KIND_ACTIVITY | activityType));
        }

        if (sample.getCaloriesBurnt() != ActivitySample.NOT_MEASURED
                || sample.getHeartRate() != ActivitySample.NOT_MEASURED
                || sample.getSteps() != ActivitySample.NOT_MEASURED
                || sample.getRawIntensity() != ActivitySample.NOT_MEASURED) {
            processor.onSample(sample);
        }
        return lastTimestamp;
    }
}
