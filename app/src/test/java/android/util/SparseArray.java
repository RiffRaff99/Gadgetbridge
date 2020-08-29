package android.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Stupid (but working) implementation of Android-specific SparseArray to be used in unit tests running using JUnit in
 * classic JVM, where Android-specific code is not available
 */
public class SparseArray<E> {
    private final SortedMap<Integer, E> mHashMap;

    public SparseArray() {
        mHashMap = new TreeMap<>();
    }

    public SparseArray(int initialCapacity) {
        mHashMap = new TreeMap<>();
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

    public int size() { return mHashMap.size(); }

    public int keyAt(int index) {
        if (index < 0 || index >= size()) throw new IllegalArgumentException("Index out of range");

        int curr = 0;
        for (Integer key : mHashMap.keySet()) {
            if (curr == index) return key;
            ++curr;
        }
        // should not happen
        throw new IllegalArgumentException("Index out of range");
    }

    public E valueAt(int index) {
        return get(keyAt(index));
    }
}
