package dev.hilligans.ourcraft.data.other;

import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.ArrayList;

public class JoinedBoundingBox extends BoundingBox {

    ArrayList<BoundingBox> boundingBoxes = new ArrayList<>();

    public JoinedBoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        super(minX,minY,minZ,maxX,maxY,maxZ);
        boundingBoxes.add(new BoundingBox(minX,minY,minZ,maxX,maxY,maxZ));
    }

    public JoinedBoundingBox(float[] vals) {
        super(vals);
        boundingBoxes.add(new BoundingBox(vals));
    }

    public JoinedBoundingBox addBox(BoundingBox boundingBox) {
        boundingBoxes.add(boundingBox);
        return this;
    }

    public JoinedBoundingBox addBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        boundingBoxes.add(new BoundingBox(minX,minY,minZ,maxX,maxY,maxZ));
        return this;
    }

    @Override
    public boolean intersectsBox(BoundingBox other, Vector3d myPos, Vector3d otherPos) {
        for(BoundingBox boundingBox : boundingBoxes) {
            if(boundingBox.intersectsBox(other,myPos,otherPos)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public float intersectsRay(float x, float y, float z, float dirX, float dirY, float dirZ, Vector2f vector2f) {
        float max = Float.MAX_VALUE;
        for(BoundingBox boundingBox : boundingBoxes) {
            float res = boundingBox.intersectsRay(x, y, z, dirX, dirY, dirZ, vector2f);
            if(res != -1) {
                if(res < max) {
                    max = res;
                }
            }
        }
        if(max == Float.MAX_VALUE) {
            return -1;
        }

        return max;
    }

    @Override
    public BoundingBox duplicate() {
        JoinedBoundingBox joinedBoundingBox = new JoinedBoundingBox(minX,minY,minZ,maxX,maxY,maxZ);
        for(int x = 1; x < boundingBoxes.size(); x++) {
            joinedBoundingBox.addBox(boundingBoxes.get(x).duplicate());
        }
        return joinedBoundingBox;
    }

    public BoundingBox rotateX(int degrees, float size) {
        float halfSize = size / 2;
        Vector3f min = new Vector3f(minX - halfSize,minY - halfSize,minZ - halfSize);
        min.rotateX((float) Math.toRadians(degrees * 90));
        Vector3f max = new Vector3f(maxX - halfSize,maxY - halfSize,maxZ - halfSize);
        max.rotateX((float) Math.toRadians(degrees * 90));
        JoinedBoundingBox joinedBoundingBox = new JoinedBoundingBox(Math.min(min.x + halfSize, max.x + halfSize),Math.min(min.y + halfSize,max.y + halfSize),Math.min(min.z + halfSize,max.z + halfSize),Math.max(min.x + halfSize, max.x + halfSize),Math.max(min.y + halfSize,max.y + halfSize),Math.max(min.z + halfSize,max.z + halfSize));
        for(int x = 1; x < boundingBoxes.size(); x++) {
            joinedBoundingBox.addBox(boundingBoxes.get(x).rotateX(degrees,size));
        }
        return joinedBoundingBox;
    }

    public BoundingBox rotateY(int degrees, float size) {
        float halfSize = size / 2;
        Vector3f min = new Vector3f(minX - halfSize,minY - halfSize,minZ - halfSize);
        min.rotateY((float) Math.toRadians(degrees * 90));
        Vector3f max = new Vector3f(maxX - halfSize,maxY - halfSize,maxZ - halfSize);
        max.rotateY((float) Math.toRadians(degrees * 90));
        JoinedBoundingBox joinedBoundingBox = new JoinedBoundingBox(Math.min(min.x + halfSize, max.x + halfSize),Math.min(min.y + halfSize,max.y + halfSize),Math.min(min.z + halfSize,max.z + halfSize),Math.max(min.x + halfSize, max.x + halfSize),Math.max(min.y + halfSize,max.y + halfSize),Math.max(min.z + halfSize,max.z + halfSize));
        for(int x = 1; x < boundingBoxes.size(); x++) {
            joinedBoundingBox.addBox(boundingBoxes.get(x).rotateY(degrees,size));
        }
        return joinedBoundingBox;
    }

    @Override
    public boolean intersectVector(Vector3f vector3f) {
        for(BoundingBox boundingBox : boundingBoxes) {
            if(boundingBox.intersectVector(vector3f)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "JoinedBoundingBox{" +
                "boundingBoxes=" + boundingBoxes +
                '}';
    }
}
