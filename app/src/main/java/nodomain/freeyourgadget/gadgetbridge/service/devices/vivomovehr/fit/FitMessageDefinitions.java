package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit;

import java.util.Arrays;
import java.util.List;

public class FitMessageDefinitions {
    public static final int MESSAGE_ID_CONNECTIVITY = 0;
    public static final int MESSAGE_ID_WEATHER_ALERT = 5;
    public static final int MESSAGE_ID_WEATHER_CONDITIONS = 6;

    public static final int FIT_MESSAGE_NUMBER_FILE_ID = 0;
    private static final int FIT_MESSAGE_NUMBER_SOFTWARE = 35;
    public static final int FIT_MESSAGE_NUMBER_CONNECTIVITY = 127;
    public static final int FIT_MESSAGE_NUMBER_WEATHER_CONDITIONS = 128;
    public static final int FIT_MESSAGE_NUMBER_WEATHER_ALERT = 129;

    public static final FitMessageDefinition DEFINITION_FILE_ID = new FitMessageDefinition("file_id", FIT_MESSAGE_NUMBER_FILE_ID, -1,
            new FitMessageFieldDefinition("type", 0, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("manufacturer", 1, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("product", 2, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("serial_number", 3, 4, FitFieldBaseType.UINT32Z, null),
            new FitMessageFieldDefinition("time_created", 4, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("number", 5, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("manufacturer_partner", 6, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("product_name", 8, 20, FitFieldBaseType.STRING, null)
    );

    public static final FitMessageDefinition DEFINITION_SOFTWARE = new FitMessageDefinition("software", FIT_MESSAGE_NUMBER_SOFTWARE, -1,
            new FitMessageFieldDefinition("message_index", 254, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("version", 3, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("part_number", 5, 20, FitFieldBaseType.STRING, null),
            new FitMessageFieldDefinition("version_string", 6, 20, FitFieldBaseType.STRING, null)
    );

    public static final FitMessageDefinition DEFINITION_CONNECTIVITY = new FitMessageDefinition("connectivity", FIT_MESSAGE_NUMBER_CONNECTIVITY, MESSAGE_ID_CONNECTIVITY,
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

    public static final FitMessageDefinition DEFINITION_WEATHER_CONDITIONS = new FitMessageDefinition("weather_conditions", FIT_MESSAGE_NUMBER_WEATHER_CONDITIONS, MESSAGE_ID_WEATHER_CONDITIONS,
            new FitMessageFieldDefinition("timestamp", 253, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("weather_report", 0, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("temperature", 1, 1, FitFieldBaseType.SINT8, 1, 0, "°C", null),
            new FitMessageFieldDefinition("condition", 2, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("wind_direction", 3, 2, FitFieldBaseType.UINT16, 1, 0, "°", null),
            new FitMessageFieldDefinition("wind_speed", 4, 2, FitFieldBaseType.UINT16, 1000, 0, "m/s", null),
            new FitMessageFieldDefinition("precipitation_probability", 5, 1, FitFieldBaseType.UINT8, 1, 0, "%", null),
            new FitMessageFieldDefinition("temperature_feels_like", 6, 1, FitFieldBaseType.SINT8, 1, 0, "°C", null),
            new FitMessageFieldDefinition("relative_humidity", 7, 1, FitFieldBaseType.UINT8, 1, 0, "%", null),
            new FitMessageFieldDefinition("location", 8, 16, FitFieldBaseType.STRING, null),
            new FitMessageFieldDefinition("observed_at_time", 9, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("observed_location_lat", 10, 4, FitFieldBaseType.SINT32, 1, 0, "semicircles", null),
            new FitMessageFieldDefinition("observed_location_long", 11, 4, FitFieldBaseType.SINT32, 1, 0, "semicircles", null),
            new FitMessageFieldDefinition("day_of_week", 12, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("high_temperature", 13, 1, FitFieldBaseType.SINT8, 1, 0, "°C", null),
            new FitMessageFieldDefinition("low_temperature", 14, 1, FitFieldBaseType.SINT8, 1, 0, "°C", null)
    );

    public static final FitMessageDefinition DEFINITION_WEATHER_ALERT = new FitMessageDefinition("weather_alert", FIT_MESSAGE_NUMBER_WEATHER_ALERT, MESSAGE_ID_WEATHER_ALERT,
            new FitMessageFieldDefinition("timestamp", 253, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("report_id", 0, 10, FitFieldBaseType.STRING, null),
            new FitMessageFieldDefinition("issue_time", 1, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("expire_time", 2, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("severity", 3, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("type", 4, 1, FitFieldBaseType.ENUM, null)
    );

    public static final List<FitMessageDefinition> ALL_DEFINITIONS = Arrays.asList(
            DEFINITION_FILE_ID,
            DEFINITION_SOFTWARE,
            DEFINITION_CONNECTIVITY,
            DEFINITION_WEATHER_CONDITIONS,
            DEFINITION_WEATHER_ALERT
    );
}
