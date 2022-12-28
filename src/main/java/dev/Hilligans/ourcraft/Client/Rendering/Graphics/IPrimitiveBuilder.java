package dev.Hilligans.ourcraft.Client.Rendering.Graphics;

import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.PrimitiveBuilder;
import dev.Hilligans.ourcraft.Client.Rendering.VertexMesh;

import java.nio.ByteBuffer;

public interface IPrimitiveBuilder {

    void setVertexFormat(VertexFormat vertexFormat);

    void ensureCapacity(int vertexBufferSize, int indexBufferSize);

    void ensureAddedCapacity(int vertexBufferSize, int indexBufferSize);

    VertexMesh toVertexMesh();

    int getVertexPointer();

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
/*A
    default void add(float... floats) {
        count += floats.length;
        vertices.add(floats);
        indices.add(getVertexPointer());
        size++;
    }

    default void addMinus(float... floats) {
        count += floats.length;
        vertices.add(floats);
        size++;
    }

    default void add(float[] vertices, int[] indices) {
        count += vertices.length;
        this.vertices.add(vertices);
        this.indices.add(indices);
        size++;
    }


 */



}
