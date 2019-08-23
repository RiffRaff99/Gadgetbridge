package nodomain.freeyourgadget.gadgetbridge.devices.vivomovehr;

import java.util.UUID;

public class VivomoveConstants {
    /**
     * @see <a href="https://www.bluetooth.com/specifications/gatt/services/">BLE GATT Services</a>
     */
    public static final UUID UUID_CHARACTERISTIC_GENERIC_ACCESS = UUID.fromString("00001800-0000-1000-8000-008055f9b34fb");
    public static final UUID UUID_CHARACTERISTIC_MEASURE = UUID.fromString("000033f2-0000-1000-8000-00805f9b34fb");

    public static final UUID UUID_SERVICE_GARMIN_1 = UUID.fromString("6A4E2401-667B-11E3-949A-0800200C9A66");
    public static final UUID UUID_SERVICE_GARMIN_2 = UUID.fromString("6A4E2500-667B-11E3-949A-0800200C9A66");

    public static final UUID UUID_CHARACTERISTIC_GARMIN_1_1 = UUID.fromString("6a4e4c80-667b-11e3-949a-0800200c9a66");
    public static final UUID UUID_CHARACTERISTIC_GARMIN_MESSAGE_RECEIVE = UUID.fromString("6a4ecd28-667b-11e3-949a-0800200c9a66");

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

    public static final int MESSAGE_DEVICE_INFORMATION = 0x13A0;
}
