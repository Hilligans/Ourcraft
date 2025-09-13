package dev.hilligans.engine.client.graphics.vulkan.boilerplate.window;

import dev.hilligans.engine.client.graphics.vulkan.boilerplate.LogicalDevice;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.pipeline.VertexBuffer;
import dev.hilligans.engine.data.Tuple;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.lwjgl.vulkan.VK10.vkDestroyBuffer;
import static org.lwjgl.vulkan.VK10.vkFreeMemory;

public class VertexBufferManager {

    public LogicalDevice device;
    public ArrayList<VertexBuffer> buffers = new ArrayList<>();
    public ConcurrentLinkedQueue<Tuple<Long,Long>> toDestroy = new ConcurrentLinkedQueue<>();

    public VertexBufferManager(LogicalDevice device) {
        this.device = device;
    }

    public VertexBuffer createBuffer(float[] vertices) {
        //VertexBuffer vertexBuffer = new VertexBuffer(device);
        return null;
    }

    public void cleanup() {
        for(Tuple<Long,Long> buffer : toDestroy) {
            vkDestroyBuffer(device.device,buffer.typeA,null);
            vkFreeMemory(device.device, buffer.typeB, null);
        }
    }

    public void tick() {
        for(Tuple<Long,Long> buffer : toDestroy) {
            vkDestroyBuffer(device.device,buffer.typeA,null);
            vkFreeMemory(device.device, buffer.typeB, null);
        }
    }
}