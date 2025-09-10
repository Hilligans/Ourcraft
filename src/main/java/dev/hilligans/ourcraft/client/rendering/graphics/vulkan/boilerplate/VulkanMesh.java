package dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate;

public record VulkanMesh(VulkanBuffer vertexBuffer, VulkanBuffer indexBuffer) {

    public void free() {
        vertexBuffer.free();
        indexBuffer.free();
    }
}
