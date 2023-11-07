package dev.hilligans.ourcraft.client.rendering.graphics;

import dev.hilligans.ourcraft.client.rendering.VertexMesh;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferPrimitiveBuilder implements IPrimitiveBuilder {

    public FloatBuffer vertexBuffer;
    public IntBuffer indexBuffer;

    public VertexFormat vertexFormat;

    public BufferPrimitiveBuilder(int vertexBufferSize, int indexBufferSize) {
        vertexBuffer = MemoryUtil.memAllocFloat(vertexBufferSize);
        indexBuffer = MemoryUtil.memAllocInt(indexBufferSize);
    }

    @Override
    public BufferPrimitiveBuilder setVertexFormat(VertexFormat vertexFormat) {
        this.vertexFormat = vertexFormat;
        return this;
    }

    @Override
    public void ensureCapacity(int vertexBufferSize, int indexBufferSize) {
        if(this.vertexBuffer.capacity() < vertexBufferSize) {
            resizeVertexBuffer();
        }
        if(this.indexBuffer.capacity() < indexBufferSize) {
            resizeIndexBuffer();
        }
    }

    @Override
    public void ensureAddedCapacity(int vertexBufferSize, int indexBufferSize) {
        ensureCapacity(vertexBuffer.position() + vertexBufferSize, indexBuffer.position() + indexBufferSize);
    }

    @Override
    public VertexMesh toVertexMesh() {
        vertexBuffer.flip();
        indexBuffer.flip();

        return new VertexMesh(vertexFormat).addData(indexBuffer, vertexBuffer);
    }

    @Override
    public VertexMesh toUnsharedVertexMesh() {
        vertexBuffer.flip();
        indexBuffer.flip();

        FloatBuffer vertices = MemoryUtil.memDuplicate(vertexBuffer);
        IntBuffer indices = MemoryUtil.memDuplicate(indexBuffer);

        VertexMesh vertexMesh = new VertexMesh(vertexFormat).addData(indices, vertices);
        return vertexMesh.setAllocator(vertexMesh);
    }

    @Override
    public int getVertexPointer() {
        return vertexBuffer.position();
    }

    @Override
    public int getIndexPointer() {
        return indexBuffer.position();
    }

    @Override
    public void addVertexPointer(int count) {
        vertexBuffer.position(vertexBuffer.position() + count);
    }

    @Override
    public void addIndexPointer(int count) {
        indexBuffer.position(indexBuffer.position() + count);
    }

    @Override
    public void putVertex(int index, float val) {
        vertexBuffer.put(index, val);
    }

    @Override
    public void putIndex(int index, int val) {
        indexBuffer.put(index, val);
    }

    @Override
    public void putVertex(int index, float... vertices) {
        vertexBuffer.put(index, vertices);
    }

    @Override
    public void putIndex(int index, int... indices) {
        indexBuffer.put(index, indices);
    }

    @Override
    public float getFloat(int index) {
        return vertexBuffer.get(index);
    }

    @Override
    public int getInt(int index) {
        return indexBuffer.get(index);
    }

    private void resizeVertexBuffer() {
        int newWidth = vertexBuffer.capacity();
        newWidth = newWidth + (newWidth >> 1);
        MemoryUtil.memRealloc(vertexBuffer, newWidth);
    }

    private void resizeIndexBuffer() {
        int newWidth = indexBuffer.capacity();
        newWidth = newWidth + (newWidth >> 1);
        MemoryUtil.memRealloc(indexBuffer, newWidth);
    }
}
