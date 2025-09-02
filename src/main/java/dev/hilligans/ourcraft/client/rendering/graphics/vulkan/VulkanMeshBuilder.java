package dev.hilligans.ourcraft.client.rendering.graphics.vulkan;

import dev.hilligans.ourcraft.client.rendering.VertexMesh;
import dev.hilligans.ourcraft.client.rendering.graphics.VertexFormat;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IMeshBuilder;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.VulkanBuffer;
import dev.hilligans.ourcraft.resource.IAllocator;
import dev.hilligans.ourcraft.resource.IBufferAllocator;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public class VulkanMeshBuilder implements IMeshBuilder, IAllocator<VertexMesh> {

    public VulkanMemoryManager memoryAllocator;

    VertexFormat format;

    FloatArrayList vertices = new FloatArrayList();
    IntArrayList indices = new IntArrayList();

    //VulkanBuffer vertexBuffer;
    //VulkanBuffer indexBuffer;

    ByteBuffer vertexBuffer;
    ByteBuffer indexBuffer;

    public VulkanMeshBuilder(VertexFormat format, VulkanMemoryManager memoryAllocator) {
        this.format = format;
        this.memoryAllocator = memoryAllocator;
    }

    @Override
    public void setData(float[] vertices, int[] indices) {
        vertexBuffer = MemoryUtil.memAlloc(vertices.length * 4);
        indexBuffer = MemoryUtil.memAlloc(indices.length * 4);

        vertexBuffer.asFloatBuffer().put(vertices);
        indexBuffer.asIntBuffer().put(indices);
    }

    @Override
    public void setData(ByteBuffer vertices, ByteBuffer indices) {
        this.vertexBuffer = vertices;
        this.indexBuffer = indices;
    }

    @Override
    public IBufferAllocator vertexAllocator() {
        return null;
    }

    @Override
    public IBufferAllocator indexAllocator() {
        return null;
    }

    @Override
    public void addVertices(float... vertices) {
        this.vertices.addElements(this.vertices.size(), vertices);
    }

    @Override
    public void addIndices(int... indices) {
        this.indices.addElements(this.indices.size(), indices);
    }

    @Override
    public void transform(float dx, int strideIndex) {
        int stride = format.getStride()/Float.BYTES;
        for(int x = strideIndex; x < vertices.size(); x += stride) {
            vertices.set(x, vertices.getFloat(x) + dx);
        }
    }

    @Override
    public int getVertexCount() {
        if(vertexBuffer != null) {
            return vertexBuffer.capacity()/format.getStride();
        }
        return vertices.size()/(format.getStride()/4);
    }

    @Override
    public int getIndexCount() {
        if(indexBuffer != null) {
            return indexBuffer.capacity()/4;
        }

        return indices.size();
    }

    @Override
    public VertexMesh build() {
        //memoryAllocator.allocateBuffer()

        if(vertexBuffer == null) {
            vertexBuffer = MemoryUtil.memAlloc(vertices.size() * 4);
            vertexBuffer.asFloatBuffer().put(vertices.elements(), 0, vertices.size());
        }
        if(indexBuffer == null) {
            indexBuffer = MemoryUtil.memAlloc(indices.size() * 4);
            indexBuffer.asIntBuffer().put(indices.elements(), 0, indices.size());
        }

        this.vertices = null;
        this.indices = null;

        VertexMesh mesh = new VertexMesh(format);
        mesh.addData(indexBuffer, vertexBuffer);
        mesh.setAllocator(this);

        return mesh;
    }

    @Override
    public VertexFormat getFormat() {
        return format;
    }

    @Override
    public void free(VertexMesh resource) {
        MemoryUtil.memFree(vertexBuffer);
        MemoryUtil.memFree(indexBuffer);
    }
}
