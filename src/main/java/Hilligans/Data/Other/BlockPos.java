package Hilligans.Data.Other;

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

    public BlockPos copy() {
        return new BlockPos(x,y,z);
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
