package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit;

import android.util.SparseIntArray;
import nodomain.freeyourgadget.gadgetbridge.devices.vivomovehr.VivomoveHrSampleProvider;
import nodomain.freeyourgadget.gadgetbridge.entities.VivomoveHrActivitySample;
import nodomain.freeyourgadget.gadgetbridge.model.ActivitySample;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.GarminTimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class FitImporter {
    private final SortedMap<Integer, List<FitEvent>> eventsPerTimestamp = new TreeMap<>();

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
                    processSleepLevelMessage(message);
                    break;

                case FitMessageDefinitions.FIT_MESSAGE_NUMBER_MONITORING_HR_DATA:
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
        for (final Map.Entry<Integer, List<FitEvent>> eventsForTimestamp : eventsPerTimestamp.entrySet()) {
            final VivomoveHrActivitySample sample = new VivomoveHrActivitySample();
            sample.setTimestamp(eventsForTimestamp.getKey());

            sample.setRawKind(ActivitySample.NOT_MEASURED);
            sample.setCaloriesBurnt(ActivitySample.NOT_MEASURED);
            sample.setSteps(ActivitySample.NOT_MEASURED);
            sample.setHeartRate(ActivitySample.NOT_MEASURED);
            sample.setFloorsClimbed(ActivitySample.NOT_MEASURED);
            sample.setRawIntensity(ActivitySample.NOT_MEASURED);

            FitEvent.EventKind bestKind = FitEvent.EventKind.UNKNOWN;
            float bestScore = Float.NEGATIVE_INFINITY;
            for (final FitEvent event : eventsForTimestamp.getValue()) {
                if (event.getHeartRate() > sample.getHeartRate()) sample.setHeartRate(event.getHeartRate());
                if (event.getFloorsClimbed() > sample.getFloorsClimbed())
                    sample.setFloorsClimbed(event.getFloorsClimbed());

                float score = 0;
                if (event.getRawKind() > 0) score += 1;
                if (event.getCaloriesBurnt() > 0) score += event.getCaloriesBurnt() * 10.0f;
                if (event.getSteps() > 0) score += event.getSteps();
                if (event.getRawIntensity() > 0) score += 10.0f * event.getRawIntensity();
                if (event.getKind().isBetterThan(bestKind) || (event.getKind() == bestKind && score > bestScore)) {
                    bestScore = score;
                    bestKind = event.getKind();
                    sample.setRawKind(event.getRawKind());
                    sample.setCaloriesBurnt(event.getCaloriesBurnt());
                    sample.setSteps(event.getSteps());
                    sample.setRawIntensity(event.getRawIntensity());
                }
            }

            if (sample.getHeartRate() == ActivitySample.NOT_MEASURED && ((sample.getRawKind() & VivomoveHrSampleProvider.RAW_TYPE_KIND_SLEEP) != 0)) {
                sample.setRawKind(VivomoveHrSampleProvider.RAW_NOT_WORN);
                sample.setRawIntensity(0);
            }

            processor.onSample(sample);
        }
    }

    private void processSleepLevelMessage(FitMessage message) {
        final Integer timestampFull = message.getIntegerField("timestamp");
        final Integer sleepLevel = message.getIntegerField("sleep_level");

        final int timestamp = GarminTimeUtils.garminTimestampToUnixTime(timestampFull);
        final int rawIntensity = (4 - sleepLevel) * 40;
        final int rawKind = VivomoveHrSampleProvider.RAW_TYPE_KIND_SLEEP | sleepLevel;

        addEvent(new FitEvent(timestamp, FitEvent.EventKind.SLEEP, rawKind, ActivitySample.NOT_MEASURED, ActivitySample.NOT_MEASURED, ActivitySample.NOT_MEASURED, ActivitySample.NOT_MEASURED, rawIntensity));
    }

    private int processMonitoringMessage(FitMessage message, boolean ohrEnabled, int lastTimestamp, SparseIntArray lastCycles) {
        final Integer activityType = message.getIntegerField("activity_type");
        final Double activeCalories = message.getNumericField("active_calories");
        final Integer intensity = message.getIntegerField("intensity");
        final Integer cycles = message.getIntegerField("cycles");
        final Double heartRateMeasured = message.getNumericField("heart_rate");
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

        int timestamp = GarminTimeUtils.garminTimestampToUnixTime(lastTimestamp);
        int rawKind, caloriesBurnt, floorsClimbed, heartRate, steps, rawIntensity;
        FitEvent.EventKind eventKind;

        if (ohrEnabled && (heartRateMeasured == null || heartRateMeasured <= 0)) {
            rawKind = VivomoveHrSampleProvider.RAW_NOT_WORN;

            eventKind = FitEvent.EventKind.NOT_WORN;
            caloriesBurnt = ActivitySample.NOT_MEASURED;
            floorsClimbed = ActivitySample.NOT_MEASURED;
            heartRate = ActivitySample.NOT_MEASURED;
            steps = ActivitySample.NOT_MEASURED;
            rawIntensity = ActivitySample.NOT_MEASURED;
        } else {
            eventKind = FitEvent.EventKind.ACTIVITY;
            caloriesBurnt = activeCalories == null ? ActivitySample.NOT_MEASURED : (int) Math.round(activeCalories);
            floorsClimbed = ActivitySample.NOT_MEASURED;
            heartRate = ohrEnabled ? (int) Math.round(heartRateMeasured) : ActivitySample.NOT_MEASURED;
            steps = currentCycles == null ? ActivitySample.NOT_MEASURED : currentCycles;
            rawIntensity = intensity == null ? 0 : intensity;
            rawKind = VivomoveHrSampleProvider.RAW_TYPE_KIND_ACTIVITY | activityTypeOrDefault;
        }

        if (rawKind != ActivitySample.NOT_MEASURED
                || caloriesBurnt != ActivitySample.NOT_MEASURED
                || floorsClimbed != ActivitySample.NOT_MEASURED
                || heartRate != ActivitySample.NOT_MEASURED
                || steps != ActivitySample.NOT_MEASURED
                || rawIntensity != ActivitySample.NOT_MEASURED) {

            addEvent(new FitEvent(timestamp, eventKind, rawKind, caloriesBurnt, floorsClimbed, heartRate, steps, rawIntensity));
        }
        return lastTimestamp;
    }

    private void processHrDataMessage(FitMessage message) {
    }

    private void processStressLevelMessage(FitMessage message) {
    }

    private void processMaxMetDataMessage(FitMessage message) {
    }

    private void addEvent(FitEvent event) {
        List<FitEvent> eventsForTimestamp = eventsPerTimestamp.get(event.getTimestamp());
        if (eventsForTimestamp == null) {
            eventsForTimestamp = new ArrayList<>();
            eventsPerTimestamp.put(event.getTimestamp(), eventsForTimestamp);
        }
        eventsForTimestamp.add(event);
    }

    private static class FitEvent {
        private final int timestamp;
        private final EventKind kind;
        private final int rawKind;
        private final int caloriesBurnt;
        private final int floorsClimbed;
        private final int heartRate;
        private final int steps;
        private final int rawIntensity;

        private FitEvent(int timestamp, EventKind kind, int rawKind, int caloriesBurnt, int floorsClimbed, int heartRate, int steps, int rawIntensity) {
            this.timestamp = timestamp;
            this.kind = kind;
            this.rawKind = rawKind;
            this.caloriesBurnt = caloriesBurnt;
            this.floorsClimbed = floorsClimbed;
            this.heartRate = heartRate;
            this.steps = steps;
            this.rawIntensity = rawIntensity;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public EventKind getKind() {
            return kind;
        }

        public int getRawKind() {
            return rawKind;
        }

        public int getCaloriesBurnt() {
            return caloriesBurnt;
        }

        public int getFloorsClimbed() {
            return floorsClimbed;
        }

        public int getHeartRate() {
            return heartRate;
        }

        public int getSteps() {
            return steps;
        }

        public int getRawIntensity() {
            return rawIntensity;
        }

        public enum EventKind {
            UNKNOWN,
            NOT_WORN,
            ACTIVITY,
            SLEEP;

            public boolean isBetterThan(EventKind other) {
                return ordinal() > other.ordinal();
            }
        }
    }
}
