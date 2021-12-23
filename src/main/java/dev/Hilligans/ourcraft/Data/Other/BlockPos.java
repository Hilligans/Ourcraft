package dev.Hilligans.ourcraft.Data.Other;

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

    public BlockPos(Vector3d vector3d) {
        this.x = (int)Math.round(vector3d.x);
        this.y = (int)Math.round(vector3d.y);
        this.z = (int)Math.round(vector3d.z);
    }

    public BlockPos(double x, double y, double z) {
        this.x = (int)Math.round(x);
        this.y = (int)Math.round(y);
        this.z = (int)Math.round(z);
    }

    public static BlockPos fromSubChunkPos(int pos) {
        return new BlockPos(pos & 0xF, (pos & 0xF0) >> 4, (pos & 0xF00) >> 8);
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

    public BlockPos add(Vector3d vector3d) {
        this.x += vector3d.x;
        this.y += vector3d.y;
        this.z += vector3d.z;
        return this;
    }

    public BlockPos multiply(Vector3d vector3d) {
        this.x *= vector3d.x;
        this.y *= vector3d.y;
        this.z *= vector3d.z;
        return this;
    }

    public BlockPos rotateZ(double cos, double sin) {
        x = (int) (this.x * cos - this.y * sin);
        y = (int) (this.x * sin + this.y * cos);
        return this;
    }

    public BlockPos rotateY(double cos, double sin) {
        x = (int) (this.x * cos + this.z * sin);
        z = (int) (-this.x * sin + this.z * cos);
        return this;
    }

    public BlockPos rotateX(double cos, double sin) {
        y = (int) (this.y * cos - this.z * sin);
        z = (int) (this.y * sin + this.z * cos);
        return this;
    }

    public BlockPos rotate(double x, double y, double z, double ang) {
        return new BlockPos(new Vector3d(this.x,this.y,this.z).rotateAxis(ang,x,y,z));
    }

    public BlockPos swapXAndZ() {
        int zz = this.z;
        this.z = this.x;
        this.x = zz;
        return this;
    }

    public BlockPos negateX() {
        this.x = -this.x;
        return this;
    }

    public BlockPos negateZ() {
        this.z = -this.z;
        return this;
    }

    public int minX(BlockPos pos) {
        return Math.min(x,pos.x);
    }

    public int minY(BlockPos pos) {
        return Math.min(y,pos.y);
    }

    public int minZ(BlockPos pos) {
        return Math.min(z,pos.z);
    }

    public int maxX(BlockPos pos) {
        return Math.max(x,pos.x);
    }

    public int maxY(BlockPos pos) {
        return Math.max(y,pos.y);
    }

    public int maxZ(BlockPos pos) {
        return Math.max(z,pos.z);
    }

    public BoundingBox createBoundingBox(BlockPos secondPos) {
        return new BoundingBox(minX(secondPos),minY(secondPos),minZ(secondPos),maxX(secondPos),maxY(secondPos),maxZ(secondPos));
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

    public long getChunkX() {
        return x >> 4 & 4294967295L;
    }

    public long getChunkZ() {
        return z >> 4 & 4294967295L;
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
