package dev.hilligans.engine.client.graphics.vulkan.boilerplate.window;

import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.LogicalDevice;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.vulkan.*;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class Shader {

    public LogicalDevice device;
    public long shader;
    public VkPipelineShaderStageCreateInfo shaderCreateInfo;
    public VkPipelineVertexInputStateCreateInfo stateCreateInfo;
    public VkVertexInputBindingDescription bindingDescription;

    public Shader(LogicalDevice device, ByteBuffer shader, int bit) {
        this.device = device;
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkShaderModuleCreateInfo createInfo = VkShaderModuleCreateInfo.calloc(memoryStack);
            createInfo.sType(VK_STRUCTURE_TYPE_SHADER_MODULE_CREATE_INFO);

            createInfo.pCode(shader);
            LongBuffer longBuffer = memoryStack.mallocLong(1);
            vkCreateShaderModule(device.device, createInfo, null, longBuffer);
            this.shader = longBuffer.get(0);

            shaderCreateInfo = VkPipelineShaderStageCreateInfo.calloc();
            shaderCreateInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_SHADER_STAGE_CREATE_INFO);
            shaderCreateInfo.stage(bit);
            shaderCreateInfo.module(this.shader);
            shaderCreateInfo.pName(MemoryUtil.memUTF8("main"));


            stateCreateInfo = VkPipelineVertexInputStateCreateInfo.calloc();
            stateCreateInfo.sType(VK_STRUCTURE_TYPE_PIPELINE_VERTEX_INPUT_STATE_CREATE_INFO);
        }
    }

    public void set(VertexFormat vertexFormat) {
        bindingDescription = VkVertexInputBindingDescription.calloc();
        bindingDescription.binding(0);
        bindingDescription.stride(vertexFormat.getStride());
        bindingDescription.inputRate(VK_VERTEX_INPUT_RATE_VERTEX);

        VkVertexInputAttributeDescription.Buffer parts = VkVertexInputAttributeDescription.calloc(vertexFormat.parts.size());

        for(int x = 0; x < vertexFormat.parts.size(); x++) {
            VertexFormat.VertexPart part = vertexFormat.parts.get(x);
            VkVertexInputAttributeDescription description = VkVertexInputAttributeDescription.calloc();
            description.binding(0).location(x);
            description.format(getFormat(part.primitiveType, part.primitiveCount));
            description.offset(part.offset);

            parts.put(x, description);
        }
        //try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            stateCreateInfo.pVertexBindingDescriptions(VkVertexInputBindingDescription.calloc(1).put(0, bindingDescription));
            stateCreateInfo.pVertexAttributeDescriptions(parts);
        //}
    }

    public static int getFormat(int type, int length) {
        if(type == VertexFormat.FLOAT) {
            return switch (length) {
                case 1 -> VK_FORMAT_R32_SFLOAT;
                case 2 -> VK_FORMAT_R32G32_SFLOAT;
                case 3 -> VK_FORMAT_R32G32B32_SFLOAT;
                case 4 -> VK_FORMAT_R32G32B32A32_SFLOAT;
                default -> throw new IllegalStateException("Unexpected value: " + length);
            };
        }
        throw new IllegalStateException("Unexpected type: " + type);
    }

    public void freeInit() {
       // stateCreateInfo.pVertexBindingDescriptions().free();
        VkVertexInputBindingDescription.Buffer b;
        if((b = stateCreateInfo.pVertexBindingDescriptions()) != null) {
            b.free();
        }
        stateCreateInfo.free();
        if(bindingDescription != null) {
            bindingDescription.free();
        }

        MemoryUtil.memFree(shaderCreateInfo.pName());
        shaderCreateInfo.free();
    }

    public void free() {
        freeInit();
        vkDestroyShaderModule(device.device, shader,null);
    }
}
