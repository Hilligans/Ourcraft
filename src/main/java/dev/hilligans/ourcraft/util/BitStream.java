package dev.hilligans.ourcraft.util;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;

public class BitStream {

    public ByteArrayList data;
    public int bytePointer;
    public int bitPointer;

    public BitStream() {
        data = new ByteArrayList(100);
    }

    public void put(long bit) {
        data.set(bytePointer,(byte)((data.getByte(bytePointer) & ~(1 << bitPointer)) | ((bit == 0 ? 0 : 1) << bitPointer)));
        if(bitPointer++ >= 8) {
            bitPointer = 0;
            bytePointer++;
        }
    }

    public void put(int size, long value) {
        for(int x = 0; x < size; x++) {
            put((value & (1L << x)));
        }
    }
}
