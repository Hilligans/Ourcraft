package dev.Hilligans.ourcraft.Data.Other;

import org.joml.Vector3d;
import org.joml.Vector3f;

public class BoundingBox {

    public float minX;
    public float minY;
    public float minZ;
    public float maxX;
    public float maxY;
    public float maxZ;

    public float eyeHeight;

    public BoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        eyeHeight = maxY;
    }

    public BoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float eyeHeight) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.eyeHeight = eyeHeight;
    }

    public BoundingBox(float[] values) {
        this.minX = values[0];
        this.minY = values[1];
        this.minZ = values[2];
        this.maxX = values[3];
        this.maxY = values[4];
        this.maxZ = values[5];
        eyeHeight = maxY;
    }

    public boolean intersectsBox(BoundingBox other, Vector3d myPos, Vector3d otherPos) {
        return this.minX + myPos.x <= other.maxX + otherPos.x && this.maxX + myPos.x >= other.minX + otherPos.x && this.minY + myPos.y <= other.maxY + otherPos.y && this.maxY + myPos.y >= other.minY + otherPos.y && this.minZ + myPos.z <= other.maxZ + otherPos.z && this.maxZ  + myPos.z >= other.minZ + otherPos.z;
    }

    public boolean intersectsBox(BoundingBox other, Vector3d myPos, Vector3d otherPos, double velX, double velY, double velZ) {
        return intersectsBox(other,myPos,new Vector3d(otherPos.x + velX,otherPos.y + velY,otherPos.z + velZ));
    }

    public boolean intersectsBox(BoundingBox other) {
        return this.minX < other.maxX && this.maxX > other.minX && this.minY < other.maxY && this.maxY > other.minY && this.minZ < other.maxZ && this.maxZ > other.minZ;
    }

    public boolean intersectVector(Vector3f vector3f) {
        return this.minX < vector3f.x && this.minY < vector3f.y && this.minZ < vector3f.z && this.maxX > vector3f.x && this.maxY > vector3f.y && this.maxZ > vector3f.z;
    }

    public boolean intersectVector(Vector3f vector3f, BlockPos source) {
        return intersectVector(new Vector3f(vector3f.x - source.x, vector3f.y - source.y, vector3f.z - source.z));
    }

    public int getHitSide(Vector3f last) {
        if(this.minX < last.x && this.maxX > last.x) {
            if(this.minY < last.y && this.maxY > last.y) {
                if(this.minZ > last.z) {
                    return 0;
                } else if(this.maxZ < last.z ){
                    return 1;
                }
            } else if(this.minZ < last.z && this.maxZ > last.z) {
                if(this.minY > last.y) {
                    return 4;
                } else if(this.maxY < last.y) {
                    return 5;
                }
            }
        }
        if(this.minZ < last.z && this.maxZ > last.z && this.minY < last.y && this.maxY > last.y) {
            if(this.minX > last.x) {
                return 2;
            } else if(this.maxX < last.x){
                return 3;
            }
        }
        return -1;
    }

    public int getHitSide(Vector3f vector3f, BlockPos source) {
        return getHitSide(new Vector3f(vector3f.x - source.x, vector3f.y - source.y, vector3f.z - source.z));
    }

    public int getHitSide(Vector3d vector3d, BlockPos source) {
        return getHitSide(new Vector3f((float)vector3d.x - source.x, (float)vector3d.y - source.y, (float)vector3d.z - source.z));
    }

    public BoundingBox rotateX(int degrees, float size) {
        float halfSize = size / 2;
        Vector3f min = new Vector3f(minX - halfSize,minY - halfSize,minZ - halfSize);
        min.rotateX((float) Math.toRadians(degrees * 90));
        Vector3f max = new Vector3f(maxX - halfSize,maxY - halfSize,maxZ - halfSize);
        max.rotateX((float) Math.toRadians(degrees * 90));
        return new BoundingBox(Math.min(min.x + halfSize, max.x + halfSize),Math.min(min.y + halfSize,max.y + halfSize),Math.min(min.z + halfSize,max.z + halfSize),Math.max(min.x + halfSize, max.x + halfSize),Math.max(min.y + halfSize,max.y + halfSize),Math.max(min.z + halfSize,max.z + halfSize));
    }

    public BoundingBox rotateY(int degrees, float size) {
        float halfSize = size / 2;
        Vector3f min = new Vector3f(minX - halfSize,minY - halfSize,minZ - halfSize);
        min.rotateY((float) Math.toRadians(degrees * 90));
        Vector3f max = new Vector3f(maxX - halfSize,maxY - halfSize,maxZ - halfSize);
        max.rotateY((float) Math.toRadians(degrees * 90));
        return new BoundingBox(Math.min(min.x + halfSize, max.x + halfSize),Math.min(min.y + halfSize,max.y + halfSize),Math.min(min.z + halfSize,max.z + halfSize),Math.max(min.x + halfSize, max.x + halfSize),Math.max(min.y + halfSize,max.y + halfSize),Math.max(min.z + halfSize,max.z + halfSize));
    }

    public BoundingBox duplicate() {
        return new BoundingBox(minX,minY,minZ,maxX,maxY,maxZ);
    }

    public float getVolume() {
        return (maxX - minX) * (maxY - minY) * (maxZ - minZ);
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "minX=" + minX +
                ", minY=" + minY +
                ", minZ=" + minZ +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                ", maxZ=" + maxZ +
                ", eyeHeight=" + eyeHeight +
                '}';
    }
}
