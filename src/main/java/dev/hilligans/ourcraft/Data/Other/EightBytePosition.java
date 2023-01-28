package dev.hilligans.ourcraft.Data.Other;

public class EightBytePosition {

    public long x;
    public long y;
    public long z;

    public EightBytePosition(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public EightBytePosition(long data) {
        x = data >> 38;
        y = data & 0xFFF;
        z = (data << 26 >> 38);
    }

    public long encode() {
        return ((x & 0x3FFFFFF) << 38) | ((z & 0x3FFFFFF) << 12) | (y & 0xFFF);
    }
}
