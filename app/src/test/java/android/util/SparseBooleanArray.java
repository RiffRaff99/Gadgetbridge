package android.util;

import java.util.HashMap;

/**
 * Stupid (but working) implementation of Android-specific SparseBooleanArray to be used in unit tests running using JUnit in
 * classic JVM, where Android-specific code is not available
 */
public class SparseBooleanArray {
    private final SparseArray<Boolean> impl;

    public SparseBooleanArray() {
        impl = new SparseArray<>();
    }

    public SparseBooleanArray(int initialCapacity) {
        impl = new SparseArray<>(initialCapacity);
    }

    public void put(int key, boolean value) {
        impl.put(key, value);
    }

    public boolean get(int key) {
        return impl.get(key, false);
    }

    public boolean get(int key, boolean valueIfNotFound) {
        return impl.get(key, valueIfNotFound);
    }

    public void append(int key, boolean value) {
        impl.append(key, value);
    }

    public void clear() {
        impl.clear();
    }

    public int size() {
        return impl.size();
    }

    public int keyAt(int index) {
        return impl.keyAt(index);
    }

    public boolean valueAt(int index) {
        return impl.valueAt(index);
    }

    public void delete(int key) {
        impl.delete(key);
    }

    public void remove(int key) {
        impl.remove(key);
    }

    public void removeAt(int index) {
        impl.removeAt(index);
    }

    public void removeAtRange(int index, int size) {
        impl.removeAtRange(index, size);
    }

    public void setValueAt(int index, boolean value) {
        impl.setValueAt(index, value);
    }

    public int indexOfKey(int key) {
        return impl.indexOfKey(key);
    }

    public int indexOfValue(boolean value) {
        return impl.indexOfValue(value);
    }
}
