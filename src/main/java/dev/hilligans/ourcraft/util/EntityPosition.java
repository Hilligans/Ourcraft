package dev.hilligans.ourcraft.util;

import dev.hilligans.ourcraft.tag.CompoundNBTTag;

public class EntityPosition implements IPosition {

    public int chunkX;
    public int chunkY;
    public int chunkZ;

    public float x;
    public float y;
    public float z;

    public short chunkWidth;
    public short chunkHeight;

    public EntityPosition() {
    }

    public EntityPosition(double x, double y, double z) {
        set(x,y,z);
    }

    public EntityPosition(CompoundNBTTag tag) {
        chunkX = tag.getInt("cx");
        chunkY = tag.getInt("cy");
        chunkZ = tag.getInt("cz");

        x = tag.getFloat("x");
        y = tag.getFloat("y");
        z = tag.getFloat("z");

        int val = tag.getInt("w");
        chunkWidth = (short) (val);
        chunkHeight = (short) (val >> 16);
    }

    public EntityPosition(IByteArray byteArray) {
        chunkX = byteArray.readInt();
        chunkY = byteArray.readInt();
        chunkZ = byteArray.readInt();

        x = byteArray.readFloat();
        y = byteArray.readFloat();
        z = byteArray.readFloat();

        chunkHeight = byteArray.readShort();
        chunkWidth = byteArray.readShort();
    }

    @Override
    public long getRawX() {
        return (long)chunkX * chunkWidth + (long)x;
    }

    @Override
    public long getRawY() {
        return (long)chunkY * chunkHeight + (long)y;
    }

    @Override
    public long getRawZ() {
        return (long)chunkZ * chunkWidth + (long)z;
    }

    @Override
    public double getX() {
        return (double)chunkX * chunkWidth + x;
    }

    @Override
    public double getY() {
        return (double)chunkY * chunkHeight + y;
    }

    @Override
    public double getZ() {
        return (double)chunkZ * chunkWidth + z;
    }

    @Override
    public void add(double x, double y, double z) {
        x += this.x;
        int leftOver = (int) (x / chunkWidth);
        this.x = (float) (x - leftOver * chunkWidth);
        chunkX += leftOver;

        y += this.y;
        leftOver = (int) (y / chunkHeight);
        this.y = (float) (y - leftOver * chunkHeight);
        chunkY += leftOver;

        z += this.z;
        leftOver = (int) (z / chunkWidth);
        this.z = (float) (z - leftOver * chunkWidth);
        chunkZ += leftOver;
    }

    @Override
    public void set(double x, double y, double z) {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        add(x,y,z);
    }

    @Override
    public IPosition add(IPosition b, IPosition dest) {
        if(b instanceof EntityPosition pos && dest instanceof EntityPosition de) {
            if(pos.chunkWidth == chunkWidth && pos.chunkHeight == chunkHeight) {
                de.chunkX = chunkX + pos.chunkX;
                de.chunkY = chunkY + pos.chunkY;
                de.chunkZ = chunkZ + pos.chunkZ;

                de.x = x + pos.x;
                de.y = y + pos.y;
                de.z = z + pos.z;

                de.chunkWidth = chunkWidth;
                de.chunkHeight = chunkHeight;

                if (de.x > chunkWidth) {
                    de.x -= chunkWidth;
                    de.chunkX++;
                }
                if (de.y > chunkHeight) {
                    de.y -= chunkHeight;
                    de.chunkY++;
                }
                if (de.z > chunkWidth) {
                    de.z -= chunkWidth;
                    de.chunkZ++;
                }
                return dest;
            }
        }
        dest.set(getX() + b.getX(), getY() + b.getY(), getZ() + b.getZ());
        return dest;
    }

    @Override
    public IPosition sub(IPosition b, IPosition dest) {
        if(b instanceof EntityPosition pos && dest instanceof EntityPosition de) {
            if(pos.chunkWidth == chunkWidth && pos.chunkHeight == chunkHeight) {
                de.chunkX = chunkX - pos.chunkX;
                de.chunkY = chunkY - pos.chunkY;
                de.chunkZ = chunkZ - pos.chunkZ;

                de.x = x - pos.x;
                de.y = y - pos.y;
                de.z = z - pos.z;

                de.chunkWidth = chunkWidth;
                de.chunkHeight = chunkHeight;

                if (de.x < 0) {
                    de.x += chunkWidth;
                    de.chunkX--;
                }
                if (de.y < 0) {
                    de.y += chunkHeight;
                    de.chunkY--;
                }
                if (de.z < 0) {
                    de.z += chunkWidth;
                    de.chunkZ--;
                }
                return dest;
            }
        }
        dest.set(getX() - b.getX(), getY() - b.getY(), getZ() - b.getZ());
        return dest;
    }

    @Override
    public void write(CompoundNBTTag tag) {
        tag.putInt("cx",chunkX);
        tag.putInt("cy",chunkY);
        tag.putInt("cz",chunkZ);

        tag.putFloat("x",x);
        tag.putFloat("y",y);
        tag.putFloat("z",z);
        tag.putInt("w",chunkWidth | chunkHeight << 16);
    }

    @Override
    public void write(IByteArray byteArray) {
        byteArray.writeInt(chunkX);
        byteArray.writeInt(chunkY);
        byteArray.writeInt(chunkZ);

        byteArray.writeFloat(x);
        byteArray.writeFloat(y);
        byteArray.writeFloat(z);

        byteArray.writeShort(chunkHeight);
        byteArray.writeShort(chunkWidth);
    }

    @Override
    public boolean isInteger() {
        return false;
    }
}
