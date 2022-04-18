package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IDefaultEngineImpl;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.PipelineState;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.VertexFormat;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window.VulkanWindow;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import dev.Hilligans.ourcraft.Client.Rendering.Texture;
import dev.Hilligans.ourcraft.Client.Rendering.VertexMesh;

import java.nio.ByteBuffer;

public class VulkanDefaultImpl implements IDefaultEngineImpl<VulkanWindow> {

    @Override
    public void drawMesh(VulkanWindow window, MatrixStack matrixStack, int texture, int program, int meshID, long indicesIndex, int length) {

    }

    @Override
    public int createMesh(VulkanWindow window, VertexMesh mesh) {
        return 0;
    }

    @Override
    public void destroyMesh(VulkanWindow window, int mesh) {

    }

    @Override
    public int createTexture(VulkanWindow window, ByteBuffer buffer, int width, int height, int format) {
        return 0;
    }

    @Override
    public void destroyTexture(VulkanWindow window, int texture) {

    }

    @Override
    public void drawAndDestroyMesh(VulkanWindow window, MatrixStack matrixStack, VertexMesh mesh, int texture, int program) {

    }

    @Override
    public void setState(VulkanWindow window, PipelineState state) {

    }

}
