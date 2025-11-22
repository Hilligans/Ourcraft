package dev.hilligans.engine.client.graphics.api;

import java.nio.ByteBuffer;

public interface IModel {

    default ByteBuffer getVertices() {return null;}
    default ByteBuffer getIndices() {return null;}

    default IMeshBuilder build(IDefaultEngineImpl<?, ?, ?> impl) { return null; }

    float[] getVertices(int side);
    int[] getIndices(int side);

    default float[] getVertices(int side, int rotX, int rotY) {
        return getVertices(side);
    }
    default int[] getIndices(int side, int rotX, int rotY) {
        return getIndices(side);
    }

    String getModel();
    String getPath();


}
