package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.protobuf.InvalidProtocolBufferException;
import nodomain.freeyourgadget.gadgetbridge.BuildConfig;
import nodomain.freeyourgadget.gadgetbridge.GBApplication;
import nodomain.freeyourgadget.gadgetbridge.database.DBHandler;
import nodomain.freeyourgadget.gadgetbridge.database.DBHelper;
import nodomain.freeyourgadget.gadgetbridge.deviceevents.GBDeviceEventBatteryInfo;
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
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit.*;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.*;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.protobuf.GdiDeviceStatus;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.protobuf.GdiFindMyWatch;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.protobuf.GdiSmartProto;
import nodomain.freeyourgadget.gadgetbridge.service.serial.GBDeviceProtocol;
import nodomain.freeyourgadget.gadgetbridge.util.DateTimeUtils;
import nodomain.freeyourgadget.gadgetbridge.util.GB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.BinaryUtils.*;

public class VivomoveHrSupport extends AbstractBTLEDeviceSupport {
    private static final Logger LOG = LoggerFactory.getLogger(VivomoveHrSupport.class);

    private BluetoothGattCharacteristic characteristicMessageSender;
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
    private Set<GarminCapability> capabilities;

    private int lastProtobufRequestId;
    private WeatherSpec lastWeatherSpec = defaultWeatherSpec();

    private static WeatherSpec defaultWeatherSpec() {
        final WeatherSpec weatherSpec = new WeatherSpec();
        weatherSpec.timestamp = (int) (System.currentTimeMillis() / 1000);
        weatherSpec.currentConditionCode = 212;
        weatherSpec.currentCondition = Weather.getConditionString(weatherSpec.currentConditionCode);
        weatherSpec.currentHumidity = 76;
        weatherSpec.location = "Test";
        weatherSpec.currentTemp = 27;
        weatherSpec.todayMaxTemp = 29;
        weatherSpec.todayMinTemp = 16;
        weatherSpec.windSpeed = 18;
        weatherSpec.windDirection = 244;
        weatherSpec.forecasts.add(new WeatherSpec.Forecast(17, 28, 905, 65));
        return weatherSpec;
    }

    public VivomoveHrSupport() {
        super(LOG);

        addSupportedService(VivomoveConstants.UUID_SERVICE_GARMIN_1);
        addSupportedService(VivomoveConstants.UUID_SERVICE_GARMIN_2);
    }

    private void dbg(String msg) {
        GB.toast(getContext(), msg, Toast.LENGTH_LONG, GB.INFO);
        LOG.debug(msg);
    }

    private int getNextProtobufRequestId() {
        lastProtobufRequestId = (lastProtobufRequestId + 1) % 65536;
        return lastProtobufRequestId;
    }

    @Override
    protected TransactionBuilder initializeDevice(TransactionBuilder builder) {
        LOG.info("Initializing");

        gbDevice.setState(GBDevice.State.INITIALIZING);
        gbDevice.sendDeviceUpdateIntent(getContext());

        characteristicMessageSender = getCharacteristic(VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_GFDI_SEND);
        characteristicMessageReceiver = getCharacteristic(VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_GFDI_RECEIVE);
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
        } else if (VivomoveConstants.UUID_CHARACTERISTIC_GARMIN_GFDI_RECEIVE.equals(characteristicUUID)) {
            handleReceivedGfdiBytes(data);
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
        LOG.debug("Received {} GFDI bytes", data.length);
        byte[] packet;
        while ((packet = gfdiPacketParser.retrievePacket()) != null) {
            LOG.debug("Processing a {}B GFDI packet", packet.length);
            processGfdiPacket(packet);
        }
    }

