package dev.hilligans.ourcraft.Client.Rendering.Graphics;

import dev.hilligans.ourcraft.Client.Rendering.VertexMesh;

public interface IPrimitiveBuilder {

    IPrimitiveBuilder setVertexFormat(VertexFormat vertexFormat);

    void ensureCapacity(int vertexBufferSize, int indexBufferSize);

    void ensureAddedCapacity(int vertexBufferSize, int indexBufferSize);

    VertexMesh toVertexMesh();

    VertexMesh toUnsharedVertexMesh();

    int getVertexPointer();

    int getIndexPointer();

    void addVertexPointer(int count);

    void addIndexPointer(int count);

    void putVertex(int index, float val);

    void putIndex(int index, int val);

    default void putIndex(int index, short val) {
        putIndex(index, (int)val);
    }

    void putVertex(int index, float... vertices);

    void putIndex(int index, int... indices);

    float getFloat(int index);

    int getInt(int index);

    default short getShort(int index) {
        return (short) getInt(index);
    }

    default void add(float... floats) {
        putVertex(getVertexPointer(), floats);
        putIndex(getIndexPointer(), getVertexPointer());
    }

    default void addMinus(float... floats) {
        putVertex(getVertexPointer(), floats);
    }

    default void add(float[] vertices, int[] indices) {
        putVertex(getVertexPointer(), vertices);
        putIndex(getIndexPointer(), indices);
    }
}
