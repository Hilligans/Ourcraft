package dev.hilligans.ourcraft.client.rendering.graphics;

public interface IPrimitiveBuilder {


    IPrimitiveBuilder setVertexFormat(VertexFormat vertexFormat);

    void ensureCapacity(int vertexBufferSize, int indexBufferSize);

    int getVertexPointer();

    int getIndexPointer();

    void putIndex(int index, int val);

    void putVertex(int index, float... vertices);

    void putIndex(int index, int... indices);

    int getInt(int index);

    default void add(float... floats) {
        putVertex(getVertexPointer(), floats);
        putIndex(getIndexPointer(), getVertexPointer());
    }

    default void add(float[] vertices, int[] indices) {
        putVertex(getVertexPointer(), vertices);
        putIndex(getIndexPointer(), indices);
    }
}
