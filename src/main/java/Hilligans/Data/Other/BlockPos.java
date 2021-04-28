package Hilligans.Data.Other;

import org.joml.Vector3d;
import org.joml.Vector3f;

public class BlockPos {

    public int x;
    public int y;
    public int z;


    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos(long x, long y, long z) {
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
    }

    public BlockPos(Vector3f vector3f) {
        this.x = Math.round(vector3f.x);
        this.y = Math.round(vector3f.y);
        this.z = Math.round(vector3f.z);
    }

    public BlockPos add(BlockPos pos) {
        x += pos.x;
        y += pos.y;
        z += pos.z;
        return this;
    }

    public BlockPos add(int x, int y, int z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3f get3f() {
        return new Vector3f(x,y,z);
    }

    public Vector3d get3d() {
        return new Vector3d(x,y,z);
    }

    public BlockPos copy() {
        return new BlockPos(x,y,z);
    }

    public long getChunkPos() {
        return (long)x >> 4 & 4294967295L | ((long)z >> 4 & 4294967295L) << 32;
    }

    public boolean isSubChunkValid() {
        return x >= 0 && x <= 15 && y >= 0 && y <= 15 && z >= 0 && z <= 15;
    }

    @Override
    public String toString() {
        return "BlockPos{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
