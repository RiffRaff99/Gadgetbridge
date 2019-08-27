package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr;

import nodomain.freeyourgadget.gadgetbridge.devices.vivomovehr.VivomoveConstants;

public final class GarminTimeUtils {
    public static int unixTimeToGarminTimestamp(int timestamp) {
        return timestamp - VivomoveConstants.GARMIN_TIME_EPOCH;
    }

    public static int javaMillisToGarminTimestamp(long millis) {
        return (int) (millis / 1000) - VivomoveConstants.GARMIN_TIME_EPOCH;
    }
}
