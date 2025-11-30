package dev.hilligans.engine.mod.handler.content;

import dev.hilligans.engine.Engine;
import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.application.IApplication;
import dev.hilligans.engine.client.graphics.*;
import dev.hilligans.engine.client.graphics.api.IMeshOptimizer;
import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine.client.graphics.api.ITextureConverter;
import dev.hilligans.engine.client.input.Input;
import dev.hilligans.engine.client.input.InputHandlerProvider;
import dev.hilligans.engine.client.graphics.util.Texture;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.api.ILayoutEngine;
import dev.hilligans.engine.resource.loaders.ResourceLoader;
import dev.hilligans.engine.schema.Schema;
import dev.hilligans.engine.template.ITemplate;

public class CoreExtensionView implements RegistrationView {

    private final ModContainer container;

    public CoreExtensionView(ModContainer container) {
        this.container = container;
    }

    public void registerGraphicsEngine(IGraphicsEngine<?,?,?>... graphicsEngines) {
        container.registerCore(Engine.name("graphics_engine"), graphicsEngines);
    }

    public void registerRenderTarget(RenderTarget... renderTargets) {
        container.registerCore(Engine.name("render_target"), renderTargets);
    }

    public void registerRenderPipelines(RenderPipeline... renderPipelines) {
        container.registerCore(Engine.name("render_pipeline"), renderPipelines);
    }

    public void registerRenderTask(RenderTaskSource... renderTasks) {
        container.registerCore(Engine.name("render_task"), renderTasks);
    }

    public void registerTextureConverter(ITextureConverter... textureConverters) {
        container.registerCore(Engine.name("texture_converter"), textureConverters);
    }

    public void registerMeshOptimizer(IMeshOptimizer... meshOptimizers) {
        container.registerCore(Engine.name("mesh_optimizer"), meshOptimizers);
    }

    public void registerVertexFormat(VertexFormat... vertexFormats) {
        container.registerCore(Engine.name("vertex_format"), vertexFormats);
    }

    public void registerShader(ShaderSource... shaderSources) {
        container.registerCore(Engine.name("shader"), shaderSources);
    }

    public void registerInputHandlerProviders(InputHandlerProvider... providers) {
        container.registerCore(Engine.name("input"), providers);
    }

    public void registerLayoutEngine(ILayoutEngine<?>... layoutEngines) {
        container.registerCore(Engine.name("layout_engine"), layoutEngines);
    }

    public void registerResourceLoader(ResourceLoader<?>... resourceLoaders) {
        container.registerCore(Engine.name("resource_loader"), resourceLoaders);
    }

    public void registerApplication(IApplication... applications) {
        container.registerCore(Engine.name("application"), applications);
    }

    public void registerTemplate(ITemplate<?>... templates) {
        container.registerCore(Engine.name("template"), templates);
    }

    public void registerSchema(Schema<?>... schemas) {
        container.registerCore(Engine.name("schema"), schemas);
    }

    public void registerKeybinds(Input... inputs) {
        container.registerCore(Engine.name("key_bind"), inputs);
    }

    public void registerTexture(Texture... textures) {
        container.registerCore(Engine.name("texture"), textures);
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
