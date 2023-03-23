package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsEngineBase;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.VulkanInstance;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.VulkanProperties;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Util.Logger;

import java.util.ArrayList;

public class VulkanEngine extends GraphicsEngineBase<VulkanWindow, VulkanDefaultImpl, VulkanGraphicsContext> {

    public VulkanInstance vulkanInstance;
    public VulkanDefaultImpl impl;
    public Client client;

    @Override
    public VulkanWindow createWindow() {
        return vulkanInstance.getDefaultDevice().logicalDevice.getWindow();
    }

    @Override
    public void render(VulkanWindow window) {
        long currentTime = System.nanoTime();
        if(currentTime - Client.timeSinceLastDraw < Client.drawTime) {
            //System.out.println("Returning");
            //return;
        }
        window.frameTracker.count();
        Client.timeSinceLastDraw = currentTime;

        window.camera.tick();
        MatrixStack matrixStack = window.camera.getMatrix();
        MatrixStack screenStack = window.camera.getScreenStack();

        int index = window.windowRenderer.waitForNextFrame();

        window.context.setBufferInUse(index);
        window.context.startRecording();
        window.renderPipeline.render(client, matrixStack, screenStack, window.context);
        window.context.endRecording();

        window.windowRenderer.render(window.context.getBuffer());
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
        gameInstance.build(this, vulkanInstance.vulkanWindow.context);
        impl.compilePipelines(vulkanInstance.vulkanWindow.renderPass, vulkanInstance.vulkanWindow.viewport);
        //vulkanInstance.run();
        return vulkanInstance.vulkanWindow;
    }

    @Override
    public void close() {
        for(VulkanWindow window : windows) {
            window.close();
        }
        vulkanInstance.cleanup();
        //vulkanInstance.exit("closing");
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
        return getVulkanInstance(new VulkanProperties(ClientMain.argumentContainer).warningValidation().errorValidation().addValidationLayers("VK_LAYER_KHRONOS_validation").verboseValidation().enableValidationLayers());
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
    public String getIdentifierName() {
        return "ourcraft:vulkanEngine";
    }

    @Override
    public String getUniqueName() {
        return "graphicsEngine.ourcraft.vulkanEngine";
    }
}
