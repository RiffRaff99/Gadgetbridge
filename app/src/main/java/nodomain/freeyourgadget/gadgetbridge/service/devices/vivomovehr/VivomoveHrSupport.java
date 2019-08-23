package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import nodomain.freeyourgadget.gadgetbridge.GBApplication;
import nodomain.freeyourgadget.gadgetbridge.database.DBHandler;
import nodomain.freeyourgadget.gadgetbridge.database.DBHelper;
import nodomain.freeyourgadget.gadgetbridge.deviceevents.GBDeviceEventVersionInfo;
import nodomain.freeyourgadget.gadgetbridge.devices.SampleProvider;
import nodomain.freeyourgadget.gadgetbridge.devices.vivomovehr.VivomoveConstants;
import nodomain.freeyourgadget.gadgetbridge.devices.vivomovehr.VivomoveHrSampleProvider;
import nodomain.freeyourgadget.gadgetbridge.entities.DaoSession;
import nodomain.freeyourgadget.gadgetbridge.entities.Device;
import nodomain.freeyourgadget.gadgetbridge.entities.User;
import nodomain.freeyourgadget.gadgetbridge.entities.VivomoveHrActivitySample;
import nodomain.freeyourgadget.gadgetbridge.impl.GBDevice;
import nodomain.freeyourgadget.gadgetbridge.model.*;
import nodomain.freeyourgadget.gadgetbridge.service.btle.AbstractBTLEDeviceSupport;
import nodomain.freeyourgadget.gadgetbridge.service.btle.TransactionBuilder;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.DeviceInformationMessage;
import nodomain.freeyourgadget.gadgetbridge.util.GB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.BinaryUtils.*;

public class VivomoveHrSupport extends AbstractBTLEDeviceSupport {
    private static final Logger LOG = LoggerFactory.getLogger(VivomoveHrSupport.class);

    private BluetoothGattCharacteristic char1_1;
    private BluetoothGattCharacteristic characteristicMessageReceiver;
    private BluetoothGattCharacteristic characteristicHeartRate;
    private BluetoothGattCharacteristic characteristicSteps;
    private BluetoothGattCharacteristic characteristicCalories;
    private BluetoothGattCharacteristic characteristicStairs;
    private BluetoothGattCharacteristic char2_7;
    private BluetoothGattCharacteristic char2_9;

    private Handler handler;

    private final VivomoveHrActivitySample lastSample = new VivomoveHrActivitySample();

    private final GfdiPacketParser gfdiPacketParser = new GfdiPacketParser();

    public VivomoveHrSupport() {
        super(LOG);

        addSupportedService(VivomoveConstants.UUID_SERVICE_GARMIN_1);
        addSupportedService(VivomoveConstants.UUID_SERVICE_GARMIN_2);
    }

    private void dbg(String msg) {
        GB.toast(getContext(), msg, Toast.LENGTH_LONG, GB.INFO);
        LOG.debug(msg);
    }

    @Override
    protected TransactionBuilder initializeDevice(TransactionBuilder builder) {
        LOG.info("Initializing");

        gbDevice.setState(GBDevice.State.INITIALIZING);
        gbDevice.sendDeviceUpdateIntent(getContext());

        char1_1 = getCharacteristic(VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_1_1);
        characteristicMessageReceiver = getCharacteristic(VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_MESSAGE_RECEIVE);
        characteristicHeartRate = getCharacteristic(VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_HEART_RATE);
        characteristicSteps = getCharacteristic(VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_STEPS);
        characteristicCalories = getCharacteristic(VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_CALORIES);
        characteristicStairs = getCharacteristic(VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_STAIRS);
        char2_7 = getCharacteristic(VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_HEART_RATE_VARIATION);
        char2_9 = getCharacteristic(VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_2_9);

        builder.setGattCallback(this);
        builder.notify(characteristicMessageReceiver, true);
//        builder.notify(characteristicHeartRate, true);
//        builder.notify(characteristicSteps, true);
//        builder.notify(characteristicCalories, true);
//        builder.notify(characteristicStairs, true);
        //builder.notify(char2_7, true);
        // builder.notify(char2_9, true);

        gbDevice.setState(GBDevice.State.INITIALIZED);
        gbDevice.sendDeviceUpdateIntent(getContext());

        final Looper mainLooper = getContext().getMainLooper();
        handler = new Handler(mainLooper);

        LOG.info("Initialization Done");

        return builder;
    }

