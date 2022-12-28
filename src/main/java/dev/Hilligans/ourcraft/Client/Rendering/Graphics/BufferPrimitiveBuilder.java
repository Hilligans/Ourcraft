package dev.Hilligans.ourcraft.Client.Rendering.Graphics;

import dev.Hilligans.ourcraft.Client.Rendering.VertexMesh;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferPrimitiveBuilder implements IPrimitiveBuilder {

    public FloatBuffer vertexBuffer;
    public IntBuffer indexBuffer;

    public int vertexBufferPosition = 0;
    public int indexBufferPosition = 0;

    public VertexFormat vertexFormat;

    public BufferPrimitiveBuilder(int vertexBufferSize, int indexBufferSize) {
        vertexBuffer = MemoryUtil.memAllocFloat(vertexBufferSize);
        indexBuffer = MemoryUtil.memAllocInt(indexBufferSize);
    }

    @Override
    public void setVertexFormat(VertexFormat vertexFormat) {
        this.vertexFormat = vertexFormat;
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
        ensureCapacity(vertexBufferPosition + vertexBufferSize, indexBufferPosition + indexBufferSize);
    }

    @Override
    public VertexMesh toVertexMesh() {
        vertexBuffer.position(vertexBufferPosition);
        indexBuffer.position(indexBufferPosition);
        return new VertexMesh(vertexFormat).addData(indexBuffer, vertexBuffer);
    }

    @Override
    public int getVertexPointer() {
        return vertexBufferPosition;
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
