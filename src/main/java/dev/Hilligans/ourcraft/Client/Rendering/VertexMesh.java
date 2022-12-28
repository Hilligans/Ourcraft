package dev.Hilligans.ourcraft.Client.Rendering;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.VertexFormat;
import dev.Hilligans.ourcraft.Resource.EmptyAllocator;
import dev.Hilligans.ourcraft.Resource.IAllocator;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class VertexMesh implements IAllocator<VertexMesh> {

    /**
     * one of these have to be not null
     */
    public VertexFormat vertexFormat;
    public String vertexFormatName;

    public ArrayList<UniformComponent> uniformComponents = new ArrayList<>();

    //public IntBuffer indices;
    // public FloatBuffer vertices;

    public IntBuffer indices;
    public FloatBuffer vertices;
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

    public VertexMesh addUniform(Matrix4f matrix4f) {

        return this;
    }

    public VertexMesh addUniform(ByteBuffer byteBuffer) {

        return this;
    }

    @Override
    public void free(VertexMesh resource) {
        MemoryUtil.memFree(resource.vertices);
        MemoryUtil.memFree(resource.indices);
    }

    static class UniformComponent {

        public String name;

        public UniformComponent(String name) {
            this.name = name;
        }

    }
}