    @Override
    public boolean onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        if (super.onCharacteristicChanged(gatt, characteristic)) {
            return true;
        }

        final UUID characteristicUUID = characteristic.getUuid();
        final byte[] data = characteristic.getValue();
        if (data.length == 0) {
            return true;
        }

        //dbg(String.format("Characteristic %s changed: %s", characteristicUUID.toString(), Arrays.toString(data)));

        if (super.onCharacteristicChanged(gatt, characteristic)) {
            return true;
        }

        if (VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_HEART_RATE.equals(characteristicUUID)) {
            processRealtimeHeartRate(data);
        } else if (VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_STEPS.equals(characteristicUUID)) {
            processRealtimeSteps(data);
        } else if (VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_CALORIES.equals(characteristicUUID)) {
            processRealtimeCalories(data);
        } else if (VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_STAIRS.equals(characteristicUUID)) {
            processRealtimeStairs(data);
        } else if (VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_INTENSITY.equals(characteristicUUID)) {
            processRealtimeIntensityMinutes(data);
        } else if (VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_HEART_RATE_VARIATION.equals(characteristicUUID)) {
            handleRealtimeHeartbeat(data);
        } else if (VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_MESSAGE_RECEIVE.equals(characteristicUUID)) {
            handleReceivedGfdiBytes(data);
        } else if (VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_2_9.equals(characteristicUUID)) {
            try {
                stream2_9.write(data);
            } catch (IOException e) {
                LOG.error("Failed to write data to stream", e);
            }
        } else {
            LOG.debug("Unknown characteristic {} changed: {}", characteristicUUID, Arrays.toString(data));
        }

