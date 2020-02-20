package android.util;

import java.util.HashMap;

/**
 * Stupid (but working) implementation of Android-specific SparseLongArray to be used in unit tests running using JUnit in
 * classic JVM, where Android-specific code is not available
 */
public class SparseLongArray {
    private final HashMap<Integer, Long> mHashMap;

    public SparseLongArray() {
        mHashMap = new HashMap<>();
    }

    public SparseLongArray(int initialCapacity) {
        mHashMap = new HashMap<>(initialCapacity);
    }

    public void put(int key, long value) {
        mHashMap.put(key, value);
    }

    public int get(int key) {
        return get(key, 0);
    }

    public long get(int key, long valueIfNotFound) {
        final Long value = mHashMap.get(key);
        return value == null ? valueIfNotFound : value;
    }

    public void append(int key, long value) {
        put(key, value);
    }

    public void clear() {
        mHashMap.clear();
    }
}
