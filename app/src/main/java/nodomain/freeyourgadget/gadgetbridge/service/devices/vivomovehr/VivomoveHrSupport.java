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
import nodomain.freeyourgadget.gadgetbridge.model.ActivityKind;
import nodomain.freeyourgadget.gadgetbridge.model.ActivitySample;
import nodomain.freeyourgadget.gadgetbridge.model.Alarm;
import nodomain.freeyourgadget.gadgetbridge.model.CalendarEventSpec;
import nodomain.freeyourgadget.gadgetbridge.model.CallSpec;
import nodomain.freeyourgadget.gadgetbridge.model.CannedMessagesSpec;
import nodomain.freeyourgadget.gadgetbridge.model.DeviceService;
import nodomain.freeyourgadget.gadgetbridge.model.MusicSpec;
import nodomain.freeyourgadget.gadgetbridge.model.MusicStateSpec;
import nodomain.freeyourgadget.gadgetbridge.model.NotificationSpec;
import nodomain.freeyourgadget.gadgetbridge.model.Weather;
import nodomain.freeyourgadget.gadgetbridge.model.WeatherSpec;
import nodomain.freeyourgadget.gadgetbridge.service.btle.AbstractBTLEDeviceSupport;
import nodomain.freeyourgadget.gadgetbridge.service.btle.TransactionBuilder;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.ancs.AncsAttribute;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.ancs.AncsAttributeRequest;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.ancs.AncsCategory;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.ancs.AncsEvent;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.ancs.AncsEventFlag;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.ancs.AncsGetNotificationAttributeCommand;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.ancs.AncsGetNotificationAttributesResponse;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.ancs.GncsDataSourceQueue;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.downloads.DirectoryData;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.downloads.DirectoryEntry;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.downloads.FileDownloadListener;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.downloads.FileDownloadQueue;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit.FitBool;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit.FitDbImporter;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit.FitMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit.FitMessageDefinitions;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit.FitParser;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.fit.FitWeatherConditions;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.BatteryStatusMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.ConfigurationMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.CurrentTimeRequestMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.CurrentTimeRequestResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.DeviceInformationMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.DeviceInformationResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.DirectoryFileFilterRequestMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.DirectoryFileFilterResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.DownloadRequestMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.DownloadRequestResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.FileTransferDataMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.FindMyPhoneRequestMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.FitDataMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.FitDataResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.FitDefinitionMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.FitDefinitionResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.GenericResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.GncsControlPointMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.GncsControlPointResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.GncsDataSourceResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.GncsNotificationSourceMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.MusicControlCapabilitiesMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.MusicControlCapabilitiesResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.MusicControlEntityUpdateMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.NotificationServiceSubscriptionMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.NotificationServiceSubscriptionResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.ProtobufRequestMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.ProtobufRequestResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.ResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.SetDeviceSettingsMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.SetDeviceSettingsResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.SupportedFileTypesRequestMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.SupportedFileTypesResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.SyncRequestMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.SystemEventMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.SystemEventResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.WeatherRequestMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.WeatherRequestResponseMessage;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.protobuf.GdiCore;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.protobuf.GdiDeviceStatus;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.protobuf.GdiFindMyWatch;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.protobuf.GdiSmartProto;
import nodomain.freeyourgadget.gadgetbridge.service.serial.GBDeviceProtocol;
import nodomain.freeyourgadget.gadgetbridge.util.FileUtils;
import nodomain.freeyourgadget.gadgetbridge.util.GB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import static nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.BinaryUtils.readByte;
import static nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.BinaryUtils.readInt;
import static nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.BinaryUtils.readShort;

public class VivomoveHrSupport extends AbstractBTLEDeviceSupport implements FileDownloadListener {
    private static final Logger LOG = LoggerFactory.getLogger(VivomoveHrSupport.class);

    private Handler handler;

    private final VivomoveHrActivitySample lastSample = new VivomoveHrActivitySample();

    private final GfdiPacketParser gfdiPacketParser = new GfdiPacketParser();
    private Set<GarminCapability> capabilities;

    private int lastProtobufRequestId;
    private int lastNotificationId;
    private int maxPacketSize;
    private WeatherSpec lastWeatherSpec = defaultWeatherSpec();

