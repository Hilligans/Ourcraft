package dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan;

import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsEngineBase;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.VulkanInstance;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.VulkanProperties;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.VulkanWindow;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Util.Logger;

import java.util.ArrayList;

public class VulkanEngine extends GraphicsEngineBase<VulkanWindow, VulkanDefaultImpl, VulkanGraphicsContext> {

    public VulkanInstance vulkanInstance;
    public VulkanDefaultImpl impl;

    @Override
    public VulkanWindow createWindow() {
        return vulkanInstance.getDefaultDevice().logicalDevice.getWindow();
    }

    @Override
    public void render(VulkanWindow window) {

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
        vulkanInstance.run();
        return vulkanInstance.vulkanWindow;
    }

    @Override
    public void close() {
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