    private void processGfdiPacket(byte[] packet) {
        final int size = readShort(packet, 0);
        if (size != packet.length) {
            LOG.error("Received GFDI packet with invalid length: {} vs {}", size, packet.length);
            return;
        }
        final int crc = readShort(packet, packet.length - 2);
        final int correctCrc = ChecksumCalculator.computeCrc(packet, 0, packet.length - 2);
        if (crc != correctCrc) {
            LOG.error("Received GFDI packet with invalid CRC: {} vs {}", crc, correctCrc);
            return;
        }

        final int messageType = readShort(packet, 2);
        switch (messageType) {
            case VivomoveConstants.MESSAGE_RESPONSE:
                processResponseMessage(ResponseMessage.parsePacket(packet), packet);
                break;

            case VivomoveConstants.MESSAGE_DEVICE_INFORMATION:
                processDeviceInformationMessage(DeviceInformationMessage.parsePacket(packet));
                break;

            case VivomoveConstants.MESSAGE_WEATHER_REQUEST:
                processWeatherRequest(WeatherRequestMessage.parsePacket(packet));
                break;

            case VivomoveConstants.MESSAGE_MUSIC_CONTROL_CAPABILITIES:
                processMusicControlCapabilities(MusicControlCapabilitiesMessage.parsePacket(packet));
                break;

            case VivomoveConstants.MESSAGE_CURRENT_TIME_REQUEST:
                processCurrentTimeRequest(CurrentTimeRequestMessage.parsePacket(packet));
                break;

            case VivomoveConstants.MESSAGE_SYNC_REQUEST:
                processSyncRequest(SyncRequestMessage.parsePacket(packet));
                break;

            case VivomoveConstants.MESSAGE_CONFIGURATION:
                processConfigurationMessage(ConfigurationMessage.parsePacket(packet));
                break;

            case VivomoveConstants.MESSAGE_PROTOBUF_RESPONSE:
                processProtobufResponse(ProtobufRequestMessage.parsePacket(packet));
                break;

            default:
                LOG.info("Unknown message type {}: {}", messageType, GB.hexdump(packet, 0, packet.length));
                break;
        }
    }

    private void processSyncRequest(SyncRequestMessage requestMessage) {
        LOG.info("Processing sync request message: option={}, types: {}", requestMessage.option, GB.hexdump(requestMessage.fileTypes, 0, requestMessage.fileTypes.length));
        sendMessage(new GenericResponseMessage(VivomoveConstants.MESSAGE_SYNC_REQUEST, 0).packet);
    }

    private void processProtobufResponse(ProtobufRequestMessage requestMessage) {
        LOG.info("Received protobuf response #{}, {}B@{}/{}: {}", requestMessage.requestId, requestMessage.protobufDataLength, requestMessage.dataOffset, requestMessage.totalProtobufLength, GB.hexdump(requestMessage.messageBytes, 0, requestMessage.messageBytes.length));
        sendMessage(new GenericResponseMessage(VivomoveConstants.MESSAGE_PROTOBUF_RESPONSE, 0).packet);
        final GdiSmartProto.Smart smart;
        try {
            smart = GdiSmartProto.Smart.parseFrom(requestMessage.messageBytes);
        } catch (InvalidProtocolBufferException e) {
            LOG.error("Failed to parse protobuf message ({}): {}", e.getLocalizedMessage(), GB.hexdump(requestMessage.messageBytes, 0, requestMessage.messageBytes.length));
            return;
        }
        boolean processed = false;
        if (smart.hasFindMyWatchService()) {
            processProtobufFindMyWatchResponse(smart.getFindMyWatchService());
            processed = true;
        }
        if (smart.hasDeviceStatusService()) {
            processProtobufDeviceStatusResponse(smart.getDeviceStatusService());
            processed = true;
        }
        if (!processed) {
            LOG.warn("Unknown protobuf response: {}", smart.toString());
        }
    }

