package dev.hilligans.engine.client.graphics.api;

import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine.resource.IBufferAllocator;
import dev.hilligans.engine.util.registry.IRegistryElement;

import java.nio.ByteBuffer;

public interface IMeshOptimizer extends IRegistryElement {

    long optimize(VertexFormat vertexFormat, ByteBuffer vertices, ByteBuffer indices, IBufferAllocator allocator);

    default String getResourceType() {
        return "mesh_optimizer";
    }
}
