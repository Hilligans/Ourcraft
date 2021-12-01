package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.VulkanInstance;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.VulkanProperties;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.Resource.LWJGLResourceProvider;
import dev.Hilligans.ourcraft.Resource.ResourceProvider;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.ClientWorld;

import java.awt.image.BufferedImage;

public class VulkanEngine implements IGraphicsEngine<VulkanGraphicsContainer> {

    VulkanInstance vulkanInstance;

    @Override
    public RenderWindow createWindow() {
        return null;
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
    public void render(RenderWindow window) {

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
        VulkanProperties vulkanProperties = new VulkanProperties();
        vulkanProperties.warningValidation().errorValidation().addValidationLayers("VK_LAYER_KHRONOS_validation", "VK_LAYER_KHRONOS_validation").enableValidationLayers();
        vulkanInstance = new VulkanInstance(vulkanProperties);
        vulkanInstance.run();
    }

    @Override
    public void close() {

    }

    @Override
    public GameInstance getGameInstance() {
        return null;
    }
}
