package nodomain.freeyourgadget.gadgetbridge.devices.vivomovehr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import nodomain.freeyourgadget.gadgetbridge.devices.AbstractSampleProvider;
import nodomain.freeyourgadget.gadgetbridge.entities.DaoSession;
import nodomain.freeyourgadget.gadgetbridge.entities.VivomoveHrActivitySample;
import nodomain.freeyourgadget.gadgetbridge.entities.VivomoveHrActivitySampleDao;
import nodomain.freeyourgadget.gadgetbridge.impl.GBDevice;
import nodomain.freeyourgadget.gadgetbridge.model.ActivityKind;

public class VivomoveHrSampleProvider extends AbstractSampleProvider<VivomoveHrActivitySample> {
    private GBDevice mDevice;
    private DaoSession mSession;

    public VivomoveHrSampleProvider(GBDevice device, DaoSession session) {
        super(device, session);

        mSession = session;
        mDevice = device;
    }

    @Override
    public int normalizeType(int rawType) {
        switch (rawType) {
            case 0:
                // generic
                return ActivityKind.TYPE_ACTIVITY;
            case 1:
                // running
                return ActivityKind.TYPE_RUNNING;
            case 2:
                // cycling
                return ActivityKind.TYPE_CYCLING;
            case 3:
                // transition
                return ActivityKind.TYPE_ACTIVITY | ActivityKind.TYPE_RUNNING | ActivityKind.TYPE_WALKING | ActivityKind.TYPE_EXERCISE | ActivityKind.TYPE_SWIMMING;
            case 4:
                // fitness_equipment
                return ActivityKind.TYPE_EXERCISE;
            case 5:
                // swimming
                return ActivityKind.TYPE_SWIMMING;
            case 6:
                // walking
                return ActivityKind.TYPE_WALKING;
            case 8:
                // sedentary
                // TODO?
                return ActivityKind.TYPE_ACTIVITY;
            default:
                return ActivityKind.TYPE_UNKNOWN;
        }
    }

    @Override
    public int toRawActivityKind(int activityKind) {
        switch (activityKind) {
            case ActivityKind.TYPE_ACTIVITY:
                // generic
                return 0;
            case ActivityKind.TYPE_RUNNING:
                // running
                return 1;
            case ActivityKind.TYPE_CYCLING:
                // cycling
                return 2;
            case ActivityKind.TYPE_ACTIVITY | ActivityKind.TYPE_RUNNING | ActivityKind.TYPE_WALKING | ActivityKind.TYPE_EXERCISE | ActivityKind.TYPE_SWIMMING:
                return 3;
            case ActivityKind.TYPE_EXERCISE:
                // fitness_equipment
                return 4;
            case ActivityKind.TYPE_SWIMMING:
                // swimming
                return 5;
            case ActivityKind.TYPE_WALKING:
                // walking
                return 6;
            default:
                if ((activityKind & ActivityKind.TYPE_SWIMMING) != 0) return 5;
                if ((activityKind & ActivityKind.TYPE_CYCLING) != 0) return 2;
                if ((activityKind & ActivityKind.TYPE_RUNNING) != 0) return 1;
                if ((activityKind & ActivityKind.TYPE_EXERCISE) != 0) return 4;
                if ((activityKind & ActivityKind.TYPE_WALKING) != 0) return 6;
                if ((activityKind & ActivityKind.TYPE_ACTIVITY) != 0) return 0;
                if ((activityKind & ActivityKind.TYPE_SLEEP) != 0) return 8;
                return 0;
        }
    }

    @Override
    public float normalizeIntensity(int rawIntensity) {
        return rawIntensity;
    }

    @Override
    public VivomoveHrActivitySample createActivitySample() {
        return new VivomoveHrActivitySample();
    }

    @Override
    public AbstractDao<VivomoveHrActivitySample, ?> getSampleDao() {
        return getSession().getVivomoveHrActivitySampleDao();
    }

    @Nullable
    @Override
    protected Property getRawKindSampleProperty() {
        return VivomoveHrActivitySampleDao.Properties.RawKind;
    }

    @NonNull
    @Override
    protected Property getTimestampSampleProperty() {
        return VivomoveHrActivitySampleDao.Properties.Timestamp;
    }

    @NonNull
    @Override
    protected Property getDeviceIdentifierSampleProperty() {
        return VivomoveHrActivitySampleDao.Properties.DeviceId;
    }
}
