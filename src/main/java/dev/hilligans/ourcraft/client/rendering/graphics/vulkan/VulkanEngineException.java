package dev.hilligans.ourcraft.client.rendering.graphics.vulkan;

public class VulkanEngineException extends RuntimeException {

    public VulkanEngineException(String reason) {
        super(reason);
    }

    public VulkanEngineException(int vkError) {
        super();
    }


    public VulkanEngineException(int vkError, String reason) {
        super(reason);
    }
}
