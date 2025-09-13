package dev.hilligans.engine.client.graphics.vulkan.boilerplate;

import dev.hilligans.engine.client.graphics.vulkan.VulkanEngineException;
import dev.hilligans.engine.client.graphics.vulkan.VulkanMemoryManager;
import dev.hilligans.engine.client.graphics.vulkan.VulkanWindow;
import dev.hilligans.engine.client.graphics.vulkan.boilerplate.window.VertexBufferManager;
import dev.hilligans.engine.util.NamedThreadFactory;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import static org.lwjgl.vulkan.VK10.*;

public class LogicalDevice {

    public PhysicalDevice physicalDevice;
    public VulkanInstance vulkanInstance;
    public VulkanMemoryManager memoryManager;
    public VkDevice device;
    public VulkanQueueFamilyManager queueFamilyManager;
    public VkDeviceQueueCreateInfo.Buffer buffer;
    public VertexBufferManager bufferManager = new VertexBufferManager(this);
    public final ExecutorService cleanup;
    public AtomicBoolean alive = new AtomicBoolean(true);

    public LogicalDevice(PhysicalDevice physicalDevice) {
        this.vulkanInstance = physicalDevice.vulkanInstance;
        this.physicalDevice = physicalDevice;
        this.cleanup = Executors.newSingleThreadExecutor(new NamedThreadFactory("Vulkan cleanup", physicalDevice.vulkanInstance.engine.getGameInstance()));
        memoryManager = new VulkanMemoryManager(this);
        getMemoryAllocations();

        queueFamilyManager = new VulkanQueueFamilyManager(this);

        try (MemoryStack memoryStack = MemoryStack.stackPush()) {

            PointerBuffer requiredExtensions = memoryStack.mallocPointer(1);
            requiredExtensions.put(0, memoryStack.ASCII(KHRSwapchain.VK_KHR_SWAPCHAIN_EXTENSION_NAME));

            VkDeviceCreateInfo info = VkDeviceCreateInfo.calloc(memoryStack);
            info.sType(VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO);
            buffer = VkDeviceQueueCreateInfo.calloc(queueFamilyManager.queueFamilies.size());
            int x = 0;
            for (QueueFamily queueFamily : queueFamilyManager.queueFamilies) {
                buffer.get(x).set(queueFamily.createInfo(memoryStack));
                x++;
            }

            info.pQueueCreateInfos(buffer);
            info.pEnabledFeatures(physicalDevice.deviceFeatures);
            info.ppEnabledExtensionNames(requiredExtensions);

            VkPhysicalDeviceFeatures features = VkPhysicalDeviceFeatures.calloc(memoryStack);
            features.samplerAnisotropy(true);

            info.pEnabledFeatures(features);

            PointerBuffer pp = memoryStack.callocPointer(1);
            vkCreateDevice(physicalDevice.physicalDevice, info, null, pp);
            device = new VkDevice(pp.get(0), physicalDevice.physicalDevice, info);
        }
    }

    public VulkanMemoryManager getMemoryManager() {
        return memoryManager;
    }

    public void executeMethod(Supplier<Integer> supplier, String exception) {
        int result = supplier.get();
        if(result != VK_SUCCESS) {
            if(!vulkanInstance.attemptMemoryRelocations) {
                throw new VulkanEngineException(String.format(exception, VulkanInstance.getStringErrorCode(result)));
            }
            attemptMemoryRelocation();
            result = supplier.get();
            if(result != VK_SUCCESS) {
                throw new VulkanEngineException(String.format(exception, VulkanInstance.getStringErrorCode(result)));
            }
        }
    }


    public void attemptMemoryRelocation() {

    }

    public void getMemoryAllocations() {
        /*
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkPhysicalDeviceMemoryBudgetPropertiesEXT budget = VkPhysicalDeviceMemoryBudgetPropertiesEXT.calloc(memoryStack);
            budget.sType(VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_MEMORY_BUDGET_PROPERTIES_EXT);
            VkPhysicalDeviceMemoryProperties2 properties = VkPhysicalDeviceMemoryProperties2.calloc(memoryStack);
            properties.sType(VK_STRUCTURE_TYPE_PHYSICAL_DEVICE_MEMORY_PROPERTIES_2);
            properties.pNext(budget);

            vkGetPhysicalDeviceMemoryProperties2(physicalDevice.physicalDevice, properties);

            for(int x = 0; x < budget.heapBudget().limit(); x++) {
                System.out.println("Heap Budget " + budget.heapBudget(x));
                System.out.println("Heap Usage " + budget.heapUsage(x));
            }
        }
        
         */
    }

    public VulkanBuffer allocateBuffer(int size, int usage, int properties) {
        return new VulkanBuffer(this, size, usage, properties, false);
    }

    public int findMemoryType(int filter, int properties) {
        //System.out.println("Yee");
        try(MemoryStack memoryStack = MemoryStack.stackPush()) {
            VkPhysicalDeviceMemoryProperties memProperties = VkPhysicalDeviceMemoryProperties.calloc(memoryStack);
            vkGetPhysicalDeviceMemoryProperties(physicalDevice.physicalDevice, memProperties);
            //  System.out.println("count:" + memProperties.memoryTypeCount());
            //vkGetDeviceMemoryCommitment(device.physicalDevice.physicalDevice,);

            for (int i = 0; i < memProperties.memoryTypeCount(); i++) {
                if ((filter & (1 << i)) != 0 && (memProperties.memoryTypes(i).propertyFlags() & properties) == properties) {
                    return i;
                }
            }
            vulkanInstance.exit("failed to find memory");
        }
        return -1;
    }

    public VulkanWindow createNewWindow() {
        return new VulkanWindow(physicalDevice.vulkanInstance,500,500, null).addDevice(this).addData();
    }

    public VulkanWindow getWindow() {
        return createNewWindow();
    }

    public void submitResourceForCleanup(Runnable runnable) {
        if(alive.get()) {
            synchronized (cleanup) {
                cleanup.submit(runnable);
            }
        } else {
            runnable.run();
        }
    }

    public void destroy() {
        alive.set(false);
        synchronized (cleanup) {
            cleanup.shutdown();
        }
        bufferManager.cleanup();

        try {
            if(!cleanup.awaitTermination(100, TimeUnit.SECONDS)) {
                throw new VulkanEngineException("Failed to clean up resources in time");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        vkDestroyDevice(device,null);
        buffer.free();
    }
}
