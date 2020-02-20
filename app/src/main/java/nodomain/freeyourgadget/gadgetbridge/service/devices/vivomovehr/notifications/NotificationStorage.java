package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.notifications;

import android.util.SparseArray;
import android.util.SparseLongArray;
import nodomain.freeyourgadget.gadgetbridge.model.NotificationSpec;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.ancs.AncsCategory;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class NotificationStorage {
    private static final long EXPIRATION = 30 * 1000;

    private final Object lock = new Object();
    private final SparseArray<NotificationSpec> storage = new SparseArray<>();
    private final LinkedHashMap<Long, Set<Integer>> expirationQueue = new LinkedHashMap<>();
    private final SparseLongArray expirationForNotification = new SparseLongArray();

    public void registerNewNotification(NotificationSpec notificationSpec, AncsCategory category) {
        final long now = System.currentTimeMillis();
        final long expiration = now + EXPIRATION;

        final int notificationId = notificationSpec.getId();

        synchronized (lock) {
            cleanup();
            storage.put(notificationId, notificationSpec);
            expirationForNotification.put(notificationId, expiration);

            Set<Integer> expirationSet = expirationQueue.get(expiration);
            if (expirationSet == null) {
                expirationSet = new HashSet<>(1);
                expirationQueue.put(expiration, expirationSet);
            }
            expirationSet.add(notificationId);
        }
    }

    public void deleteNotification(int id) {
        synchronized (lock) {
            storage.delete(id);
            final long expiration = expirationForNotification.get(id);
            final Set<Integer> expirationSet = expirationQueue.get(expiration);
            if (expirationSet != null) {
                expirationSet.remove(id);
            }
            cleanup();
        }
    }

    public NotificationSpec retrieveNotification(int id) {
        synchronized (lock) {
            cleanup();
            return storage.get(id);
        }
    }

    public int getCountInCategory(AncsCategory category) {
        return 1;
    }

    private void cleanup() {

    }
}
