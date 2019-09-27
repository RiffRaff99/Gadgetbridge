package android.util;

import java.util.HashMap;

/**
 * Stupid (but working) implementation of Android-specific SparseIntArray to be used in unit tests running using JUnit in
 * classic JVM, where Android-specific code is not available
 */
public class SparseIntArray {
    private final HashMap<Integer, Integer> mHashMap;

    public SparseIntArray() {
        mHashMap = new HashMap<>();
    }

    public SparseIntArray(int initialCapacity) {
        mHashMap = new HashMap<>(initialCapacity);
    }

    public void put(int key, int value) {
        mHashMap.put(key, value);
    }

    public int get(int key) {
        return get(key, 0);
    }

    public int get(int key, int valueIfNotFound) {
        final Integer value = mHashMap.get(key);
        return value == null ? valueIfNotFound : value;
    }

    public void append(int key, int value) {
        put(key, value);
    }

    public void clear() {
        mHashMap.clear();
    }
}
