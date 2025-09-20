package dev.hilligans.engine.mod.handler.content;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.application.IApplication;
import dev.hilligans.engine.client.graphics.*;
import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine.client.input.Input;
import dev.hilligans.engine.client.input.InputHandlerProvider;
import dev.hilligans.engine.client.graphics.util.Texture;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.api.ILayoutEngine;
import dev.hilligans.engine.resource.loaders.ResourceLoader;
import dev.hilligans.engine.resource.registry.loaders.RegistryLoader;

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
