package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Pipeline;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.VulkanWindow;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkFramebufferCreateInfo;

import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class FrameBuffer {

    public VulkanWindow vulkanWindow;
    public VkFramebufferCreateInfo framebufferCreateInfo;
    public long frameBuffer;

    public FrameBuffer(VulkanWindow vulkanWindow, int index) {
        this.vulkanWindow = vulkanWindow;
        framebufferCreateInfo = VkFramebufferCreateInfo.calloc();
        framebufferCreateInfo.sType(VK_STRUCTURE_TYPE_FRAMEBUFFER_CREATE_INFO);
        framebufferCreateInfo.renderPass(vulkanWindow.renderPass.renderPass);
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            LongBuffer longBuffer = memoryStack.longs(vulkanWindow.imageView.imageViews.get(index));
            framebufferCreateInfo.pAttachments(longBuffer);
            framebufferCreateInfo.width(vulkanWindow.vulkanWidth);
            framebufferCreateInfo.height(vulkanWindow.vulkanHeight);
            framebufferCreateInfo.layers(1);
            LongBuffer pos = memoryStack.mallocLong(1);
            if (vkCreateFramebuffer(vulkanWindow.device.device, framebufferCreateInfo, null, pos) != VK_SUCCESS) {
                vulkanWindow.device.vulkanInstance.exit("failed to create frame buffer");
            }
            frameBuffer = pos.get(0);
        }
    }

    public void cleanup() {
        vkDestroyFramebuffer(vulkanWindow.device.device,frameBuffer,null);
    }
}
