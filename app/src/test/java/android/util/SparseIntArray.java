package android.util;

/**
 * Stupid (but working) implementation of Android-specific SparseIntArray to be used in unit tests running using JUnit in
 * classic JVM, where Android-specific code is not available
 */
public class SparseIntArray {
    private final SparseArray<Integer> impl;

    public SparseIntArray() {
        impl = new SparseArray<>();
    }

    public SparseIntArray(int initialCapacity) {
        impl = new SparseArray<>(initialCapacity);
    }

    public void put(int key, int value) {
        impl.put(key, value);
    }

    public int get(int key) {
        return impl.get(key, 0);
    }

    public int get(int key, int valueIfNotFound) {
        return impl.get(key, valueIfNotFound);
    }

    public void append(int key, int value) {
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

    public int valueAt(int index) {
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

    public void setValueAt(int index, int value) {
        impl.setValueAt(index, value);
    }

    public int indexOfKey(int key) {
        return impl.indexOfKey(key);
    }

    public int indexOfValue(int value) {
        return impl.indexOfValue(value);
    }
}
