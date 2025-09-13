package dev.hilligans.engine.data;


import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Objects;
import java.util.function.Consumer;

public class FloatList {

    private static final int DEFAULT_CAPACITY = 10;

    private static final float[] EMPTY_ELEMENTDATA = {};

    private static final float[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    public float[] elementData;

    public int size;

    protected int modCount = 0;

    public FloatList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new float[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                    initialCapacity);
        }
    }

    public FloatList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    public void trimToSize() {
        modCount++;
        if (size < elementData.length) {
            elementData = (size == 0)
                    ? EMPTY_ELEMENTDATA
                    : Arrays.copyOf(elementData, size);
        }
    }

    public void ensureCapacity(int minCapacity) {
        int minExpand = (elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA) ? 0 : DEFAULT_CAPACITY;
        if (minCapacity > minExpand) {
            ensureExplicitCapacity(minCapacity);
        }
    }

    private static int calculateCapacity(float[] elementData, int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            return Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        return minCapacity;
    }

    public void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }

    public void ensureExplicitCapacity(int minCapacity) {
        modCount++;
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private void grow(int minCapacity) {
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public float get(int index) {
        rangeCheck(index);
        return elementData[index];
    }

    public float[] getElementData() {
        float[] floats = new float[size()];
        System.arraycopy(elementData,0,floats,0,size);
        return floats;
    }

    public FloatBuffer getElementDataBuffer() {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(size);
        return buffer.put(elementData,0,size).flip();
    }

    public float set(int index, float element) {
        rangeCheck(index);

        float oldValue = elementData[index];
        elementData[index] = element;
        return oldValue;
    }

    public boolean add(float e) {
        ensureCapacityInternal(size + 1);
        elementData[size++] = e;
        return true;
    }

    public void add(int index, float element) {
        rangeCheckForAdd(index);

        ensureCapacityInternal(size + 1);
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = element;
        size++;
    }

    public FloatList add(float... values) {
        ensureCapacityInternal(size + values.length);
        System.arraycopy(values,0,elementData,size,values.length);
        size+=values.length;
        return this;
    }

    public void add(Float[] values) {
        ensureCapacityInternal(size + values.length);
        for(Float val : values) {
            elementData[size++] = val;
        }
    }

    public float remove(int index) {
        rangeCheck(index);

        modCount++;
        float oldValue = elementData[index];

        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                    numMoved);
        elementData[--size] = 0.0f;

        return oldValue;
    }

    public void clear() {
        modCount++;
        for (int i = 0; i < size; i++)
            elementData[i] = 0.0f;

        size = 0;
    }

    protected void removeRange(int fromIndex, int toIndex) {
        modCount++;
        int numMoved = size - toIndex;
        System.arraycopy(elementData, toIndex, elementData, fromIndex, numMoved);
        int newSize = size - (toIndex-fromIndex);
        for (int i = newSize; i < size; i++) {
            elementData[i] = 0.0f;
        }
        size = newSize;
    }

    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, false);
    }

    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, true);
    }

    private boolean batchRemove(Collection<?> c, boolean complement) {
        final float[] elementData = this.elementData;
        int r = 0, w = 0;
        boolean modified = false;
        try {
            for (; r < size; r++)
                if (c.contains(elementData[r]) == complement)
                    elementData[w++] = elementData[r];
        } finally {
            if (r != size) {
                System.arraycopy(elementData, r,
                        elementData, w,
                        size - r);
                w += size - r;
            }
            if (w != size) {
                for (int i = w; i < size; i++)
                    elementData[i] = 0.0f;
                modCount += size - w;
                size = w;
                modified = true;
            }
        }
        return modified;
    }

    public void forEach(Consumer<Float> action) {
        Objects.requireNonNull(action);
        final int expectedModCount = modCount;
        final int size = this.size;
        for (int i=0; modCount == expectedModCount && i < size; i++) {
            action.accept(elementData[i]);
        }
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }
}
