package Hilligans.Data.Other;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.World.VAOManager;
import org.joml.Vector3f;

public class BoundingBox {

    public float minX;
    public float minY;
    public float minZ;
    public float maxX;
    public float maxY;
    public float maxZ;

    public BoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public boolean intersectsBox(BoundingBox other, Vector3f myPos, Vector3f otherPos) {
        return this.minX + myPos.x <= other.maxX + otherPos.x && this.maxX + myPos.x >= other.minX + otherPos.x && this.minY + myPos.y <= other.maxY + otherPos.y && this.maxY + myPos.y >= other.minY + otherPos.y && this.minZ + myPos.z <= other.maxZ + otherPos.z && this.maxZ  + myPos.z >= other.minZ + otherPos.z;
    }

    public boolean intersectsBox(BoundingBox other, Vector3f myPos, Vector3f otherPos, float velX, float velY, float velZ) {
        return intersectsBox(other,myPos,new Vector3f(otherPos.x + velX,otherPos.y + velY,otherPos.z + velZ));
    }

    public boolean intersectsBox(BoundingBox other) {
        return this.minX < other.maxX && this.maxX > other.minX && this.minY < other.maxY && this.maxY > other.minY && this.minZ < other.maxZ && this.maxZ > other.minZ;
    }








}
