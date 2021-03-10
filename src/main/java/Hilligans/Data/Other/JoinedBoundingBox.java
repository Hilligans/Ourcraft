package Hilligans.Data.Other;

import org.joml.Vector3f;

import java.util.ArrayList;

public class JoinedBoundingBox extends BoundingBox {

    ArrayList<BoundingBox> boundingBoxes = new ArrayList<>();

    public JoinedBoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        super(minX,minY,minZ,maxX,maxY,maxZ);
        boundingBoxes.add(new BoundingBox(minX,minY,minZ,maxX,maxY,maxZ));
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
    public boolean intersectsBox(BoundingBox other, Vector3f myPos, Vector3f otherPos) {
        for(BoundingBox boundingBox : boundingBoxes) {
            if(boundingBox.intersectsBox(other,myPos,otherPos)) {
                return true;
            }
        }
        return false;
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
}
