package dev.hilligans.ourcraft.mod.handler.content;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.application.IApplication;
import dev.hilligans.ourcraft.client.input.Input;
import dev.hilligans.ourcraft.client.input.InputHandlerProvider;
import dev.hilligans.ourcraft.client.rendering.Texture;
import dev.hilligans.ourcraft.client.rendering.graphics.*;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.graphics.api.ILayoutEngine;
import dev.hilligans.ourcraft.resource.loaders.ResourceLoader;
import dev.hilligans.ourcraft.resource.registry.loaders.RegistryLoader;

public class CoreExtensionView implements RegistrationView {

    private final ModContainer container;

    public CoreExtensionView(ModContainer container) {
        this.container = container;
    }

    public void registerRegistryLoader(RegistryLoader... registryLoaders) {
        container.registerCore("ourcraft:registry_loader", registryLoaders);
    }

    public void registerGraphicsEngine(IGraphicsEngine<?,?,?>... graphicsEngines) {
        container.registerCore("ourcraft:graphics_engine", graphicsEngines);
    }

    public void registerRenderTarget(RenderTarget... renderTargets) {
        container.registerCore("ourcraft:render_target", renderTargets);
    }

    public void registerRenderPipelines(RenderPipeline... renderPipelines) {
        container.registerCore("ourcraft:render_pipeline", renderPipelines);
    }

    public void registerRenderTask(RenderTaskSource... renderTasks) {
        container.registerCore("ourcraft:render_task", renderTasks);
    }

    public void registerVertexFormat(VertexFormat... vertexFormats) {
        container.registerCore("ourcraft:vertex_format", vertexFormats);
    }

    public void registerShader(ShaderSource... shaderSources) {
        container.registerCore("ourcraft:shader", shaderSources);
    }

    public void registerInputHandlerProviders(InputHandlerProvider... providers) {
        container.registerCore("ourcraft:input", providers);
    }

    public void registerLayoutEngine(ILayoutEngine<?>... layoutEngines) {
        container.registerCore("ourcraft:layout_engine", layoutEngines);
    }

    public void registerResourceLoader(ResourceLoader<?>... resourceLoaders) {
        container.registerCore("ourcraft:resource_loader", resourceLoaders);
    }

    public void registerApplication(IApplication... applications) {
        container.registerCore("ourcraft:application", applications);
    }

    public void registerKeybinds(Input... inputs) {
        container.registerCore("ourcraft:key_bind", inputs);
    }

    public void registerTexture(Texture... textures) {
        container.registerCore("ourcraft:texture", textures);
    }

    @Override
    public ModContainer getModContainer() {
        return container;
    }

    @Override
    public GameInstance getGameInstance() {
        return container.gameInstance;
    }
}
