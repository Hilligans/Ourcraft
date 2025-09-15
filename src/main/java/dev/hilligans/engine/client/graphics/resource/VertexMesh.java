package dev.hilligans.engine.client.graphics.resource;

import dev.hilligans.engine.resource.IAllocator;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class VertexMesh implements IAllocator<VertexMesh> {

    /**
     * one of these have to be not null
     */
    public VertexFormat vertexFormat;
    public String vertexFormatName;

    public IntBuffer indices;
    public FloatBuffer vertices;
    public int elementSize = 4;
    public IAllocator<VertexMesh> allocator;


    public VertexMesh(String vertexFormatName) {
        this.vertexFormatName = vertexFormatName;
    }

    public VertexMesh(VertexFormat vertexFormat) {
        this.vertexFormat = vertexFormat;
    }

    public VertexMesh addData(IntBuffer indices, FloatBuffer vertices) {
        this.indices = indices;
        this.vertices = vertices;
        return this;
    }

    public VertexMesh addData(int[] indices, float[] vertices) {
        this.vertices = MemoryUtil.memAllocFloat(vertices.length).put(vertices).flip();
        this.indices = MemoryUtil.memAllocInt(indices.length).put(indices).flip();
        allocator = this;
        return this;
    }

    public VertexMesh addData(ByteBuffer indices, ByteBuffer vertices) {
        this.vertices = vertices.asFloatBuffer();
        this.indices = indices.asIntBuffer();
        return this;
    }

    public VertexMesh setAllocator(IAllocator<VertexMesh> allocator) {
        this.allocator = allocator;
        return this;
    }

    public void destroy() {
        if(allocator != null) {
            allocator.free(this);
        }
    }

    public VertexMesh setVertexFormat(String format) {
        this.vertexFormatName = format;
        return this;
    }


    @Override
    public void free(VertexMesh resource) {
        MemoryUtil.memFree(resource.vertices);
        MemoryUtil.memFree(resource.indices);
    }
}