    private void processProtobufDeviceStatusResponse(GdiDeviceStatus.DeviceStatusService deviceStatusService) {
        if (deviceStatusService.hasRemoteDeviceBatteryStatusResponse()) {
            final GdiDeviceStatus.DeviceStatusService.RemoteDeviceBatteryStatusResponse batteryStatusResponse = deviceStatusService.getRemoteDeviceBatteryStatusResponse();
            final int batteryLevel = batteryStatusResponse.getCurrentBatteryLevel();
            LOG.info("Received remote battery status {}: level={}", batteryStatusResponse.getStatus(), batteryLevel);
            final GBDeviceEventBatteryInfo batteryEvent = new GBDeviceEventBatteryInfo();
            batteryEvent.level = (short) batteryLevel;
            handleGBDeviceEvent(batteryEvent);
            return;
        }
        if (deviceStatusService.hasActivityStatusResponse()) {
            final GdiDeviceStatus.DeviceStatusService.ActivityStatusResponse activityStatusResponse = deviceStatusService.getActivityStatusResponse();
            LOG.info("Received activity status: {}", activityStatusResponse.getStatus());
        }
        LOG.warn("Unknown DeviceStatusService response: {}", deviceStatusService);
    }

    private void processProtobufFindMyWatchResponse(GdiFindMyWatch.FindMyWatchService findMyWatchService) {
        if (findMyWatchService.hasCancelRequest()) {
            LOG.info("Watch search cancelled, watch found");
            GBApplication.deviceService().onFindDevice(false);
            return;
        }
        if (findMyWatchService.hasCancelResponse() || findMyWatchService.hasFindResponse()) {
            LOG.debug("Received findMyWatch response");
            return;
        }
        LOG.warn("Unknown FindMyWatchService response: {}", findMyWatchService);
    }

    private void processMusicControlCapabilities(MusicControlCapabilitiesMessage capabilitiesMessage) {
        LOG.info("Processing music control capabilities request caps={}", capabilitiesMessage.supportedCapabilities);
        sendMessage(new MusicControlCapabilitiesResponseMessage(0, GarminMusicControlCommand.values()).packet);
    }

    private void processWeatherRequest(WeatherRequestMessage requestMessage) {
        LOG.info("Processing weather request fmt={}, {} hrs, {}/{}", requestMessage.format, requestMessage.hoursOfForecast, requestMessage.latitude, requestMessage.longitude);
        sendMessage(new WeatherRequestResponseMessage(0, 0, 1, 300).packet);
    }

    private void processCurrentTimeRequest(CurrentTimeRequestMessage requestMessage) {
        long now = System.currentTimeMillis();
        final TimeZone timeZone = TimeZone.getDefault();
        final Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTimeInMillis(now);
        int dstOffset = calendar.get(Calendar.DST_OFFSET) / 1000;
        int timeZoneOffset = timeZone.getOffset(now) / 1000;
        int garminTimestamp = GarminTimeUtils.javaMillisToGarminTimestamp(now);

        LOG.info("Processing current time request #{}: time={}, DST={}, ofs={}", requestMessage.referenceID, garminTimestamp, dstOffset, timeZoneOffset);
        sendMessage(new CurrentTimeRequestResponseMessage(0, requestMessage.referenceID, garminTimestamp, timeZoneOffset, dstOffset).packet);
    }

    private void processResponseMessage(ResponseMessage responseMessage, byte[] packet) {
        switch (responseMessage.requestID) {
            case VivomoveConstants.MESSAGE_FIT_DEFINITION:
                processFitDefinitionResponse(FitDefinitionResponseMessage.parsePacket(packet));
                break;
            case VivomoveConstants.MESSAGE_FIT_DATA:
                processFitDataResponse(FitDataResponseMessage.parsePacket(packet));
                break;
            case VivomoveConstants.MESSAGE_PROTOBUF_REQUEST:
                processProtobufRequestResponse(ProtobufRequestResponseMessage.parsePacket(packet));
                break;
            case VivomoveConstants.MESSAGE_DEVICE_SETTINGS:
                processDeviceSettingsResponse(SetDeviceSettingsResponseMessage.parsePacket(packet));
                break;
            case VivomoveConstants.MESSAGE_SYSTEM_EVENT:
                processSystemEventResponse(SystemEventResponseMessage.parsePacket(packet));
                break;
            default:
                LOG.info("Received response to message {}: {}", responseMessage.requestID, responseMessage.getStatusStr());
                break;
        }
    }

