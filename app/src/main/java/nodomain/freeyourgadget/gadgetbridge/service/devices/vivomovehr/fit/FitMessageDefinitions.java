package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit;

import java.util.Arrays;
import java.util.List;

public class FitMessageDefinitions {
    public static final int MESSAGE_ID_CONNECTIVITY = 0;
    public static final int MESSAGE_ID_WEATHER_ALERT = 5;
    public static final int MESSAGE_ID_WEATHER_CONDITIONS = 6;

    public static final String FIT_MESSAGE_NAME_FILE_ID = "file_id";
    public static final String FIT_MESSAGE_NAME_EVENT = "event";
    public static final String FIT_MESSAGE_NAME_DEVICE_INFO = "device_info";
    public static final String FIT_MESSAGE_NAME_DEBUG = "debug";
    public static final String FIT_MESSAGE_NAME_SOFTWARE = "software";
    public static final String FIT_MESSAGE_NAME_FILE_CREATOR = "file_creator";
    public static final String FIT_MESSAGE_NAME_MONITORING = "monitoring";
    public static final String FIT_MESSAGE_NAME_MONITORING_INFO = "monitoring_info";
    public static final String FIT_MESSAGE_NAME_CONNECTIVITY = "connectivity";
    public static final String FIT_MESSAGE_NAME_WEATHER_CONDITIONS = "weather_conditions";
    public static final String FIT_MESSAGE_NAME_WEATHER_ALERT = "weather_alert";
    public static final String FIT_MESSAGE_NAME_OHR_SETTINGS = "ohr_settings";
    public static final String FIT_MESSAGE_NAME_MONITORING_HR_DATA = "monitoring_hr_data";
    public static final String FIT_MESSAGE_NAME_STRESS_LEVEL = "stress_level";
    public static final String FIT_MESSAGE_NAME_MANUAL_STRESS_LEVEL = "manual_stress_level";
    public static final String FIT_MESSAGE_NAME_MAX_MET_DATA = "max_met_data";
    public static final String FIT_MESSAGE_NAME_WHR_DIAG = "whr_diag";
    public static final String FIT_MESSAGE_NAME_METRICS_INFO = "metrics_info";
    public static final String FIT_MESSAGE_NAME_NEURAL_NETWORK_INFO = "neural_network_info";
    public static final String FIT_MESSAGE_NAME_NEURAL_NETWORK_DATA = "neural_network_data";
    public static final String FIT_MESSAGE_NAME_SLEEP_LEVEL = "sleep_level";
    public static final String FIT_MESSAGE_NAME_END_OF_FILE = "end_of_file";

    public static final int FIT_MESSAGE_NUMBER_FILE_ID = 0;
    public static final int FIT_MESSAGE_NUMBER_EVENT = 21;
    public static final int FIT_MESSAGE_NUMBER_DEVICE_INFO = 23;
    public static final int FIT_MESSAGE_NUMBER_DEBUG = 24;
    public static final int FIT_MESSAGE_NUMBER_SOFTWARE = 35;
    public static final int FIT_MESSAGE_NUMBER_FILE_CREATOR = 49;
    public static final int FIT_MESSAGE_NUMBER_MONITORING = 55;
    public static final int FIT_MESSAGE_NUMBER_MONITORING_INFO = 103;
    public static final int FIT_MESSAGE_NUMBER_CONNECTIVITY = 127;
    public static final int FIT_MESSAGE_NUMBER_WEATHER_CONDITIONS = 128;
    public static final int FIT_MESSAGE_NUMBER_WEATHER_ALERT = 129;
    public static final int FIT_MESSAGE_NUMBER_OHR_SETTINGS = 188;
    public static final int FIT_MESSAGE_NUMBER_MONITORING_HR_DATA = 211;
    public static final int FIT_MESSAGE_NUMBER_STRESS_LEVEL = 227;
    public static final int FIT_MESSAGE_NUMBER_MANUAL_STRESS_LEVEL = 228;
    public static final int FIT_MESSAGE_NUMBER_MAX_MET_DATA = 229;
    public static final int FIT_MESSAGE_NUMBER_WHR_DIAG = 233;
    public static final int FIT_MESSAGE_NUMBER_METRICS_INFO = 241;
    public static final int FIT_MESSAGE_NUMBER_NEURAL_NETWORK_INFO = 273;
    public static final int FIT_MESSAGE_NUMBER_NEURAL_NETWORK_DATA = 274;
    public static final int FIT_MESSAGE_NUMBER_SLEEP_LEVEL = 275;
    public static final int FIT_MESSAGE_NUMBER_END_OF_FILE = 276;

