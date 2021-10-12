package dev.Hilligans.Ourcraft.Util;

import java.util.ArrayList;

public class BitArray {

    public ArrayList<Long> bits = new ArrayList<>();
    public long val = 0;
    public long readPos = 63;
    public long writePos = 63;
    public long readIndex = 0;
    public long writeIndex = 0;

    public long read(int length) {
        long temp = 0;
        for(int x = 0; x < length; x++) {
            temp |= getBit() << (length - x - 1);
        }
        return temp;
    }

    public void write(long val, int length) {
        int highestBit = findHighestBit(val);
        if(highestBit > length) {
            highestBit = length;
        }
        for(int x = 0; x < length - highestBit - 1; x++) {
            writeBit(0);
        }
        for(int x = 0; x < highestBit + 1; x++) {
            if((val >> (highestBit - x) & 1) == 1) {
                writeBit(1);
            } else {
                writeBit(0);
            }
        }
    }

    public void flip() {
         readPos = 63 - writePos;
    }

    private int findHighestBit(long val) {
        int bit = 0;
        for(int x = 0; x < 64; x++) {
            if((val >> x & 1) == 1) {
                bit = x;
            }
        }
        return bit;
    }

    private void writeBit(long bit) {
        val |= bit << writePos;
        writePos--;
        if(writePos < 0) {
            writePos = 63;
            bits.add(val);
            val = 0;
        }
    }

    private long getBit() {
        long temp = val >> readPos & 1;
        readPos--;
        if(readPos < 0) {
            readPos = 63;
            val = bits.get(0);
            bits.remove(0);
        }
        return temp;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(long val : bits) {
            s.append(Long.toBinaryString(val));
        }
        s.append(Long.toBinaryString(val));
        return s.toString();
    }
}