    private void processDeviceSettingsResponse(SetDeviceSettingsResponseMessage responseMessage) {
        LOG.info("Received response to device settings message: status={}, response={}", responseMessage.status, responseMessage.response);
    }

    private void processSystemEventResponse(SystemEventResponseMessage responseMessage) {
        LOG.info("Received response to system event message: status={}, response={}", responseMessage.status, responseMessage.response);
    }

    private void processFitDefinitionResponse(FitDefinitionResponseMessage responseMessage) {
        LOG.info("Received response to FIT definition message: status={}, FIT response={}", responseMessage.status, responseMessage.fitResponse);
    }

    private void processFitDataResponse(FitDataResponseMessage responseMessage) {
        LOG.info("Received response to FIT data message: status={}, FIT response={}", responseMessage.status, responseMessage.fitResponse);
    }

    private void processProtobufRequestResponse(ProtobufRequestResponseMessage responseMessage) {
        LOG.info("Received response to protobuf message #{}@{}:  status={}, error={}", responseMessage.requestId, responseMessage.dataOffset, responseMessage.protobufStatus, responseMessage.error);
    }

    private void processDeviceInformationMessage(DeviceInformationMessage deviceInformationMessage) {
        LOG.info("Received device information: protocol {}, product {}, unit {}, SW {}, max packet {}, BT name {}, device name {}, device model {}", deviceInformationMessage.protocolVersion, deviceInformationMessage.productNumber, deviceInformationMessage.unitNumber, deviceInformationMessage.getSoftwareVersionStr(), deviceInformationMessage.maxPacketSize, deviceInformationMessage.bluetoothFriendlyName, deviceInformationMessage.deviceName, deviceInformationMessage.deviceModel);

        final GBDeviceEventVersionInfo deviceEventVersionInfo = new GBDeviceEventVersionInfo();
        deviceEventVersionInfo.fwVersion = deviceInformationMessage.getSoftwareVersionStr();
        handleGBDeviceEvent(deviceEventVersionInfo);

        // prepare and send response
        final boolean protocolVersionSupported = deviceInformationMessage.protocolVersion / 100 == 1;
        if (!protocolVersionSupported) {
            LOG.error("Unsupported protocol version {}", deviceInformationMessage.protocolVersion);
        }
        final int protocolFlags = protocolVersionSupported ? 1 : 0;
        final DeviceInformationResponseMessage deviceInformationResponseMessage = new DeviceInformationResponseMessage(VivomoveConstants.STATUS_ACK, 112, -1, VivomoveConstants.GADGETBRIDGE_UNIT_NUMBER, BuildConfig.VERSION_CODE, 16384, getBluetoothAdapter().getName(), Build.MANUFACTURER, Build.DEVICE, protocolFlags);

        sendMessage(deviceInformationResponseMessage.packet);
    }

    private void processConfigurationMessage(ConfigurationMessage configurationMessage) {
        this.capabilities = GarminCapability.setFromBinary(configurationMessage.configurationPayload);

        LOG.info("Received configuration message; capabilities: {}", GarminCapability.setToString(capabilities));

        // prepare and send response
        sendMessage(new GenericResponseMessage(VivomoveConstants.MESSAGE_CONFIGURATION, VivomoveConstants.STATUS_ACK).packet);

        // and report our own configuration/capabilities
        final byte[] ourCapabilityFlags = GarminCapability.setToBinary(VivomoveConstants.OUR_CAPABILITIES);
        sendMessage(new ConfigurationMessage(ourCapabilityFlags).packet);

        // initialize current time and settings
        sendCurrentTime(null);
        sendSettings(null);

        // and everything is ready now
        sendSyncReady();
        requestBatteryStatusUpdate();
        sendFitDefinitions();
        sendFitConnectivityMessage();
    }

    private void sendMessage(byte[] messageBytes) {
        try {
            final TransactionBuilder builder = performInitialized("sendMessage()");
            sendMessage(builder, messageBytes);
            builder.queue(getQueue());
        } catch (IOException e) {
            LOG.error("Unable to send a message", e);
        }
    }

