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
        return rawType;
    }

    @Override
    public int toRawActivityKind(int activityKind) {
        return activityKind;
    }

    @Override
    public float normalizeIntensity(int rawIntensity) {
        return rawIntensity / (float) 8000.0;
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
