package dev.hilligans.ourcraft.mod.handler.content;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.client.input.InputHandlerProvider;
import dev.hilligans.ourcraft.client.rendering.graphics.*;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.graphics.api.ILayoutEngine;
import dev.hilligans.ourcraft.resource.registry.loaders.RegistryLoader;

public class CoreExtensionView implements RegistrationView {

    private final ModContainer container;

    public CoreExtensionView(ModContainer container) {
        this.container = container;
    }

    public void registerRegistryLoader(RegistryLoader... registryLoaders) {
        container.register("registry_loader", registryLoaders);
    }

    public void registerGraphicsEngine(IGraphicsEngine<?,?,?>... graphicsEngines) {
        container.register("graphics_engine", graphicsEngines);
    }

    public void registerRenderTarget(RenderTarget... renderTargets) {
        container.register("render_target", renderTargets);
    }

    public void registerRenderPipelines(RenderPipeline... renderPipelines) {
        container.register("render_pipeline", renderPipelines);
    }

    public void registerRenderTask(RenderTaskSource... renderTasks) {
        container.register("render_task", renderTasks);
    }

    public void registerVertexFormat(VertexFormat... vertexFormats) {
        container.register("vertex_format", vertexFormats);
    }

    public void registerShader(ShaderSource... shaderSources) {
        container.register("shader_source", shaderSources);
    }

    public void registerInputHandlerProviders(InputHandlerProvider... providers) {
        container.register("input", providers);
    }

    public void registerLayoutEngine(ILayoutEngine<?>... layoutEngines) {
        container.register("layout_engine", layoutEngines);
    }

    @Override
    public ModContent getModContent() {
        return null;
    }

    @Override
    public GameInstance getGameInstance() {
        return container.gameInstance;
    }
}