    private void sendMessage(TransactionBuilder builder, byte[] messageBytes) {
        // ugly, refactor??
        if (builder == null) {
            sendMessage(messageBytes);
            return;
        }

        final byte[] packet = GfdiPacketParser.wrapMessageToPacket(messageBytes);
        int remainingBytes = packet.length;
        if (remainingBytes > VivomoveConstants.MAX_WRITE_SIZE) {
            int position = 0;
            while (remainingBytes > 0) {
                final byte[] fragment = Arrays.copyOfRange(packet, position, position + Math.min(remainingBytes, VivomoveConstants.MAX_WRITE_SIZE));
                builder.write(characteristicMessageSender, fragment);
                position += fragment.length;
                remainingBytes -= fragment.length;
            }
        } else {
            builder.write(characteristicMessageSender, packet);
        }
    }

    private void sendProtobufRequest(byte[] protobufMessage) {
        final int requestId = getNextProtobufRequestId();
        LOG.debug("Sending {}B protobuf request #{}: {}", protobufMessage.length, requestId, GB.hexdump(protobufMessage, 0, protobufMessage.length));
        sendMessage(new ProtobufRequestMessage(requestId, 0, protobufMessage.length, protobufMessage.length, protobufMessage).packet);
    }

    private void requestBatteryStatusUpdate() {
        sendProtobufRequest(
                GdiSmartProto.Smart.newBuilder()
                        .setDeviceStatusService(
                                GdiDeviceStatus.DeviceStatusService.newBuilder()
                                        .setRemoteDeviceBatteryStatusRequest(
                                                GdiDeviceStatus.DeviceStatusService.RemoteDeviceBatteryStatusRequest.newBuilder()
                                        )
                        )
                        .build()
                        .toByteArray());
    }

    private void requestActivityStatus() {
        sendProtobufRequest(
                GdiSmartProto.Smart.newBuilder()
                        .setDeviceStatusService(
                                GdiDeviceStatus.DeviceStatusService.newBuilder()
                                        .setActivityStatusRequest(
                                                GdiDeviceStatus.DeviceStatusService.ActivityStatusRequest.newBuilder()
                                        )
                        )
                        .build()
                        .toByteArray());
    }

    private void sendSyncReady() {
        sendMessage(new SystemEventMessage(GarminSystemEventType.SYNC_READY, 0).packet);
    }

    private void sendCurrentTime(TransactionBuilder builder) {
        final Map<GarminDeviceSetting, Object> settings = new LinkedHashMap<>(3);

        long now = System.currentTimeMillis();
        final TimeZone timeZone = TimeZone.getDefault();
        final Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTimeInMillis(now);
        int dstOffset = calendar.get(Calendar.DST_OFFSET) / 1000;
        int timeZoneOffset = 0; // timeZone.getOffset(now) / 1000;
        int garminTimestamp = GarminTimeUtils.javaMillisToGarminTimestamp(now);

        settings.put(GarminDeviceSetting.CURRENT_TIME, garminTimestamp);
        settings.put(GarminDeviceSetting.DAYLIGHT_SAVINGS_TIME_OFFSET, dstOffset);
        settings.put(GarminDeviceSetting.TIME_ZONE_OFFSET, timeZoneOffset);
        // TODO: NEXT_DAYLIGHT_SAVINGS_START, NEXT_DAYLIGHT_SAVINGS_END
        LOG.info("Setting time to {}, dstOffset={}, tzOffset={} (DST={})", garminTimestamp, dstOffset, timeZoneOffset, timeZone.inDaylightTime(new Date(now)) ? 1 : 0);
        sendMessage(builder, new SetDeviceSettingsMessage(settings).packet);
    }

    private void sendSettings(TransactionBuilder builder) {
        final Map<GarminDeviceSetting, Object> settings = new LinkedHashMap<>(3);

        settings.put(GarminDeviceSetting.WEATHER_CONDITIONS_ENABLED, true);
        settings.put(GarminDeviceSetting.WEATHER_ALERTS_ENABLED, true);
        settings.put(GarminDeviceSetting.AUTO_UPLOAD_ENABLED, true);
        LOG.info("Sending settings");
        sendMessage(builder, new SetDeviceSettingsMessage(settings).packet);
    }

