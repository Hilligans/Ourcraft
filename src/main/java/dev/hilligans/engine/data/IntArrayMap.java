package dev.hilligans.engine.data;

import org.bouncycastle.util.Arrays;

public class IntArrayMap {

    private static final long mask = 0xFFFFFFFFL;
    private static final long noNext = mask << 32;

    private long[] map;

    public IntArrayMap(int size) {
        this.map = new long[size];
    }

    public boolean add(int value) {
        int key = value % map.length;
        if(map[key] != 0) {
            return false;
        }

        map[key] = value | noNext;
        return true;
    }

    public int findPosition() {
        for(int x = 0; x < map.length; x++) {
            if(map[x] == 0) {
                return x;
            }
        }
        throw new RuntimeException("No space!");
    }

    public void addLast(int value) {
        int key = value % map.length;

        int next;
        while((next = next(key)) != -1) {
            key = next;
        }

        int newIndex = findPosition();
        map[key] = (map[key] & mask) | ((long)newIndex << 32);
        map[newIndex] = value | noNext;
    }

    public int get(int value) {
        int key = value % map.length;

        while((map[key] & mask) != value) {
            key = next(key);
        }

        return key;
    }

    public int getMaxChainSize() {
        int maxCount = 0;
        for(int x = 0; x < map.length; x++) {
            int index = x;
            int count = 1;
            while((index = next(index)) != -1) {
                count++;
            }
            if(count > maxCount) {
                maxCount = count;
            }
        }
        return maxCount;
    }

    private int next(int key) {
        long val = map[key];
        if(val == 0) {
            return -1;
        }

        int next = (int)(val >>> 32);
        if((val & noNext) == noNext) {
            return -1;
        }

        return next;
    }

    public int size() {
        return map.length;
    }

    public void clear() {
        Arrays.fill(map, 0);
    }
}
