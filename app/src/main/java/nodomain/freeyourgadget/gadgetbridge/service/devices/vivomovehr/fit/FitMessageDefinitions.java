package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit;

public class FitMessageDefinitions {
    public static final int MESSAGE_ID_CONNECTIVITY = 0;
    public static final int MESSAGE_ID_WEATHER_CONDITIONS = 6;

    public static final int FIT_MESSAGE_NUMBER_CONNECTIVITY = 127;
    public static final int FIT_MESSAGE_NUMBER_WEATHER_CONDITIONS = 128;

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

    public static final FitMessageDefinition definitionWeatherConditions = new FitMessageDefinition(FIT_MESSAGE_NUMBER_WEATHER_CONDITIONS, MESSAGE_ID_WEATHER_CONDITIONS,
            new FitMessageFieldDefinition("timestamp", 253, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("weather_report", 0, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("temperature", 1, 1, FitFieldBaseType.SINT8, null),
            new FitMessageFieldDefinition("condition", 2, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("wind_direction", 3, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("wind_speed", 4, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("precipitation_probability", 5, 1, FitFieldBaseType.UINT8, null),
            new FitMessageFieldDefinition("temperature_feels_like", 6, 1, FitFieldBaseType.SINT8, null),
            new FitMessageFieldDefinition("relative_humidity", 7, 1, FitFieldBaseType.UINT8, null),
            new FitMessageFieldDefinition("location", 8, 16, FitFieldBaseType.STRING, null),
            new FitMessageFieldDefinition("observed_at_time", 9, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("observed_location_lat", 10, 4, FitFieldBaseType.SINT32, null),
            new FitMessageFieldDefinition("observed_location_long", 11, 4, FitFieldBaseType.SINT32, null),
            new FitMessageFieldDefinition("day_of_week", 12, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("high_temperature", 13, 1, FitFieldBaseType.SINT8, null),
            new FitMessageFieldDefinition("low_temperature", 14, 1, FitFieldBaseType.SINT8, null)
    );
}