    private void sendFitDefinitions() {
        sendMessage(new FitDefinitionMessage(Arrays.asList(
                FitMessageDefinitions.definitionConnectivity,
                FitMessageDefinitions.definitionWeatherConditions
        )).packet);
    }

    private void sendFitConnectivityMessage() {
        final FitMessage connectivityMessage = new FitMessage(FitMessageDefinitions.definitionConnectivity);
        connectivityMessage.setField(0, FitBool.TRUE);
        connectivityMessage.setField(1, FitBool.TRUE);
        connectivityMessage.setField(2, FitBool.INVALID);
        connectivityMessage.setField(4, FitBool.TRUE);
        connectivityMessage.setField(5, FitBool.TRUE);
        connectivityMessage.setField(6, FitBool.TRUE);
        connectivityMessage.setField(8, FitBool.TRUE);
        connectivityMessage.setField(9, FitBool.TRUE);
        connectivityMessage.setField(10, FitBool.TRUE);
        connectivityMessage.setField(13, FitBool.TRUE);
        sendMessage(new FitDataMessage(Collections.singletonList(connectivityMessage)).packet);
    }

    private void sendWeatherConditions() {
        final WeatherSpec weather = lastWeatherSpec;
        final FitMessage weatherConditionsMessage = new FitMessage(FitMessageDefinitions.definitionWeatherConditions);
        weatherConditionsMessage.setField(253, GarminTimeUtils.unixTimeToGarminTimestamp(weather.timestamp));
        weatherConditionsMessage.setField(0, 0); // 0 = current, 2 = hourly_forecast, 3 = daily_forecast
        weatherConditionsMessage.setField(1, weather.currentTemp);
        weatherConditionsMessage.setField(2, openWeatherCodeToFitWeatherStatus(weather.currentConditionCode));
        weatherConditionsMessage.setField(3, weather.windDirection);
        weatherConditionsMessage.setField(4, Math.round(weather.windSpeed));
        weatherConditionsMessage.setField(7, weather.currentHumidity);
        weatherConditionsMessage.setField(8, weather.location);
        final Calendar timestamp = Calendar.getInstance();
        timestamp.setTimeInMillis(weather.timestamp * 1000L);
        weatherConditionsMessage.setField(12, timestamp.get(Calendar.DAY_OF_WEEK));
        weatherConditionsMessage.setField(13, weather.todayMaxTemp);
        weatherConditionsMessage.setField(14, weather.todayMinTemp);

        sendMessage(new FitDataMessage(Collections.singletonList(weatherConditionsMessage)).packet);
    }

    private int openWeatherCodeToFitWeatherStatus(int openWeatherCode) {
        switch (openWeatherCode) {
            case 800:
                // clear
                return 0;
            case 801:
            case 802:
                // partly_cloudy
                return 1;
            case 803:
                // mostly cloudy
                return 2;
            case 804:
                // cloudy
                return 22;
            case 701:
            case 721:
                // hazy
                return 11;
            case 741:
                // fog
                return 8;
            case 771:
            case 781:
                // windy
                return 5;
            case 615:
                // light_rain_snow
                return 20;
            case 616:
                // heavy_rain_snow
                return 21;
            case 611:
            case 612:
            case 613:
                // wintry_mix
                return 7;
            case 500:
            case 520:
            case 521:
            case 300:
            case 310:
            case 313:
                // light_rain
                return 16;
            case 501:
            case 531:
            case 301:
            case 311:
                // rain
                return 3;
            case 502:
            case 503:
            case 504:
            case 522:
            case 302:
            case 312:
            case 314:
                // heavy_rain
                return 17;
            case 321:
                // scattered_showers
                return 13;
            case 511:
                // unknown_precipitation
                return 15;
            case 200:
            case 201:
            case 202:
            case 210:
            case 211:
            case 212:
            case 230:
            case 231:
            case 232:
                // thunderstorm
                return 6;
            case 221:
                // scattered_thunderstorms
                return 14;
            case 600:
                // light_snow
                return 18;
            case 601:
                // snow
                return 4;
            case 602:
                // heavy_snow
                return 19;
            default:
                throw new IllegalArgumentException("Unknown weather code " + openWeatherCode);

        }
    }

