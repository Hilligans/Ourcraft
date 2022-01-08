package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.VulkanInstance;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.VulkanProperties;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.VulkanWindow;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.Resource.LWJGLResourceProvider;
import dev.Hilligans.ourcraft.Resource.ResourceProvider;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.ClientWorld;

import java.awt.image.BufferedImage;

public class VulkanEngine implements IGraphicsEngine<VulkanGraphicsContainer, VulkanWindow> {

    public VulkanInstance vulkanInstance;
    public GameInstance gameInstance;

    public VulkanEngine(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    @Override
    public VulkanWindow createWindow() {
        return vulkanInstance.getDefaultDevice().logicalDevice.getWindow();
    }

    @Override
    public VulkanGraphicsContainer getChunkGraphicsContainer(Chunk chunk) {
        return null;
    }

    @Override
    public VulkanGraphicsContainer createChunkGraphicsContainer() {
        return null;
    }

    @Override
    public void putChunkGraphicsContainer(Chunk chunk, VulkanGraphicsContainer container) {

    }

    @Override
    public void render(VulkanWindow window) {

    }

    @Override
    public void renderWorld(MatrixStack matrixStack, ClientWorld world) {

    }

    @Override
    public void renderScreen(MatrixStack screenStack) {

    }

    @Override
    public ResourceProvider createResourceProvider() {
        return new LWJGLResourceProvider();
    }

    @Override
    public void setup() {
        vulkanInstance = getVulkanInstance();
        vulkanInstance.run();
    }

    @Override
    public void close() {
        vulkanInstance.exit("closing");
    }

    @Override
    public GameInstance getGameInstance() {
        return gameInstance;
    }

    public static VulkanInstance getVulkanInstance() {
        return getVulkanInstance(new VulkanProperties(ClientMain.argumentContainer).warningValidation().errorValidation().addValidationLayers("VK_LAYER_KHRONOS_validation", "VK_LAYER_KHRONOS_validation").enableValidationLayers());
    }

    public static VulkanInstance getVulkanInstance(VulkanProperties vulkanProperties) {
        if(sInstance == null) {
            sInstance = new VulkanInstance(vulkanProperties);
        }
        return sInstance;
    }

    public static VulkanInstance sInstance;
}