        return true;
    }

    private void processRealtimeHeartRate(byte[] data) {
        int unknown1 = readByte(data, 0);
        int heartRate = readByte(data, 1);
        int unknown2 = readByte(data, 2);
        int unknown3 = readShort(data, 3);

        lastSample.setHeartRate(heartRate);
        processSample();

        LOG.debug("Realtime HR {} ({}, {}, {})", heartRate, unknown1, unknown2, unknown3);
    }

    private void processRealtimeSteps(byte[] data) {
        int steps = readInt(data, 0);
        int goal = readInt(data, 4);

        lastSample.setSteps(steps);
        processSample();

        LOG.debug("Realtime steps: {} steps (goal: {})", steps, goal);
    }

    private void processRealtimeCalories(byte[] data) {
        int calories = readInt(data, 0);
        int unknown = readInt(data, 4);

        lastSample.setCaloriesBurnt(calories);
        processSample();

        LOG.debug("Realtime calories: {} cal burned (unknown: {})", calories, unknown);
    }

    private void processRealtimeStairs(byte[] data) {
        int floorsClimbed = readShort(data, 0);
        int unknown = readShort(data, 2);
        int floorGoal = readShort(data, 4);

        lastSample.setFloorsClimbed(floorsClimbed);
        processSample();

        LOG.debug("Realtime stairs: {} floors climbed (goal: {}, unknown: {})", floorsClimbed, floorGoal, unknown);
    }

    private void processSample() {
        if (lastSample.getCaloriesBurnt() == null || lastSample.getFloorsClimbed() == null || lastSample.getHeartRate() == 0 || lastSample.getSteps() == 0) {
            LOG.debug("Skipping incomplete sample");
            return;
        }

        try (DBHandler dbHandler = GBApplication.acquireDB()) {
            final DaoSession session = dbHandler.getDaoSession();

            final GBDevice gbDevice = getDevice();
            final Device device = DBHelper.getDevice(gbDevice, session);
            final User user = DBHelper.getUser(session);
            final int ts = (int) (System.currentTimeMillis() / 1000);
            final VivomoveHrSampleProvider provider = new VivomoveHrSampleProvider(gbDevice, session);
            final VivomoveHrActivitySample sample = createActivitySample(device, user, ts, provider);

            sample.setCaloriesBurnt(lastSample.getCaloriesBurnt());
            sample.setFloorsClimbed(lastSample.getFloorsClimbed());
            sample.setHeartRate(lastSample.getHeartRate());
            sample.setSteps(lastSample.getSteps());
            sample.setRawIntensity(ActivitySample.NOT_MEASURED);
            sample.setRawKind(ActivityKind.TYPE_ACTIVITY); // to make it visible in the charts TODO: add a MANUAL kind for that?

            LOG.debug("Publishing sample");
            provider.addGBActivitySample(sample);
        } catch (Exception e) {
            GB.toast(getContext(), "Error saving real-time activity data: " + e.getLocalizedMessage(), Toast.LENGTH_LONG, GB.ERROR);
            LOG.error("Error saving real-time activity data", e);
        }

        final Intent intent = new Intent(DeviceService.ACTION_REALTIME_SAMPLES)
                .putExtra(DeviceService.EXTRA_REALTIME_SAMPLE, lastSample);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    private void processRealtimeIntensityMinutes(byte[] data) {
        int weeklyLimit = readInt(data, 10);

        LOG.debug("Realtime intensity recorded; weekly limit: {}", weeklyLimit);
    }

    private void handleRealtimeHeartbeat(byte[] data) {
        int interval = readShort(data, 0);
        int timer = readInt(data, 2);

        float heartRate = (60.0f * 1024.0f) / interval;
        LOG.debug("Realtime heartbeat frequency {} at {}", heartRate, timer);
    }

    public VivomoveHrActivitySample createActivitySample(Device device, User user, int timestampInSeconds, SampleProvider provider) {
        final VivomoveHrActivitySample sample = new VivomoveHrActivitySample();
        sample.setDevice(device);
        sample.setUser(user);
        sample.setTimestamp(timestampInSeconds);
        sample.setProvider(provider);

        return sample;
    }

    private void handleReceivedGfdiBytes(byte[] data) {
        gfdiPacketParser.receivedBytes(data);
        byte[] packet;
        while ((packet = gfdiPacketParser.retrievePacket()) != null) {
            processGfdiPacket(packet);
        }
    }

    private void processGfdiPacket(byte[] packet) {
        final int size = readShort(packet, 0);
        if (size != packet.length) {
            LOG.error("Received GFDI packet with invalid length: {} vs {}", size, packet.length);
            return;
        }

        final int messageType = readShort(packet, 2);
        switch (messageType) {
            case VivomoveConstants.MESSAGE_DEVICE_INFORMATION:
                processDeviceInformationMessage(packet);
                break;

            default:
                LOG.info("Unknown message type {}", messageType);
                break;
        }
    }

    private void processDeviceInformationMessage(byte[] packet) {
        final DeviceInformationMessage deviceInformationMessage = DeviceInformationMessage.parsePacket(packet);

        LOG.info("Received device information: protocol {}, product {}, unit {}, SW {}, max packet {}, BT name {}, device name {}, device model {}", deviceInformationMessage.protocolVersion, deviceInformationMessage.productNumber, deviceInformationMessage.unitNumber, deviceInformationMessage.getSoftwareVersionStr(), deviceInformationMessage.maxPacketSize, deviceInformationMessage.bluetoothFriendlyName, deviceInformationMessage.deviceName, deviceInformationMessage.deviceModel);

        final GBDeviceEventVersionInfo deviceEventVersionInfo = new GBDeviceEventVersionInfo();
        deviceEventVersionInfo.fwVersion = deviceInformationMessage.getSoftwareVersionStr();
        handleGBDeviceEvent(deviceEventVersionInfo);
    }

    @Override
    public boolean useAutoConnect() {
        return false;
    }

    @Override
    public void onNotification(NotificationSpec notificationSpec) {
    }

    @Override
    public void onDeleteNotification(int id) {

    }

    @Override
    public void onSetTime() {
        dbg("onSetTime()");
    }

    @Override
    public void onSetAlarms(ArrayList<? extends Alarm> alarms) {

    }

    @Override
    public void onSetCallState(CallSpec callSpec) {

    }

    @Override
    public void onSetCannedMessages(CannedMessagesSpec cannedMessagesSpec) {

    }

    @Override
    public void onSetMusicState(MusicStateSpec stateSpec) {

    }

    @Override
    public void onSetMusicInfo(MusicSpec musicSpec) {

    }

    @Override
    public void onEnableRealtimeSteps(boolean enable) {
        try {
            performInitialized((enable ? "Enable" : "Disable") + " realtime steps").notify(characteristicSteps, enable).queue(getQueue());
        } catch (IOException e) {
            LOG.error("Unable to change realtime steps notification to: " + enable, e);
        }
    }

    @Override
    public void onInstallApp(Uri uri) {

    }

    @Override
    public void onAppInfoReq() {

    }

    @Override
    public void onAppStart(UUID uuid, boolean start) {

    }

    @Override
    public void onAppDelete(UUID uuid) {

    }

    @Override
    public void onAppConfiguration(UUID appUuid, String config, Integer id) {

    }

    @Override
    public void onAppReorder(UUID[] uuids) {

    }

    @Override
    public void onFetchRecordedData(int dataTypes) {

    }

    @Override
    public void onReset(int flags) {

    }

    @Override
    public void onHeartRateTest() {
        dbg("onHeartRateTest()");
    }

    @Override
    public void onEnableRealtimeHeartRateMeasurement(boolean enable) {
        try {
            performInitialized((enable ? "Enable" : "Disable") + " realtime heartrate").notify(characteristicHeartRate, enable).queue(getQueue());
        } catch (IOException ex) {
            LOG.error("Unable to change realtime steps notification to: " + enable, ex);
        }
    }

    @Override
    public void onFindDevice(boolean start) {

    }

    @Override
    public void onSetConstantVibration(int integer) {

    }

    @Override
    public void onScreenshotReq() {

    }

    @Override
    public void onEnableHeartRateSleepSupport(boolean enable) {

    }

    @Override
    public void onSetHeartRateMeasurementInterval(int seconds) {
        dbg("onSetHeartRateMeasurementInterval(" + seconds + ")");
    }

    @Override
    public void onAddCalendarEvent(CalendarEventSpec calendarEventSpec) {

    }

    @Override
    public void onDeleteCalendarEvent(byte type, long id) {

    }

    @Override
    public void onSendConfiguration(String config) {

    }

    @Override
    public void onReadConfiguration(String config) {

    }

    private OutputStream stream2_9;

    private Runnable stopStreaming = new Runnable() {
        @Override
        public void run() {
            try {
                performInitialized("Write stop request").write(char2_9, new byte[]{0}).notify(char2_9, false).queue(getQueue());
                stream2_9.close();
                dbg("Streaming stopped");
            } catch (IOException ex) {
                dbg("I/O error stopping: " + ex.getLocalizedMessage());
            }
        }
    };

    @Override
    public void onTestNewFunction() {
        dbg("onTestNewFunction()");
        getContext().deleteFile("data2_9.bin");
        /*
        try {
            stream2_9 = getContext().openFileOutput("data2_9.bin", 0);
            performInitialized("Write start request").write(char2_9, new byte[]{1}).notify(char2_9, true).queue(getQueue());
            handler.postDelayed(stopStreaming, 20000);
        } catch (IOException ex) {
            dbg("I/O error starting: " + ex.getLocalizedMessage());
        }
         */
    }

    @Override
    public void onSendWeather(WeatherSpec weatherSpec) {

    }
}