    public static final FitMessageDefinition DEFINITION_FILE_ID = new FitMessageDefinition(FIT_MESSAGE_NAME_FILE_ID, FIT_MESSAGE_NUMBER_FILE_ID, -1,
            new FitMessageFieldDefinition("type", 0, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("manufacturer", 1, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("product", 2, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("serial_number", 3, 4, FitFieldBaseType.UINT32Z, null),
            new FitMessageFieldDefinition("time_created", 4, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("number", 5, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("manufacturer_partner", 6, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("product_name", 8, 20, FitFieldBaseType.STRING, null)
    );

    public static final FitMessageDefinition DEFINITION_EVENT = new FitMessageDefinition(FIT_MESSAGE_NAME_EVENT, FIT_MESSAGE_NUMBER_EVENT, -1,
            new FitMessageFieldDefinition("timestamp", 253, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("event", 0, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("event_type", 1, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("data16", 2, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("data", 3, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("event_group", 4, 1, FitFieldBaseType.UINT8, null),
            new FitMessageFieldDefinition("device_index", 13, 1, FitFieldBaseType.UINT8, null),
            new FitMessageFieldDefinition("activity_type", 14, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("start_timestamp", 15, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("activity_subtype", 16, 1, FitFieldBaseType.ENUM, null)
    );

    public static final FitMessageDefinition DEFINITION_DEVICE_INFO = new FitMessageDefinition(FIT_MESSAGE_NAME_DEVICE_INFO, FIT_MESSAGE_NUMBER_DEVICE_INFO, -1,
            new FitMessageFieldDefinition("timestamp", 253, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("device_index", 0, 1, FitFieldBaseType.UINT8, null),
            new FitMessageFieldDefinition("device_type", 1, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("manufacturer", 2, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("serial_number", 3, 4, FitFieldBaseType.UINT32Z, null),
            new FitMessageFieldDefinition("product", 4, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("software_version", 5, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("hardware_version", 6, 1, FitFieldBaseType.UINT8, null),
            new FitMessageFieldDefinition("cum_operating_time", 7, 4, FitFieldBaseType.UINT32, 1, 0, "s", null),
            new FitMessageFieldDefinition("cum_training_time", 8, 4, FitFieldBaseType.UINT32, 1, 0, "s", null),
            new FitMessageFieldDefinition("reception", 9, 4, FitFieldBaseType.UINT8, 1, 0, "%", null),
            new FitMessageFieldDefinition("battery_voltage", 10, 2, FitFieldBaseType.UINT16, 256, 0, "V", null),
            new FitMessageFieldDefinition("battery_status", 11, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("rx_pass_count", 15, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("rx_fail_count", 16, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("software_version_string", 17, 20, FitFieldBaseType.STRING, null),
            new FitMessageFieldDefinition("sensor_position", 18, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("descriptor", 19, 20, FitFieldBaseType.STRING, null),
            new FitMessageFieldDefinition("ant_transmission_type", 20, 1, FitFieldBaseType.UINT8Z, null),
            new FitMessageFieldDefinition("ant_device_number", 21, 2, FitFieldBaseType.UINT16Z, null),
            new FitMessageFieldDefinition("ant_network", 22, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("source_type", 25, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("product_name", 27, 20, FitFieldBaseType.STRING, null)
    );

    public static final FitMessageDefinition DEFINITION_DEBUG = new FitMessageDefinition(FIT_MESSAGE_NAME_DEBUG, FIT_MESSAGE_NUMBER_DEBUG, -1,
            new FitMessageFieldDefinition("timestamp", 253, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("id", 0, 1, FitFieldBaseType.UINT8, null),
            new FitMessageFieldDefinition("string", 1, 20, FitFieldBaseType.STRING, null),
            new FitMessageFieldDefinition("data", 2, 20, FitFieldBaseType.BYTE, null),
            new FitMessageFieldDefinition("time256", 3, 20, FitFieldBaseType.UINT8, 256, 0, "s", null),
            new FitMessageFieldDefinition("fractional_timestamp", 4, 2, FitFieldBaseType.UINT16, 32768.0, 0, "s", null)
    );

    public static final FitMessageDefinition DEFINITION_SOFTWARE = new FitMessageDefinition(FIT_MESSAGE_NAME_SOFTWARE, FIT_MESSAGE_NUMBER_SOFTWARE, -1,
            new FitMessageFieldDefinition("message_index", 254, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("version", 3, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("part_number", 5, 20, FitFieldBaseType.STRING, null),
            new FitMessageFieldDefinition("version_string", 6, 20, FitFieldBaseType.STRING, null)
    );

    public static final FitMessageDefinition DEFINITION_FILE_CREATOR = new FitMessageDefinition(FIT_MESSAGE_NAME_FILE_CREATOR, FIT_MESSAGE_NUMBER_FILE_CREATOR, -1,
            new FitMessageFieldDefinition("software_version", 0, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("hardware_version", 1, 2, FitFieldBaseType.UINT8, null),
            new FitMessageFieldDefinition("creator_name", 2, 2, FitFieldBaseType.STRING, null)
    );

    public static final FitMessageDefinition DEFINITION_MONITORING = new FitMessageDefinition(FIT_MESSAGE_NAME_MONITORING, FIT_MESSAGE_NUMBER_MONITORING, -1,
            new FitMessageFieldDefinition("timestamp", 253, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("device_index", 0, 1, FitFieldBaseType.UINT8, null),
            new FitMessageFieldDefinition("calories", 1, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("distance", 2, 4, FitFieldBaseType.UINT32, 100, 0, "m", null),
            // TODO: Scale depends on activity type
            new FitMessageFieldDefinition("cycles", 3, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("active_time", 4, 1, FitFieldBaseType.UINT32, 1000, 0, "s", null),
            new FitMessageFieldDefinition("activity_type", 5, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("activity_subtype", 6, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("activity_level", 7, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("distance_16", 8, 2, FitFieldBaseType.UINT16, 0.01, 0, "m", null),
            new FitMessageFieldDefinition("cycles_16", 9, 2, FitFieldBaseType.UINT16, 0.5, 0, "cycles", null),
            new FitMessageFieldDefinition("active_time_16", 10, 2, FitFieldBaseType.UINT16, 1, 0, "s", null),
            new FitMessageFieldDefinition("local_timestamp", 11, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("temperature", 12, 2, FitFieldBaseType.SINT16, 100, 0, "°C", null),
            new FitMessageFieldDefinition("temperature_min", 14, 2, FitFieldBaseType.SINT16, 100, 0, "°C", null),
            new FitMessageFieldDefinition("temperature_max", 15, 2, FitFieldBaseType.SINT16, 100, 0, "°C", null),
            // TODO: Array
            new FitMessageFieldDefinition("activity_time", 16, 2, FitFieldBaseType.UINT16, 1, 0, "min", null),
            new FitMessageFieldDefinition("active_calories", 19, 2, FitFieldBaseType.UINT16, 1, 0, "kcal", null),
            new FitMessageFieldDefinition("current_activity_type_intensity", 24, 1, FitFieldBaseType.BYTE, null),
            new FitMessageFieldDefinition("timestamp_min_8", 25, 1, FitFieldBaseType.UINT8, null),
            new FitMessageFieldDefinition("timestamp_16", 26, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("heart_rate", 27, 1, FitFieldBaseType.UINT8, 1, 0, "bpm", null),
            new FitMessageFieldDefinition("intensity", 28, 1, FitFieldBaseType.UINT8, null),
            new FitMessageFieldDefinition("duration_min", 29, 2, FitFieldBaseType.UINT16, 1, 0, "min", null),
            new FitMessageFieldDefinition("duration", 30, 4, FitFieldBaseType.UINT32, 1, 0, "s", null),
            new FitMessageFieldDefinition("ascent", 31, 4, FitFieldBaseType.UINT32, 1000, 0, "m", null),
            new FitMessageFieldDefinition("descent", 32, 4, FitFieldBaseType.UINT32, 1000, 0, "m", null),
            new FitMessageFieldDefinition("moderate_activity_minutes", 33, 2, FitFieldBaseType.UINT16, 1, 0, "min", null),
            new FitMessageFieldDefinition("vigorous_activity_minutes", 34, 2, FitFieldBaseType.UINT16, 1, 0, "min", null),
            new FitMessageFieldDefinition("ascent_total", 35, 4, FitFieldBaseType.UINT32, 1000, 0, "m", null),
            new FitMessageFieldDefinition("descent_total", 36, 4, FitFieldBaseType.UINT32, 1000, 0, "m", null),
            new FitMessageFieldDefinition("moderate_activity_minutes_total", 37, 2, FitFieldBaseType.UINT16, 1, 0, "min", null),
            new FitMessageFieldDefinition("vigorous_activity_minutes_total", 38, 2, FitFieldBaseType.UINT16, 1, 0, "min", null)
    );

    public static final FitMessageDefinition DEFINITION_MONITORING_INFO = new FitMessageDefinition(FIT_MESSAGE_NAME_MONITORING_INFO, FIT_MESSAGE_NUMBER_MONITORING_INFO, -1,
            new FitMessageFieldDefinition("timestamp", 253, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("local_timestamp", 0, 4, FitFieldBaseType.UINT32, null),
            // TODO: Arrays
            new FitMessageFieldDefinition("activity_type", 1, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("cycles_to_distance", 3, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("cycles_to_calories", 4, 2, FitFieldBaseType.UINT16, null),

            new FitMessageFieldDefinition("resting_metabolic_rate", 5, 2, FitFieldBaseType.UINT16, null),
            new FitMessageFieldDefinition("cycles_goal", 7, 4, FitFieldBaseType.UINT32, 2, 0, "cycles", null),
            new FitMessageFieldDefinition("monitoring_time_source", 8, 1, FitFieldBaseType.ENUM, null)
    );

    public static final FitMessageDefinition DEFINITION_CONNECTIVITY = new FitMessageDefinition(FIT_MESSAGE_NAME_CONNECTIVITY, FIT_MESSAGE_NUMBER_CONNECTIVITY, MESSAGE_ID_CONNECTIVITY,
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

    public static final FitMessageDefinition DEFINITION_WEATHER_CONDITIONS = new FitMessageDefinition(FIT_MESSAGE_NAME_WEATHER_CONDITIONS, FIT_MESSAGE_NUMBER_WEATHER_CONDITIONS, MESSAGE_ID_WEATHER_CONDITIONS,
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

    public static final FitMessageDefinition DEFINITION_WEATHER_ALERT = new FitMessageDefinition(FIT_MESSAGE_NAME_WEATHER_ALERT, FIT_MESSAGE_NUMBER_WEATHER_ALERT, MESSAGE_ID_WEATHER_ALERT,
            new FitMessageFieldDefinition("timestamp", 253, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("report_id", 0, 10, FitFieldBaseType.STRING, null),
            new FitMessageFieldDefinition("issue_time", 1, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("expire_time", 2, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("severity", 3, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("type", 4, 1, FitFieldBaseType.ENUM, null)
    );

    public static final FitMessageDefinition DEFINITION_OHR_SETTINGS = new FitMessageDefinition(FIT_MESSAGE_NAME_OHR_SETTINGS, FIT_MESSAGE_NUMBER_OHR_SETTINGS, -1,
            new FitMessageFieldDefinition("timestamp", 253, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("enabled", 0, 1, FitFieldBaseType.ENUM, null)
    );

    public static final FitMessageDefinition DEFINITION_MONITORING_HR_DATA = new FitMessageDefinition(FIT_MESSAGE_NAME_MONITORING_HR_DATA, FIT_MESSAGE_NUMBER_MONITORING_HR_DATA, -1,
            new FitMessageFieldDefinition("timestamp", 253, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("resting_heart_rate", 0, 1, FitFieldBaseType.UINT8, 1, 0, "bpm", null),
            new FitMessageFieldDefinition("current_day_resting_heart_rate", 1, 1, FitFieldBaseType.UINT8, 1, 0, "bpm", null)
    );

    public static final FitMessageDefinition DEFINITION_STRESS_LEVEL = new FitMessageDefinition(FIT_MESSAGE_NAME_STRESS_LEVEL, FIT_MESSAGE_NUMBER_STRESS_LEVEL, -1,
            new FitMessageFieldDefinition("stress_level_value", 0, 2, FitFieldBaseType.SINT16, null),
            new FitMessageFieldDefinition("stress_level_time", 1, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("average_stress_intensity", 2, 1, FitFieldBaseType.SINT8, null)
    );

    public static final FitMessageDefinition DEFINITION_MANUAL_STRESS_LEVEL = new FitMessageDefinition(FIT_MESSAGE_NAME_MANUAL_STRESS_LEVEL, FIT_MESSAGE_NUMBER_MANUAL_STRESS_LEVEL, -1,
            new FitMessageFieldDefinition("stress_level_value", 0, 2, FitFieldBaseType.SINT16, null),
            new FitMessageFieldDefinition("stress_level_time", 1, 4, FitFieldBaseType.UINT32, null)
    );

    public static final FitMessageDefinition DEFINITION_MAX_MET_DATA = new FitMessageDefinition(FIT_MESSAGE_NAME_MAX_MET_DATA, FIT_MESSAGE_NUMBER_MAX_MET_DATA, -1,
            new FitMessageFieldDefinition("update_time", 0, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("max_met", 1, 4, FitFieldBaseType.SINT32, null),
            new FitMessageFieldDefinition("vo2_max", 2, 2, FitFieldBaseType.UINT16, 10, 0, "mL/kg/min", null),
            new FitMessageFieldDefinition("fitness_age", 3, 1, FitFieldBaseType.SINT8, null),
            new FitMessageFieldDefinition("fitness_age_desc", 4, 1, FitFieldBaseType.SINT8, null),
            new FitMessageFieldDefinition("sport", 5, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("sub_sport", 6, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("analyzer_method", 7, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("max_met_category", 8, 1, FitFieldBaseType.ENUM, null),
            new FitMessageFieldDefinition("calibrated_data", 9, 1, FitFieldBaseType.ENUM, null)
    );

    public static final FitMessageDefinition DEFINITION_WHR_DIAG = new FitMessageDefinition(FIT_MESSAGE_NAME_WHR_DIAG, FIT_MESSAGE_NUMBER_WHR_DIAG, -1,
            new FitMessageFieldDefinition("timestamp", 253, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("fractional_timestamp", 1, 2, FitFieldBaseType.UINT16, 32768.0, 0, "s", null),
            new FitMessageFieldDefinition("page_data", 2, 1, FitFieldBaseType.BYTE, null)
    );

    public static final FitMessageDefinition DEFINITION_METRICS_INFO = new FitMessageDefinition(FIT_MESSAGE_NAME_METRICS_INFO, FIT_MESSAGE_NUMBER_METRICS_INFO, -1,
            new FitMessageFieldDefinition("timestamp", 253, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("local_timestamp", 0, 4, FitFieldBaseType.UINT32, null)
    );

    public static final FitMessageDefinition DEFINITION_NEURAL_NETWORK_INFO = new FitMessageDefinition(FIT_MESSAGE_NAME_NEURAL_NETWORK_INFO, FIT_MESSAGE_NUMBER_NEURAL_NETWORK_INFO, -1,
            new FitMessageFieldDefinition("timestamp", 253, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("network_version", 0, 1, FitFieldBaseType.UINT8, null),
            new FitMessageFieldDefinition("implicit_message_duration", 1, 2, FitFieldBaseType.UINT16, 1, 0, "s", null),
            new FitMessageFieldDefinition("local_timestamp", 2, 4, FitFieldBaseType.UINT32, null)
    );

    public static final FitMessageDefinition DEFINITION_NEURAL_NETWORK_DATA = new FitMessageDefinition(FIT_MESSAGE_NAME_NEURAL_NETWORK_DATA, FIT_MESSAGE_NUMBER_NEURAL_NETWORK_DATA, -1,
            new FitMessageFieldDefinition("network_data", 0, 20, FitFieldBaseType.BYTE, null)
    );

    public static final FitMessageDefinition DEFINITION_SLEEP_LEVEL = new FitMessageDefinition(FIT_MESSAGE_NAME_SLEEP_LEVEL, FIT_MESSAGE_NUMBER_SLEEP_LEVEL, -1,
            new FitMessageFieldDefinition("timestamp", 253, 4, FitFieldBaseType.UINT32, null),
            new FitMessageFieldDefinition("sleep_level", 0, 1, FitFieldBaseType.ENUM, null)
    );

    public static final FitMessageDefinition DEFINITION_END_OF_FILE = new FitMessageDefinition(FIT_MESSAGE_NAME_END_OF_FILE, FIT_MESSAGE_NUMBER_END_OF_FILE, -1,
            new FitMessageFieldDefinition("timestamp", 253, 4, FitFieldBaseType.UINT32, null)
    );


    public static final List<FitMessageDefinition> ALL_DEFINITIONS = Arrays.asList(
            DEFINITION_FILE_ID,
            DEFINITION_EVENT,
            DEFINITION_DEVICE_INFO,
            DEFINITION_DEBUG,
            DEFINITION_SOFTWARE,
            DEFINITION_FILE_CREATOR,
            DEFINITION_MONITORING,
            DEFINITION_MONITORING_INFO,
            DEFINITION_CONNECTIVITY,
            DEFINITION_WEATHER_CONDITIONS,
            DEFINITION_WEATHER_ALERT,
            DEFINITION_OHR_SETTINGS,
            DEFINITION_MONITORING_HR_DATA,
            DEFINITION_STRESS_LEVEL,
            DEFINITION_MANUAL_STRESS_LEVEL,
            DEFINITION_MAX_MET_DATA,
            DEFINITION_WHR_DIAG,
            DEFINITION_METRICS_INFO,
            DEFINITION_NEURAL_NETWORK_INFO,
            DEFINITION_NEURAL_NETWORK_DATA,
            DEFINITION_SLEEP_LEVEL,
            DEFINITION_END_OF_FILE
    );
}
