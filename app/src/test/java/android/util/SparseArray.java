package android.util;

import java.util.HashMap;

/**
 * Stupid (but working) implementation of Android-specific SparseArray to be used in unit tests running using JUnit in
 * classic JVM, where Android-specific code is not available
 */
public class SparseArray<E> {
    private final HashMap<Integer, E> mHashMap;

    public SparseArray() {
        mHashMap = new HashMap<>();
    }

    public SparseArray(int initialCapacity) {
        mHashMap = new HashMap<>(initialCapacity);
    }

    public void put(int key, E value) {
        mHashMap.put(key, value);
    }

    public E get(int key) {
        return mHashMap.get(key);
    }

    public E get(int key, E defaultValue) {
        return mHashMap.containsKey(key) ? mHashMap.get(key) : defaultValue;
    }

    public void append(int key, E value) {
        put(key, value);
    }

    public void clear() {
        mHashMap.clear();
    }
}
