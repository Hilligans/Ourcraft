package dev.hilligans.engine.client.graphics.api;

import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine.data.BoundingBox;
import dev.hilligans.engine.resource.IBufferAllocator;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

public interface IMeshBuilder {

    /* The direct set methods, note, you cannot add more vertices once these have been called */
    void setData(float[] vertices, int[] indices);
    void setData(ByteBuffer vertices, ByteBuffer indices);
    IBufferAllocator vertexAllocator();
    IBufferAllocator indexAllocator();

    /* builder methods */
    void addVertices(float... vertices);
    void addIndices(int... indices);

    void transform(float dx, int strideIndex);

    int getVertexCount();
    int getIndexCount();

    long getVertexSize();
    long getIndexSize();

    VertexFormat getFormat();

    default void addBoundingBox(BoundingBox boundingBox, Consumer<IMeshBuilder> extraData) {
        int s = getVertexCount();

        this.addVertices(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        extraData.accept(this);
        this.addVertices(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        extraData.accept(this);
        this.addVertices(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        extraData.accept(this);
        this.addVertices(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        extraData.accept(this);
        this.addVertices(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        extraData.accept(this);
        this.addVertices(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        extraData.accept(this);
        this.addVertices(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        extraData.accept(this);
        this.addVertices(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        extraData.accept(this);

        this.addIndices(s + 0, s + 1, s + 1, s + 2, s + 2, s + 3, s + 3, s + 0, s + 0, s + 4, s + 1, s + 5, s + 2, s + 6, s + 3, s + 7, s + 4, s + 5, s + 5, s + 6, s + 6, s + 7, s + 7, s + 4);
    }

    default void addQuad(float minX, float minY, float minTexX, float minTexY, float maxX, float maxY, float maxTexX, float maxTexY, float z) {
        int s = getVertexCount();

        this.addVertices(
                minX, minY, z, minTexX, minTexY,
                minX, maxY, z, minTexX, maxTexY,
                maxX, minY, z, maxTexX, minTexY,
                maxX, maxY, z, maxTexX, maxTexY);

        this.addClockwiseIndices(s);
    }

    default void addClockwiseIndices(int base) {
        this.addIndices(base, base + 1, base + 2, base + 3, base + 2, base + 1);
    }

    default void addCounterClockwiseIndices(int base) {
        this.addIndices(base, base + 2, base + 1, base + 3, base + 1, base + 2);
    }
}
