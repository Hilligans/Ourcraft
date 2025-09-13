package dev.hilligans.engine.client.graphics.vulkan.boilerplate;

public record VulkanMesh(VulkanBuffer vertexBuffer, VulkanBuffer indexBuffer) {

    public void free() {
        vertexBuffer.free();
        indexBuffer.free();
    }
}
