package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit;

public class FitMessageDefinitions {
    public static final int MESSAGE_ID_CONNECTIVITY = 0;

    public static final int FIT_MESSAGE_NUMBER_CONNECTIVITY = 127;

    public static final FitMessageDefinition definitionConnectivity = new FitMessageDefinition(FIT_MESSAGE_NUMBER_CONNECTIVITY, MESSAGE_ID_CONNECTIVITY,
            new FitMessageFieldDefinition("bluetooth_enabled", 0, 1, FitFieldBaseType.ENUM, FitBool.FALSE),
            new FitMessageFieldDefinition("bluetooth_le_enabled", 1, 1, FitFieldBaseType.ENUM, FitBool.FALSE),
            new FitMessageFieldDefinition("ant_enabled", 2, 1, FitFieldBaseType.ENUM, FitBool.FALSE),
            new FitMessageFieldDefinition("live_tracking_enabled", 4, 1, FitFieldBaseType.ENUM, FitBool.FALSE),
            new FitMessageFieldDefinition("weather_conditions_enabled", 5, 1, FitFieldBaseType.ENUM, FitBool.FALSE),
            new FitMessageFieldDefinition("weather_alerts_enabled", 6, 1, FitFieldBaseType.ENUM, FitBool.FALSE),
            new FitMessageFieldDefinition("auto_activity_upload_enabled", 7, 1, FitFieldBaseType.ENUM, FitBool.FALSE),
            new FitMessageFieldDefinition("course_download_enabled", 8, 1, FitFieldBaseType.ENUM, FitBool.FALSE),
            new FitMessageFieldDefinition("workout_download_enabled", 9, 1, FitFieldBaseType.ENUM, FitBool.FALSE),
            new FitMessageFieldDefinition("gps_ephemeris_download_enabled", 10, 1, FitFieldBaseType.ENUM, FitBool.FALSE),
            new FitMessageFieldDefinition("live_track_auto_start_enabled", 13, 1, FitFieldBaseType.ENUM, FitBool.FALSE)
    );
}
