package dev.hilligans.ourcraft.Data.Other;

import org.joml.Vector2d;
import org.joml.Vector2f;

import java.util.ArrayList;

public class JoinedRectangleBB extends RectangleBB {

    ArrayList<RectangleBB> boundingBoxes = new ArrayList<>();

    public JoinedRectangleBB(float minX, float minY, float maxX, float maxY) {
        super(minX, minY, maxX, maxY);
        boundingBoxes.add(new RectangleBB(minX,minY,maxX,maxY));
    }

    public JoinedRectangleBB(float[] vals) {
        super(vals);
        boundingBoxes.add(new RectangleBB(vals));
    }

    public JoinedRectangleBB addBox(RectangleBB boundingBox) {
        boundingBoxes.add(boundingBox);
        return this;
    }

    public RectangleBB addBox(float minX, float minY, float maxX, float maxY) {
        boundingBoxes.add(new RectangleBB(minX,minY,maxX,maxY));
        return this;
    }

    @Override
    public boolean intersectsBox(RectangleBB other, Vector2d myPos, Vector2d otherPos) {
        for(RectangleBB boundingBox : boundingBoxes) {
            if(boundingBox.intersectsBox(other,myPos,otherPos)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean intersectVector(Vector2f vector2f) {
        for(RectangleBB boundingBox : boundingBoxes) {
            if(boundingBox.intersectVector(vector2f)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public JoinedRectangleBB duplicate() {
        JoinedRectangleBB joinedBoundingBox = new JoinedRectangleBB(minX,minY,maxX,maxY);
        for(int x = 1; x < boundingBoxes.size(); x++) {
            joinedBoundingBox.addBox(boundingBoxes.get(x).duplicate());
        }
        return joinedBoundingBox;
    }

    @Override
    public String toString() {
        return "JoinedBoundingBox{" +
                "boundingBoxes=" + boundingBoxes +
                '}';
    }
}
