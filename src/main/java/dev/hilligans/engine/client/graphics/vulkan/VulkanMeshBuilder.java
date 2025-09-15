package dev.hilligans.engine.client.graphics.vulkan;

import dev.hilligans.engine.client.graphics.resource.VertexMesh;
import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine.client.graphics.api.IMeshBuilder;
import dev.hilligans.engine.client.graphics.vulkan.api.IVulkanMemoryManager;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.VulkanBuffer;
import dev.hilligans.engine.resource.IAllocator;
import dev.hilligans.engine.resource.IBufferAllocator;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.nio.ByteBuffer;

public class VulkanMeshBuilder implements IMeshBuilder, IAllocator<VertexMesh> {

    public VulkanMemoryManager memoryAllocator;
    public IVulkanMemoryManager memoryManager;

    VertexFormat format;

    FloatArrayList vertices = new FloatArrayList();
    IntArrayList indices = new IntArrayList();

    float[] vertexArray;
    int[] indexArray;

    ByteBuffer vertexBuffer;
    ByteBuffer indexBuffer;

    public VulkanMeshBuilder(VertexFormat format, VulkanMemoryManager memoryAllocator, IVulkanMemoryManager memoryManager) {
        this.format = format;
        this.memoryAllocator = memoryAllocator;
        this.memoryManager = memoryManager;
    }

    @Override
    public void setData(float[] vertices, int[] indices) {
        this.vertexArray = vertices;
        this.indexArray = indices;
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
        } else if(vertexArray != null) {
            return vertexArray.length/(format.getStride()/Float.BYTES);
        }
        return vertices.size()/(format.getStride()/ Float.BYTES);
    }

    @Override
    public int getIndexCount() {
        if(indexBuffer != null) {
            return indexBuffer.capacity()/Float.BYTES;
        } else if(indexArray != null) {
            return indexArray.length/(format.getStride()/Float.BYTES);
        }

        return indices.size();
    }

    @Override
    public long getVertexSize() {
        if(vertices != null) {
            return (long) vertices.size() * Float.BYTES;
        } else if(vertexBuffer != null) {
            return vertexBuffer.capacity();
        }

        return (long) vertexArray.length * Float.BYTES;
    }

    @Override
    public long getIndexSize() {
        if(indices != null) {
            return (long) indices.size() * Integer.BYTES;
        } else if(indexBuffer != null) {
            return indexBuffer.capacity();
        }

        return (long) indexArray.length * Integer.BYTES;
    }

    @Override
    public VertexFormat getFormat() {
        return format;
    }

    public void writeToBuffers(VulkanBuffer vertices, VulkanBuffer indices) {
        if(this.vertices != null) {
            vertices.write(this.vertices.elements(), this.vertices.size());
        } else if(this.vertexArray != null) {
            vertices.write(this.vertexArray);
        } else if(this.vertexBuffer != null) {
            vertices.write(this.vertexBuffer);
        }

        if(this.indices != null) {
            indices.write(this.indices.elements(), this.indices.size());
        } else if(this.indexArray != null) {
            indices.write(this.indexArray);
        } else if(this.indexBuffer != null) {
            indices.write(this.indexBuffer);
        }
    }

    @Override
    public void free(VertexMesh resource) {

    }
}
