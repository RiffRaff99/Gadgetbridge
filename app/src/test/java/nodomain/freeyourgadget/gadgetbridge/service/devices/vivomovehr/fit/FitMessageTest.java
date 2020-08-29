package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit;

import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.FitDataMessage;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FitMessageTest {
    @Test
    public void testFitMessage() throws IOException {
        final FitMessage deviceSettingsMessage = new FitMessage(FitMessageDefinitions.DEFINITION_DEVICE_SETTINGS);
        deviceSettingsMessage.setField("bluetooth_connection_alerts_enabled", 0);
        deviceSettingsMessage.setField("auto_lock_enabled", 0);
        deviceSettingsMessage.setField("activity_tracker_enabled", 1);
        deviceSettingsMessage.setField("alarm_time", 0);
        deviceSettingsMessage.setField("ble_auto_upload_enabled", 1);
        deviceSettingsMessage.setField("autosync_min_steps", 1000);
        deviceSettingsMessage.setField("vibration_intensity", 2);
        deviceSettingsMessage.setField("screen_timeout", 0);
        deviceSettingsMessage.setField("mounting_side", 1);
        deviceSettingsMessage.setField("phone_notification_activity_filter", 0);
        deviceSettingsMessage.setField("auto_goal_enabled", 1);
        deviceSettingsMessage.setField("autosync_min_time", 60);
        deviceSettingsMessage.setField("glance_mode_layout", 0);
        deviceSettingsMessage.setField("time_offset", 7200);
        deviceSettingsMessage.setField("phone_notification_default_filter", 0);
        deviceSettingsMessage.setField("alarm_mode", -1);
        deviceSettingsMessage.setField("backlight_timeout", 5);
        deviceSettingsMessage.setField("sedentary_hr_alert_threshold", 100);
        deviceSettingsMessage.setField("backlight_brightness", 0);
        deviceSettingsMessage.setField("time_zone", 254);
        deviceSettingsMessage.setField("sedentary_hr_alert_state", 0);
        deviceSettingsMessage.setField("auto_activity_start_enabled", 0);
        deviceSettingsMessage.setField("alarm_days", 0);
        deviceSettingsMessage.setField("default_page", 1);
        deviceSettingsMessage.setField("message_tones_enabled", 2);
        deviceSettingsMessage.setField("key_tones_enabled", 2);
        deviceSettingsMessage.setField("date_mode", 0);
        deviceSettingsMessage.setField("backlight_gesture", 1);
        deviceSettingsMessage.setField("backlight_mode", 3);
        deviceSettingsMessage.setField("move_alert_enabled", 1);
        deviceSettingsMessage.setField("sleep_do_not_disturb_enabled", 0);
        deviceSettingsMessage.setField("display_orientation", 2);
        deviceSettingsMessage.setField("time_mode", 1);
        deviceSettingsMessage.setField("pages_enabled", 127);
        deviceSettingsMessage.setField("smart_notification_display_orientation", 0);
        deviceSettingsMessage.setField("display_steps_goal_enabled", 1);
        Files.write(Paths.get("c:\\temp\\garmin-settings-test.fit"), new FitDataMessage(deviceSettingsMessage).packet);
        System.out.println(deviceSettingsMessage);

        // assertArrayEquals(new byte[100], new FitDataMessage(deviceSettingsMessage).packet);
    }
}