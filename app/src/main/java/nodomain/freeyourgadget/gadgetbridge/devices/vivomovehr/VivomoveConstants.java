package nodomain.freeyourgadget.gadgetbridge.devices.vivomovehr;

import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.GarminCapability;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VivomoveConstants {
    /**
     * @see <a href="https://www.bluetooth.com/specifications/gatt/services/">BLE GATT Services</a>
     */
    public static final UUID UUID_CHARACTERISTIC_GENERIC_ACCESS = UUID.fromString("00001800-0000-1000-8000-008055f9b34fb");
    public static final UUID UUID_CHARACTERISTIC_MEASURE = UUID.fromString("000033f2-0000-1000-8000-00805f9b34fb");

    public static final UUID UUID_SERVICE_GARMIN_1 = UUID.fromString("6A4E2401-667B-11E3-949A-0800200C9A66");
    public static final UUID UUID_SERVICE_GARMIN_2 = UUID.fromString("6A4E2500-667B-11E3-949A-0800200C9A66");

    public static final UUID UUID_CHARACTERISTIC_GARMIN_GFDI_SEND = UUID.fromString("6a4e4c80-667b-11e3-949a-0800200c9a66");
    public static final UUID UUID_CHARACTERISTIC_GARMIN_GFDI_RECEIVE = UUID.fromString("6a4ecd28-667b-11e3-949a-0800200c9a66");

    public static final UUID UUID_CHARACTERISTIC_GARMIN_HEART_RATE = UUID.fromString("6a4e2501-667b-11e3-949a-0800200c9a66");
    public static final UUID UUID_CHARACTERISTIC_GARMIN_STEPS = UUID.fromString("6a4e2502-667b-11e3-949a-0800200c9a66");
    public static final UUID UUID_CHARACTERISTIC_GARMIN_CALORIES = UUID.fromString("6a4e2503-667b-11e3-949a-0800200c9a66");
    public static final UUID UUID_CHARACTERISTIC_GARMIN_STAIRS = UUID.fromString("6a4e2504-667b-11e3-949a-0800200c9a66");
    public static final UUID UUID_CHARACTERISTIC_GARMIN_INTENSITY = UUID.fromString("6a4e2505-667b-11e3-949a-0800200c9a66");
    public static final UUID UUID_CHARACTERISTIC_GARMIN_HEART_RATE_VARIATION = UUID.fromString("6a4e2507-667b-11e3-949a-0800200c9a66");
    // public static final UUID UUID_CHARACTERISTIC_GARMIN_STRESS = UUID.fromString("6a4e2508-667b-11e3-949a-0800200c9a66");
    public static final UUID UUID_CHARACTERISTIC_GARMIN_2_9 = UUID.fromString("6a4e2509-667b-11e3-949a-0800200c9a66");
    // public static final UUID UUID_CHARACTERISTIC_GARMIN_SPO2 = UUID.fromString("6a4e250c-667b-11e3-949a-0800200c9a66");
    // public static final UUID UUID_CHARACTERISTIC_GARMIN_RESPIRATION = UUID.fromString("6a4e250e-667b-11e3-949a-0800200c9a66");

    // public static final UUID UUID_CLIENT_CHARACTERISTIC_CONFIGURATION = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    public static final int STATUS_ACK = 0;
    public static final int STATUS_NAK = 1;
    public static final int STATUS_UNSUPPORTED = 2;
    public static final int STATUS_DECODE_ERROR = 3;
    public static final int STATUS_CRC_ERROR = 4;
    public static final int STATUS_LENGTH_ERROR = 5;

    public static final int GADGETBRIDGE_UNIT_NUMBER = 22222;

    // TODO: Better capability management/configuration
    public static final Set<GarminCapability> OUR_CAPABILITIES = new HashSet<>(Arrays.asList(GarminCapability.SYNC, GarminCapability.ADVANCED_MUSIC_CONTROLS, GarminCapability.FIND_MY_PHONE, GarminCapability.WEATHER_CONDITIONS, GarminCapability.WEATHER_ALERTS, GarminCapability.DEVICE_MESSAGES, GarminCapability.SMS_NOTIFICATIONS, GarminCapability.SYNC, GarminCapability.DEVICE_INITIATES_SYNC, GarminCapability.HOST_INITIATED_SYNC_REQUESTS, GarminCapability.CALENDAR, GarminCapability.CURRENT_TIME_REQUEST_SUPPORT));
    //public static final Set<GarminCapability> OUR_CAPABILITIES = GarminCapability.ALL_CAPABILITIES;

    public static final int MAX_WRITE_SIZE = 20;

    /**
     * Garmin zero time in seconds since Epoch: 1989-12-31T00:00:00Z
     */
    public static final int GARMIN_TIME_EPOCH = 631065600;

    public static final int MESSAGE_RESPONSE = 5000;
    public static final int MESSAGE_WEATHER_REQUEST = 5014;
    public static final int MESSAGE_BATTERY_STATUS = 5023;
    public static final int MESSAGE_DEVICE_INFORMATION = 5024;
    public static final int MESSAGE_DEVICE_SETTINGS = 5026;
    public static final int MESSAGE_SYSTEM_EVENT = 5030;
    public static final int MESSAGE_SYNC_REQUEST = 5037;
    public static final int MESSAGE_MUSIC_CONTROL_CAPABILITIES = 5042;
    public static final int MESSAGE_PROTOBUF_REQUEST = 5043;
    public static final int MESSAGE_PROTOBUF_RESPONSE = 5044;
    public static final int MESSAGE_CONFIGURATION = 5050;
    public static final int MESSAGE_CURRENT_TIME_REQUEST = 5052;
}