    private final FitParser fitParser = new FitParser(FitMessageDefinitions.ALL_DEFINITIONS);
    private VivomoveHrCommunicator communicator;
    private GncsDataSourceQueue gncsDataSourceQueue;
    private FileDownloadQueue fileDownloadQueue;
    private FitDbImporter fitImporter;
    private boolean notificationSubscription;

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

    private int assignNotificationId() {
        return lastNotificationId++;
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

        communicator = new VivomoveHrCommunicator(this);

        builder.setGattCallback(this);
        communicator.start(builder);
        fileDownloadQueue = new FileDownloadQueue(communicator, this);

        gbDevice.setState(GBDevice.State.INITIALIZED);
        gbDevice.sendDeviceUpdateIntent(getContext());

        final Looper mainLooper = getContext().getMainLooper();
        handler = new Handler(mainLooper);

        LOG.info("Initialization Done");

        return builder;
    }

    @Override
    public boolean onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        final UUID characteristicUUID = characteristic.getUuid();
        if (super.onCharacteristicChanged(gatt, characteristic)) {
            LOG.debug("Change of characteristic {} handled by parent", characteristicUUID);
            return true;
        }

        final byte[] data = characteristic.getValue();
        if (data.length == 0) {
            LOG.debug("No data received on change of characteristic {}", characteristicUUID);
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

    private void sendMessage(byte[] messageBytes) {
        communicator.sendMessage(messageBytes);
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

            case VivomoveConstants.MESSAGE_FILE_TRANSFER_DATA:
                fileDownloadQueue.onFileTransferData(FileTransferDataMessage.parsePacket(packet));
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

            case VivomoveConstants.MESSAGE_FIND_MY_PHONE:
                processFindMyPhoneRequest(FindMyPhoneRequestMessage.parsePacket(packet));
                break;

            case VivomoveConstants.MESSAGE_CANCEL_FIND_MY_PHONE:
                processCancelFindMyPhoneRequest();
                break;

            case VivomoveConstants.MESSAGE_NOTIFICATION_SERVICE_SUBSCRIPTION:
                processNotificationServiceSubscription(NotificationServiceSubscriptionMessage.parsePacket(packet));
                break;

            case VivomoveConstants.MESSAGE_GNCS_CONTROL_POINT_REQUEST:
                processGncsControlPointRequest(GncsControlPointMessage.parsePacket(packet));
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

    private void processCancelFindMyPhoneRequest() {
        LOG.info("Processing request to cancel find-my-phone");
        sendMessage(new GenericResponseMessage(VivomoveConstants.MESSAGE_FIND_MY_PHONE, 0).packet);
        GB.closeFindMyPhoneNotification(getContext());
    }

    private void processFindMyPhoneRequest(FindMyPhoneRequestMessage requestMessage) {
        // TODO: use the new GBDeviceEventFindPhone
        LOG.info("Processing find-my-phone request ({} s)", requestMessage.duration);
        sendMessage(new GenericResponseMessage(VivomoveConstants.MESSAGE_FIND_MY_PHONE, 0).packet);

        GB.startFindMyPhoneNotification(null, null, getContext());
    }

    private void processGncsControlPointRequest(GncsControlPointMessage requestMessage) {
        if (requestMessage == null) {
            // TODO: Proper error handling with specific error code
            sendMessage(new GncsControlPointResponseMessage(VivomoveConstants.STATUS_ACK, GncsControlPointResponseMessage.RESPONSE_ANCS_ERROR_OCCURRED, GncsControlPointResponseMessage.ANCS_ERROR_UNKNOWN_ANCS_COMMAND).packet);
            return;
        }
        switch (requestMessage.command.command) {
            case GET_NOTIFICATION_ATTRIBUTES:
                final AncsGetNotificationAttributeCommand getNotificationAttributeCommand = (AncsGetNotificationAttributeCommand) requestMessage.command;
                LOG.info("Processing ANCS request to get attributes of notification #{}", getNotificationAttributeCommand.notificationUID);
                sendMessage(new GncsControlPointResponseMessage(VivomoveConstants.STATUS_ACK, GncsControlPointResponseMessage.RESPONSE_SUCCESSFUL, GncsControlPointResponseMessage.ANCS_ERROR_NO_ERROR).packet);
                final Map<AncsAttribute, String> attributes = new LinkedHashMap<>();
                for (AncsAttributeRequest attributeRequest : getNotificationAttributeCommand.attributes) {
                    final AncsAttribute attribute = attributeRequest.attribute;
                    final String attributeValue;
                    LOG.debug("Requested ANCS attribute {}", attribute);
                    switch (attribute) {
                        case DATE:
                            final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd'T'HHmmSS", Locale.ROOT);
                            //fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
                            attributeValue = fmt.format(new Date(System.currentTimeMillis() - 1500));
                            break;
                        case TITLE:
                            attributeValue = "zkouska";
                            break;
                        case SUBTITLE:
                            attributeValue = "zkouska!";
                            break;
                        case APP_IDENTIFIER:
                            attributeValue = BuildConfig.APPLICATION_ID;
                            break;
                        case MESSAGE:
                            attributeValue = "Zkousime ANCS";
                            break;
                        case MESSAGE_SIZE:
                            attributeValue = "13";
                            break;
                        case POSITIVE_ACTION_LABEL:
                            attributeValue = "Ano";
                            break;
                        case NEGATIVE_ACTION_LABEL:
                            attributeValue = "Ne";
                            break;
                        case PHONE_NUMBER:
                            attributeValue = "+420555123456";
                            break;
                        default:
                            LOG.warn("Unknown attribute {}", attribute);
                            attributeValue = "";
                            break;
                    }
                    final String valueShortened = attributeRequest.maxLength > 0 && attributeValue.length() > attributeRequest.maxLength ? attributeValue.substring(0, attributeRequest.maxLength) : attributeValue;
                    attributes.put(attribute, valueShortened);
                }
                gncsDataSourceQueue.addToQueue(new AncsGetNotificationAttributesResponse(getNotificationAttributeCommand.notificationUID, attributes).packet);
                break;

            default:
                LOG.error("Unknown GNCS control point command {}", requestMessage.command.command);
                sendMessage(new GncsControlPointResponseMessage(VivomoveConstants.STATUS_ACK, GncsControlPointResponseMessage.RESPONSE_ANCS_ERROR_OCCURRED, GncsControlPointResponseMessage.ANCS_ERROR_UNKNOWN_ANCS_COMMAND).packet);
                break;
        }
    }

    private void processNotificationServiceSubscription(NotificationServiceSubscriptionMessage requestMessage) {
        LOG.info("Processing notification service subscription request message: intent={}, flags={}", requestMessage.intentIndicator, requestMessage.featureFlags);
        notificationSubscription = requestMessage.intentIndicator == NotificationServiceSubscriptionMessage.INTENT_SUBSCRIBE;
        sendMessage(new NotificationServiceSubscriptionResponseMessage(0, 0, requestMessage.intentIndicator, requestMessage.featureFlags).packet);
    }

    private void processSyncRequest(SyncRequestMessage requestMessage) {
        final StringBuilder requestedTypes = new StringBuilder();
        for (GarminMessageType type : requestMessage.fileTypes) {
            if (requestedTypes.length() > 0) requestedTypes.append(", ");
            requestedTypes.append(type);
        }
        LOG.info("Processing sync request message: option={}, types: {}", requestMessage.option, requestedTypes);
        sendMessage(new GenericResponseMessage(VivomoveConstants.MESSAGE_SYNC_REQUEST, 0).packet);
        if (requestMessage.option != SyncRequestMessage.OPTION_INVISIBLE) {
            doSync();
        }
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
        if (smart.hasDeviceStatusService()) {
            processProtobufDeviceStatusResponse(smart.getDeviceStatusService());
            processed = true;
        }
        if (smart.hasFindMyWatchService()) {
            processProtobufFindMyWatchResponse(smart.getFindMyWatchService());
            processed = true;
        }
        if (smart.hasCoreService()) {
            processProtobufCoreResponse(smart.getCoreService());
            processed = true;
        }
        if (!processed) {
            LOG.warn("Unknown protobuf response: {}", smart.toString());
        }
    }

    private void processProtobufCoreResponse(GdiCore.CoreService coreService) {
        if (coreService.hasSyncResponse()) {
            final GdiCore.CoreService.SyncResponse syncResponse = coreService.getSyncResponse();
            LOG.info("Received sync status: {}", syncResponse.getStatus());
        }
        LOG.warn("Unknown CoreService response: {}", coreService);
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
            return;
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
            case VivomoveConstants.MESSAGE_DIRECTORY_FILE_FILTER_REQUEST:
                processDirectoryFileFilterResponse(DirectoryFileFilterResponseMessage.parsePacket(packet));
                break;
            case VivomoveConstants.MESSAGE_DOWNLOAD_REQUEST:
                fileDownloadQueue.onDownloadRequestResponse(DownloadRequestResponseMessage.parsePacket(packet));
                break;
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
            case VivomoveConstants.MESSAGE_SUPPORTED_FILE_TYPES_REQUEST:
                processSupportedFileTypesResponse(SupportedFileTypesResponseMessage.parsePacket(packet));
                break;
            case VivomoveConstants.MESSAGE_GNCS_DATA_SOURCE:
                gncsDataSourceQueue.responseReceived(GncsDataSourceResponseMessage.parsePacket(packet));
                break;
            default:
                LOG.info("Received response to message {}: {}", responseMessage.requestID, responseMessage.getStatusStr());
                break;
        }
    }

    private void processDirectoryFileFilterResponse(DirectoryFileFilterResponseMessage responseMessage) {
        if (responseMessage.status == VivomoveConstants.STATUS_ACK && responseMessage.response == DirectoryFileFilterResponseMessage.RESPONSE_DIRECTORY_FILTER_APPLIED) {
            LOG.info("Received response to directory file filter request: {}/{}, requesting download of directory data", responseMessage.status, responseMessage.response);
            fileDownloadQueue.addToDownloadQueue(0, 0);
        } else {
            LOG.error("Directory file filter request failed: {}/{}", responseMessage.status, responseMessage.response);
        }
    }

    private void processSupportedFileTypesResponse(SupportedFileTypesResponseMessage responseMessage) {
        final StringBuilder supportedTypes = new StringBuilder();
        for (SupportedFileTypesResponseMessage.FileTypeInfo type : responseMessage.fileTypes) {
            if (supportedTypes.length() > 0) supportedTypes.append(", ");
            supportedTypes.append(String.format(Locale.ROOT, "%d/%d: %s", type.fileDataType, type.fileSubType, type.garminDeviceFileType));
        }
        LOG.info("Received the list of supported file types (status={}): {}", responseMessage.status, supportedTypes);
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

        this.maxPacketSize = deviceInformationMessage.maxPacketSize;
        this.gncsDataSourceQueue = new GncsDataSourceQueue(communicator, maxPacketSize);

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
        sendCurrentTime();
        sendSettings();

        // and everything is ready now
        sendSyncReady();
        requestBatteryStatusUpdate();
        sendFitDefinitions();
        sendFitConnectivityMessage();
        requestSupportedFileTypes();
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

    private void sendRequestSync() {
        sendProtobufRequest(
                GdiSmartProto.Smart.newBuilder()
                        .setCoreService(
                                GdiCore.CoreService.newBuilder()
                                        .setSyncRequest(
                                                GdiCore.CoreService.SyncRequest.newBuilder()
                                        )
                        )
                        .build()
                        .toByteArray());
    }

    private void requestSupportedFileTypes() {
        LOG.info("Requesting list of supported file types");
        sendMessage(new SupportedFileTypesRequestMessage().packet);
    }

    private void sendSyncReady() {
        sendMessage(new SystemEventMessage(GarminSystemEventType.SYNC_READY, 0).packet);
    }

    private void sendCurrentTime() {
        final Map<GarminDeviceSetting, Object> settings = new LinkedHashMap<>(3);

        long now = System.currentTimeMillis();
        final TimeZone timeZone = TimeZone.getDefault();
        final Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTimeInMillis(now);
        int dstOffset = calendar.get(Calendar.DST_OFFSET) / 1000;
        int timeZoneOffset = timeZone.getOffset(now) / 1000;
        int garminTimestamp = GarminTimeUtils.javaMillisToGarminTimestamp(now);

        settings.put(GarminDeviceSetting.CURRENT_TIME, garminTimestamp);
        settings.put(GarminDeviceSetting.DAYLIGHT_SAVINGS_TIME_OFFSET, dstOffset);
        settings.put(GarminDeviceSetting.TIME_ZONE_OFFSET, timeZoneOffset);
        // TODO: NEXT_DAYLIGHT_SAVINGS_START, NEXT_DAYLIGHT_SAVINGS_END
        LOG.info("Setting time to {}, dstOffset={}, tzOffset={} (DST={})", garminTimestamp, dstOffset, timeZoneOffset, timeZone.inDaylightTime(new Date(now)) ? 1 : 0);
        sendMessage(new SetDeviceSettingsMessage(settings).packet);
    }

    private void sendSettings() {
        final Map<GarminDeviceSetting, Object> settings = new LinkedHashMap<>(3);

        settings.put(GarminDeviceSetting.WEATHER_CONDITIONS_ENABLED, true);
        settings.put(GarminDeviceSetting.WEATHER_ALERTS_ENABLED, true);
        settings.put(GarminDeviceSetting.AUTO_UPLOAD_ENABLED, true);
        LOG.info("Sending settings");
        sendMessage(new SetDeviceSettingsMessage(settings).packet);
    }

    private void sendFitDefinitions() {
        sendMessage(new FitDefinitionMessage(
                FitMessageDefinitions.DEFINITION_CONNECTIVITY,
                FitMessageDefinitions.DEFINITION_WEATHER_CONDITIONS,
                FitMessageDefinitions.DEFINITION_WEATHER_ALERT
        ).packet);
    }

    private void sendFitConnectivityMessage() {
        final FitMessage connectivityMessage = new FitMessage(FitMessageDefinitions.DEFINITION_CONNECTIVITY);
        connectivityMessage.setField(0, FitBool.TRUE);
        connectivityMessage.setField(1, FitBool.TRUE);
        connectivityMessage.setField(2, FitBool.INVALID);
        connectivityMessage.setField(4, FitBool.TRUE);
        connectivityMessage.setField(5, FitBool.TRUE);
        connectivityMessage.setField(6, FitBool.TRUE);
        connectivityMessage.setField(7, FitBool.TRUE);
        connectivityMessage.setField(8, FitBool.TRUE);
        connectivityMessage.setField(9, FitBool.TRUE);
        connectivityMessage.setField(10, FitBool.TRUE);
        connectivityMessage.setField(13, FitBool.TRUE);
        sendMessage(new FitDataMessage(connectivityMessage).packet);
    }

    private void sendWeatherConditions() {
        final WeatherSpec weather = lastWeatherSpec;
        final FitMessage weatherConditionsMessage = new FitMessage(FitMessageDefinitions.DEFINITION_WEATHER_CONDITIONS);
        weatherConditionsMessage.setField(253, GarminTimeUtils.unixTimeToGarminTimestamp(weather.timestamp));
        weatherConditionsMessage.setField(0, 0); // 0 = current, 2 = hourly_forecast, 3 = daily_forecast
        weatherConditionsMessage.setField(1, weather.currentTemp);
        weatherConditionsMessage.setField(2, FitWeatherConditions.openWeatherCodeToFitWeatherStatus(weather.currentConditionCode));
        weatherConditionsMessage.setField(3, weather.windDirection);
        weatherConditionsMessage.setField(4, Math.round(weather.windSpeed * 1000.0 / 3.6));
        weatherConditionsMessage.setField(7, weather.currentHumidity);
        weatherConditionsMessage.setField(8, weather.location);
        final Calendar timestamp = Calendar.getInstance();
        timestamp.setTimeInMillis(weather.timestamp * 1000L);
        weatherConditionsMessage.setField(12, timestamp.get(Calendar.DAY_OF_WEEK));
        weatherConditionsMessage.setField(13, weather.todayMaxTemp);
        weatherConditionsMessage.setField(14, weather.todayMinTemp);

        sendMessage(new FitDataMessage(weatherConditionsMessage).packet);
    }

    private void sendWeatherAlert() {
        final FitMessage weatherConditionsMessage = new FitMessage(FitMessageDefinitions.DEFINITION_WEATHER_ALERT);
        weatherConditionsMessage.setField(253, GarminTimeUtils.javaMillisToGarminTimestamp(System.currentTimeMillis()));
        weatherConditionsMessage.setField(0, "TESTRPT");
        final Calendar issue = Calendar.getInstance();
        issue.set(2019, 8, 27, 0, 0, 0);
        final Calendar expiry = Calendar.getInstance();
        issue.set(2019, 8, 29, 0, 0, 0);
        weatherConditionsMessage.setField(1, GarminTimeUtils.javaMillisToGarminTimestamp(issue.getTimeInMillis()));
        weatherConditionsMessage.setField(2, GarminTimeUtils.javaMillisToGarminTimestamp(expiry.getTimeInMillis()));
        weatherConditionsMessage.setField(3, FitWeatherConditions.ALERT_SEVERITY_ADVISORY);
        weatherConditionsMessage.setField(4, FitWeatherConditions.ALERT_TYPE_SEVERE_THUNDERSTORM);

        sendMessage(new FitDataMessage(weatherConditionsMessage).packet);
    }

    private void sendNotification(AncsEvent event, NotificationSpec spec) {
        final AncsCategory category;
        final Set<AncsEventFlag> flags = new HashSet<>();
        switch (spec.type) {
            case GENERIC_SMS:
                category = AncsCategory.SMS;
                break;
            case GENERIC_PHONE:
                category = AncsCategory.INCOMING_CALL;
                flags.add(AncsEventFlag.IMPORTANT);
                break;
            case GENERIC_EMAIL:
            case GMAIL:
            case BBM:
            case MAILBOX:
            case OUTLOOK:
                category = AncsCategory.EMAIL;
                break;
            case GENERIC_NAVIGATION:
            case GOOGLE_MAPS:
                category = AncsCategory.LOCATION;
                break;
            case GENERIC_CALENDAR:
            case GENERIC_ALARM_CLOCK:
                category = AncsCategory.SCHEDULE;
                break;
            case FACEBOOK:
            case LINKEDIN:
                flags.add(AncsEventFlag.SILENT);
                category = AncsCategory.SOCIAL;
                break;
            // TODO: The rest
            default:
                category = AncsCategory.OTHER;
                break;
        }
        sendMessage(new GncsNotificationSourceMessage(event, flags, category, 1, assignNotificationId()).packet);
    }

    private void listFiles(int filterType) {
        LOG.info("Requesting file list (filter={})", filterType);
        sendMessage(new DirectoryFileFilterRequestMessage(filterType).packet);
    }

    private void downloadFile(int fileIndex) {
        LOG.info("Requesting download of file {}", fileIndex);
        sendMessage(new DownloadRequestMessage(fileIndex, 0, 1, 0, 0).packet);
    }

    private void downloadGarminDeviceXml() {
        LOG.info("Requesting Garmin device XML download");
        sendMessage(new DownloadRequestMessage(VivomoveConstants.GARMIN_DEVICE_XML_FILE_INDEX, 0, 1, 0, 0).packet);
    }

    private void sendBatteryStatus() {
        LOG.info("Sending battery status");
        sendMessage(new BatteryStatusMessage(12).packet);
    }

    private void doSync() {
        LOG.info("Starting sync");
        fitImporter = new FitDbImporter(getDevice());
        // sendMessage(new SystemEventMessage(GarminSystemEventType.PAIR_START, 0).packet);
        listFiles(0);
        // TODO: Localization
        GB.updateTransferNotification(null, "Downloading list of files", true, 0, getContext());
    }

    @Override
    public boolean useAutoConnect() {
        return false;
    }

    @Override
    public void onNotification(NotificationSpec notificationSpec) {
        dbg("onNotification " + notificationSpec.type + " #" + notificationSpec.getId());
        if (notificationSubscription) {
            sendNotification(AncsEvent.NOTIFICATION_ADDED, notificationSpec);
        } else {
            LOG.debug("No notification subscription is active, ignoring notification");
        }
    }

    @Override
    public void onDeleteNotification(int id) {
        dbg("onDeleteNotification " + id);
    }

    @Override
    public void onSetTime() {
        dbg("onSetTime()");
        sendCurrentTime();
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
        communicator.enableRealtimeSteps(enable);
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
        /*
        public static int TYPE_ACTIVITY     = 0x00000001;
        public static int TYPE_WORKOUTS     = 0x00000002;
        public static int TYPE_GPS_TRACKS   = 0x00000004;
        public static int TYPE_TEMPERATURE  = 0x00000008;
        public static int TYPE_DEBUGLOGS    = 0x00000010;
        */

        dbg("onFetchRecordedData " + dataTypes);
        doSync();
    }

    @Override
    public void onReset(int flags) {
        switch (flags) {
            case GBDeviceProtocol.RESET_FLAGS_FACTORY_RESET:
                LOG.warn("Requesting factory reset");
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
        communicator.enableRealtimeHeartRate(enable);
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

    private boolean foreground;

    @Override
    public void onTestNewFunction() {
        dbg("onTestNewFunction()");
        sendMessage(new SystemEventMessage(GarminSystemEventType.NEW_DOWNLOAD_AVAILABLE, 0).packet);
        //sendMessage(new SystemEventMessage(foreground ? GarminSystemEventType.HOST_DID_ENTER_BACKGROUND : GarminSystemEventType.HOST_DID_ENTER_FOREGROUND, 0).packet);
        //foreground = !foreground;
    }

    @Override
    public void onSendWeather(WeatherSpec weatherSpec) {
        dbg("onSendWeather");
        this.lastWeatherSpec = weatherSpec;
        sendWeatherConditions();
    }

    private long totalDownloadSize;
    private long lastTransferNotificationTimestamp;

    @Override
    public void onDirectoryDownloaded(DirectoryData directoryData) {
        long totalSize = 0;
        for (DirectoryEntry entry : directoryData.entries) {
            LOG.info("File #{}: type {}/{} #{}, {}B, flags {}/{}, timestamp {}", entry.fileIndex, entry.fileDataType, entry.fileSubType, entry.fileNumber, entry.fileSize, entry.specificFlags, entry.fileFlags, entry.fileDate);
            if (entry.fileIndex == 0) {
                // ?
                LOG.warn("File #0 reported?");
                continue;
            }
            fileDownloadQueue.addToDownloadQueue(entry.fileIndex, entry.fileSize);
            totalSize += entry.fileSize;
        }
        totalDownloadSize = totalSize;
    }

    @Override
    public void onFileDownloadComplete(int fileIndex, byte[] data) {
        LOG.info("Downloaded file {}: {} bytes", fileIndex, data.length);
        fitImporter.processFitFile(fitParser.parseFitFile(data));
        try {
            final File outputFile = new File(FileUtils.getExternalFilesDir(), "vivomovehr-" + fileIndex + ".fit");
            FileUtils.copyStreamToFile(new ByteArrayInputStream(data), outputFile);
        } catch (IOException e) {
            LOG.error("Unable to save file {}", fileIndex, e);
        }
    }

    @Override
    public void onFileDownloadError(int fileIndex) {
        LOG.error("Failed to download file {}", fileIndex);
    }

    @Override
    public void onDownloadProgress(long remainingBytes) {
        LOG.info("{}B/{} remaining to download", remainingBytes, totalDownloadSize);
        final long now = System.currentTimeMillis();
        if (now - lastTransferNotificationTimestamp < 1000) {
            // do not issue updates too often
            return;
        }
        lastTransferNotificationTimestamp = now;
        if (remainingBytes == 0) {
            GB.updateTransferNotification(null, null, false, 100, getContext());
        } else if (totalDownloadSize > 0) {
            GB.updateTransferNotification(null, "Downloading data", true, Math.round(100.0f * (totalDownloadSize - remainingBytes) / totalDownloadSize), getContext());
        }
    }

    @Override
    public void onAllDownloadsCompleted() {
        LOG.info("All downloads completed");
        sendMessage(new SystemEventMessage(GarminSystemEventType.SYNC_COMPLETE, 0).packet);
        fitImporter.processData();
        fitImporter = null;
    }
}
