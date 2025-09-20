package dev.hilligans.engine.data;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3i;

public class RectangleBB {

    public float minX;
    public float minY;
    public float maxX;
    public float maxY;

    public RectangleBB(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public RectangleBB(float[] values) {
        this.minX = values[0];
        this.minY = values[1];
        this.maxX = values[2];
        this.maxY = values[3];
    }

    public boolean intersectsBox(RectangleBB other, Vector2d myPos, Vector2d otherPos) {
        return this.minX + myPos.x <= other.maxX + otherPos.x && this.maxX + myPos.x >= other.minX + otherPos.x && this.minY + myPos.y <= other.maxY + otherPos.y && this.maxY + myPos.y >= other.minY + otherPos.y;
    }

    public boolean intersectsBox(RectangleBB other, Vector2d myPos, Vector2d otherPos, double velX, double velY) {
        return intersectsBox(other,myPos,new Vector2d(otherPos.x + velX,otherPos.y + velY));
    }

    public boolean intersectsBox(RectangleBB other) {
        return this.minX < other.maxX && this.maxX > other.minX && this.minY < other.maxY && this.maxY > other.minY;
    }

    public boolean intersectVector(Vector2f vector2f) {
        return this.minX < vector2f.x && this.minY < vector2f.y && this.maxX > vector2f.x && this.maxY > vector2f.y;
    }

    public boolean intersectVector(Vector2f vector2f, Vector3i source) {
        return intersectVector(new Vector2f(vector2f.x - source.x, vector2f.y - source.y));
    }

    public RectangleBB duplicate() {
        return new RectangleBB(minX,minY,maxX,maxY);
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "minX=" + minX +
                ", minY=" + minY +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                '}';
    }

}