    private void sendBatteryStatus() {
        LOG.info("Sending battery status");
        sendMessage(new BatteryStatusMessage(12).packet);
    }

    @Override
    public boolean useAutoConnect() {
        return false;
    }

    @Override
    public void onNotification(NotificationSpec notificationSpec) {
        dbg("onNotification " + notificationSpec);
    }

    @Override
    public void onDeleteNotification(int id) {
        dbg("onDeleteNotification " + id);
    }

    @Override
    public void onSetTime() {
        dbg("onSetTime()");
        sendCurrentTime(null);
    }

    @Override
    public void onSetAlarms(ArrayList<? extends Alarm> alarms) {

    }

    @Override
    public void onSetCallState(CallSpec callSpec) {
        dbg("onSetCallState " + callSpec);
    }

    @Override
    public void onSetCannedMessages(CannedMessagesSpec cannedMessagesSpec) {
        dbg("onSetCannedMessages");
    }

    @Override
    public void onSetMusicState(MusicStateSpec stateSpec) {
        dbg("onSetMusicState " + stateSpec);
    }

    @Override
    public void onSetMusicInfo(MusicSpec musicSpec) {
        dbg("onSetMusicInfo " + musicSpec);
        sendMessage(new MusicControlEntityUpdateMessage(
                new AmsEntityAttribute(AmsEntity.TRACK, AmsEntityAttribute.TRACK_ATTRIBUTE_ARTIST, 0, musicSpec.artist),
                new AmsEntityAttribute(AmsEntity.TRACK, AmsEntityAttribute.TRACK_ATTRIBUTE_ALBUM, 0, musicSpec.album),
                new AmsEntityAttribute(AmsEntity.TRACK, AmsEntityAttribute.TRACK_ATTRIBUTE_TITLE, 0, musicSpec.track),
                new AmsEntityAttribute(AmsEntity.TRACK, AmsEntityAttribute.TRACK_ATTRIBUTE_DURATION, 0, String.valueOf(musicSpec.duration))
        ).packet);
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
        switch(flags) {
            case GBDeviceProtocol.RESET_FLAGS_FACTORY_RESET:
                LOG.info("Requesting factory reset");
                sendMessage(new SystemEventMessage(GarminSystemEventType.FACTORY_RESET, 1).packet);
                break;

            default:
                GB.toast(getContext(), "This kind of reset not supported for this device", Toast.LENGTH_LONG, GB.ERROR);
                break;
        }
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
        dbg("onFindDevice " + start);
        if (start) {
            sendProtobufRequest(
                    GdiSmartProto.Smart.newBuilder()
                            .setFindMyWatchService(
                                    GdiFindMyWatch.FindMyWatchService.newBuilder()
                                            .setFindRequest(
                                                    GdiFindMyWatch.FindMyWatchService.FindMyWatchRequest.newBuilder()
                                                            .setTimeout(60)
                                            )
                            )
                            .build()
                            .toByteArray());
        } else {
            sendProtobufRequest(
                    GdiSmartProto.Smart.newBuilder()
                            .setFindMyWatchService(
                                    GdiFindMyWatch.FindMyWatchService.newBuilder()
                                            .setCancelRequest(
                                                    GdiFindMyWatch.FindMyWatchService.FindMyWatchCancelRequest.newBuilder()
                                            )
                            )
                            .build()
                            .toByteArray());
        }
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

    @Override
    public void onTestNewFunction() {
        dbg("onTestNewFunction()");
    }

    @Override
    public void onSendWeather(WeatherSpec weatherSpec) {
        dbg("onSendWeather");
        this.lastWeatherSpec = weatherSpec;
        sendWeatherConditions();
    }
}
