package dev.hilligans.ourcraft.client.rendering.graphics.vulkan;

import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsEngineBase;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.SingleUseCommandBuffer;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.VulkanInstance;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.boilerplate.VulkanProperties;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.util.Logger;

import java.util.ArrayList;

import static org.lwjgl.vulkan.VK10.vkEnumerateInstanceExtensionProperties;

public class VulkanEngine extends GraphicsEngineBase<VulkanWindow, VulkanDefaultImpl, VulkanBaseGraphicsContext> {

    public VulkanInstance vulkanInstance;
    public VulkanDefaultImpl impl;
    public Client client;

    @Override
    public VulkanWindow createWindow() {
        return vulkanInstance.getDefaultDevice().logicalDevice.getWindow();
    }

    @Override
    public void render(RenderWindow window, GraphicsContext graphicsContext) {
        long currentTime = System.nanoTime();
        if(currentTime - Client.timeSinceLastDraw < Client.drawTime) {
            //System.out.println("Returning");
            //return;
        }

        vulkanInstance.logicalDevice.getMemoryAllocations();
        window.frameTracker.count();
        Client.timeSinceLastDraw = currentTime;

        window.camera.tick();
        MatrixStack matrixStack = window.camera.getMatrix();
        MatrixStack screenStack = window.camera.getScreenStack();

       // int index = window.windowRenderer.waitForNextFrame();

      //  window.context.setBufferInUse(index);
       // window.context.startRecording();
      //  window.renderPipeline.render(client, matrixStack, screenStack, window.context);
       // window.context.endRecording();

       // window.windowRenderer.render(window.renderPool.queue, window.context.getBuffer());
        //window.renderPipeline.render(client,matrixStack,screenStack, new GraphicsContext());
    }

    @Override
    public void renderScreen(MatrixStack screenStack) {

    }

    @Override
    public VulkanWindow setup() {
        impl = new VulkanDefaultImpl(this);
        vulkanInstance = getVulkanInstance();
        vulkanInstance.setGraphicsEngine(this);
        vulkanInstance.setUp();
        vulkanInstance.vulkanWindow.setup();
        this.windows.add(vulkanInstance.vulkanWindow);

        SingleUseCommandBuffer buf = vulkanInstance.logicalDevice.queueFamilyManager.getSingleCommandPool(true, false, true, false);
        VulkanBaseGraphicsContext graphicsContext = new TransferVulkanContext(buf.getBuffer(), vulkanInstance.logicalDevice, vulkanInstance.vulkanWindow);
        gameInstance.build(this, graphicsContext);
        buf.endAndSubmit(graphicsContext.getCommandBuffer().onCompletion);


        impl.compilePipelines(vulkanInstance.vulkanWindow.renderPass, vulkanInstance.vulkanWindow.viewport);
        //vulkanInstance.run();
        return vulkanInstance.vulkanWindow;
    }

    @Override
    public void close() {
        gameInstance.cleanupGraphics(this, null);
        for(VulkanWindow window : windows) {
            window.close();
        }
        impl.cleanup();
        vulkanInstance.cleanup();
        //vulkanInstance.exit("closing");
    }

    @Override
    public VulkanBaseGraphicsContext getContext() {
        return null;
    }

    @Override
    public ArrayList<VulkanWindow> getWindows() {
        return null;
    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public GraphicsContext getGraphicsContext() {
        return null;
    }

    @Override
    public boolean isCompatible() {
        return true;
    }

    @Override
    public VulkanDefaultImpl getDefaultImpl() {
        return impl;
    }

    @Override
    public int getProgram(String name) {
        return 0;
    }

    public static VulkanInstance getVulkanInstance() {
        return getVulkanInstance(new VulkanProperties(Ourcraft.getArgumentContainer()).warningValidation().errorValidation().addValidationLayers("VK_LAYER_KHRONOS_validation").verboseValidation().enableValidationLayers());
    }

    public static VulkanInstance getVulkanInstance(VulkanProperties vulkanProperties) {
        if(sInstance == null) {
            sInstance = new VulkanInstance(vulkanProperties);
        }
        return sInstance;
    }

    public static VulkanInstance sInstance;

    @Override
    public String getResourceName() {
        return "vulkanEngine";
    }

    @Override
    public String getResourceOwner() {
        return "ourcraft";
    }
}
