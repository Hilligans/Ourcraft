package dev.Hilligans.ourcraft.ModHandler.Content;

import dev.Hilligans.ourcraft.Client.Input.InputHandlerProvider;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.*;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.Resource.RegistryLoaders.RegistryLoader;

import java.util.Arrays;

public class CoreExtensionView implements RegistrationView {

    private final ModContent modContent;

    public CoreExtensionView(ModContent modContent) {
        this.modContent = modContent;
    }

    public void registerRegistryLoader(RegistryLoader... registryLoaders) {
        modContent.registerRegistryLoader(registryLoaders);
    }

    public void registerGraphicsEngine(IGraphicsEngine<?,?,?>... graphicsEngines) {
        modContent.registerGraphicsEngine(graphicsEngines);
    }

    public void registerRenderTarget(RenderTarget... renderTargets) {
        modContent.registerRenderTarget(renderTargets);
    }

    public void registerRenderPipelines(RenderPipeline... renderPipelines) {
        modContent.registerRenderPipelines(renderPipelines);
    }

    public void registerRenderTask(RenderTaskSource... renderTasks) {
        modContent.registerRenderTask(renderTasks);
    }

    public void registerVertexFormat(VertexFormat... vertexFormats) {
        modContent.registerVertexFormat(vertexFormats);
    }

    public void registerShader(ShaderSource... shaderSources) {
       modContent.registerShader(shaderSources);
    }

    public void registerInputHandlerProviders(InputHandlerProvider... providers) {
        modContent.registerInputHandlerProviders(providers);
    }

    @Override
    public ModContent getModContent() {
        return modContent;
    }

    @Override
    public GameInstance getGameInstance() {
        return modContent.getGameInstance();
    }
}
