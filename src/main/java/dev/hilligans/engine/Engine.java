package dev.hilligans.engine;

import dev.hilligans.engine.application.IApplication;
import dev.hilligans.engine.authentication.IAuthenticationScheme;
import dev.hilligans.engine.authentication.UnauthenticatedScheme;
import dev.hilligans.engine.client.graphics.*;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.api.ILayoutEngine;
import dev.hilligans.engine.client.graphics.fixedfunctiongl.FixedFunctionGLEngine;
import dev.hilligans.engine.client.graphics.nuklear.NuklearLayoutEngine;
import dev.hilligans.engine.client.graphics.opengl.OpenGLEngine;
import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine.client.graphics.tasks.BlankRenderTask;
import dev.hilligans.engine.client.graphics.tasks.EngineLoadingTask;
import dev.hilligans.engine.client.graphics.tasks.GUIRenderTask;
import dev.hilligans.engine.client.graphics.tasks.SplitWindowRenderTask;
import dev.hilligans.engine.client.graphics.vulkan.VulkanEngine;
import dev.hilligans.engine.client.input.Input;
import dev.hilligans.engine.client.input.InputHandlerProvider;
import dev.hilligans.engine.client.input.providers.ControllerHandlerProvider;
import dev.hilligans.engine.client.input.providers.KeyPressHandlerProvider;
import dev.hilligans.engine.client.input.providers.MouseHandlerProvider;
import dev.hilligans.engine.command.ICommand;
import dev.hilligans.engine.data.Tuple;
import dev.hilligans.engine.mod.handler.ModClass;
import dev.hilligans.engine.mod.handler.content.CoreExtensionView;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.mod.handler.content.RegistryView;
import dev.hilligans.engine.network.Protocol;
import dev.hilligans.engine.network.engine.INetworkEngine;
import dev.hilligans.engine.network.engine.NettyEngine;
import dev.hilligans.engine.resource.loaders.ImageLoader;
import dev.hilligans.engine.resource.loaders.JsonLoader;
import dev.hilligans.engine.resource.loaders.ResourceLoader;
import dev.hilligans.engine.resource.loaders.StringLoader;
import dev.hilligans.engine.resource.registry.loaders.RegistryLoader;
import dev.hilligans.engine.test.ITest;
import dev.hilligans.engine.util.registry.IRegistryElement;
import dev.hilligans.engine.util.registry.Registry;
import dev.hilligans.engine.client.graphics.util.Texture;

public class Engine implements ModClass {

    public static final String ENGINE_NAME = "ourcraft";

    @Override
    public void registerRegistries(RegistryView view) {
        Tuple<Class<? extends IRegistryElement>, String>[] elements = new Tuple[]{
                new Tuple(IGraphicsEngine.class, "graphics_engine"),
                new Tuple(Protocol.class, "protocol"),
                new Tuple(ResourceLoader.class, "resource_loader"),
                new Tuple(RegistryLoader.class, "registry_loader"),
                new Tuple(RenderTarget.class, "render_target"),
                new Tuple(RenderPipeline.class, "render_pipeline"),
                new Tuple(RenderTaskSource.class, "render_task"),
                new Tuple(Input.class, "key_bind"),
                new Tuple(VertexFormat.class, "vertex_format"),
                new Tuple(InputHandlerProvider.class, "input"),
                new Tuple(Texture.class, "texture"),
                new Tuple(ShaderSource.class, "shader"),
                new Tuple(ILayoutEngine.class, "layout_engine"),
                new Tuple(INetworkEngine.class, "network_engine"),
                new Tuple(ICommand.class, "command"),
                new Tuple(IAuthenticationScheme.class, "authentication_scheme"),
                new Tuple(ITest.class, "test"),
                new Tuple(IApplication.class, "application"),
        };

        for(Tuple<Class<? extends IRegistryElement>, String> element : elements) {
            view.registerRegistry(() -> new Registry<>(view.getGameInstance(), element.getTypeA(), element.getTypeB()));
        }
    }

    @Override
    public void registerCoreExtensions(CoreExtensionView view) {
        view.registerResourceLoader(new JsonLoader(), new ImageLoader(), new StringLoader());

        if (view.getGameInstance().getSide().isClient()) {
            view.registerGraphicsEngine(new VulkanEngine());
            view.registerGraphicsEngine(new OpenGLEngine());
            view.registerGraphicsEngine(new FixedFunctionGLEngine());

            view.registerVertexFormat(position_texture_color, position_color_texture, position_texture_globalColor, position_texture, position_texture_animatedWrap_shortenedColor, position_color);
            view.registerVertexFormat(position2_texture_color, position_color_lines);

            view.registerShader(new ShaderSource("world_shader", "ourcraft:position_color_texture", "Shaders/WorldVertexShader.glsl", "Shaders/WorldFragmentShader.glsl").withUniform("transform", "4fv").withUniform("color", "4f"));
            view.registerShader(new ShaderSource("position_color_shader", "ourcraft:position_color", "Shaders/WorldVertexColorShader.glsl", "Shaders/WorldFragmentShader.glsl").withUniform("transform", "4fv").withUniform("color", "4f"));
            view.registerShader(new ShaderSource("position_texture", "ourcraft:position_texture", "Shaders/PositionTexture.vsh", "Shaders/PositionTexture.fsh").withUniform("transform", "4fv").withUniform("color", "4f"));
            view.registerShader(new ShaderSource("position_color_lines_shader", "ourcraft:position_color_lines", "Shaders/WorldVertexColorShader.glsl", "Shaders/WorldFragmentColorShader.glsl").withUniform("transform", "4fv").withUniform("color", "4f"));
            view.registerShader(new ShaderSource("nk_shader", "position2_texture_color", "Shaders/NkVertexShader.glsl", "Shaders/NkFragmentShader.glsl").withUniform("transform", "4fv"));

            view.registerRenderPipelines(new RenderPipeline("engine_loading_pipeline"));

            view.registerRenderTarget(new RenderTarget("engine_loading_target", "ourcraft:engine_loading_pipeline", "ourcraft:engine_loading_task")
                    .setPipelineState(new PipelineState()));

            view.registerRenderTask(new EngineLoadingTask());

            view.registerInputHandlerProviders(new ControllerHandlerProvider(), new KeyPressHandlerProvider(), new MouseHandlerProvider());

            view.registerLayoutEngine(new NuklearLayoutEngine());

            view.registerRenderTask(new GUIRenderTask());
            view.registerRenderTask(new BlankRenderTask());
        }
    }

    @Override
    public void registerContent(ModContainer container) {
        container.registerAuthenticationScheme(new UnauthenticatedScheme());
        container.registerNetworkEngine(new NettyEngine());

        if(container.getGameInstance().getSide().isClient()) {
            container.registerRenderTask(new SplitWindowRenderTask());
        }
    }

    public static final VertexFormat position_texture_color = new VertexFormat("ourcraft", "position_texture_color", VertexFormat.TRIANGLES)
            .addPart("position", VertexFormat.FLOAT,3)
            .addPart("texture", VertexFormat.FLOAT, 2)
            .addPart("color", VertexFormat.FLOAT, 4);

    public static final VertexFormat position_color_texture = new VertexFormat("ourcraft", "position_color_texture", VertexFormat.TRIANGLES)
            .addPart("position", VertexFormat.FLOAT,3)
            .addPart("color", VertexFormat.FLOAT, 4)
            .addPart("texture", VertexFormat.FLOAT, 2);

    public static final VertexFormat position2_texture_color = new VertexFormat("ourcraft", "position2_texture_color", VertexFormat.TRIANGLES)
            .addPart("position2", VertexFormat.FLOAT, 2)
            .addPart("texture", VertexFormat.FLOAT, 2)
            .addPart("color", VertexFormat.FLOAT, 4);

    public static final VertexFormat position_texture_globalColor = new VertexFormat("oucraft", "position_texture_globalColor", VertexFormat.TRIANGLES)
            .addPart("position", VertexFormat.FLOAT, 3)
            .addPart("texture", VertexFormat.FLOAT, 2)
            .addPart("globalColor", VertexFormat.UNSIGNED_INT, 1);

    public static final VertexFormat position_texture = new VertexFormat("ourcraft", "position_texture", VertexFormat.TRIANGLES)
            .addPart("position", VertexFormat.FLOAT,3)
            .addPart("texture", VertexFormat.FLOAT, 2);

    public static final VertexFormat position_texture_animatedWrap_shortenedColor = new VertexFormat("ourcraft", "position_texture_animatedWrap_shortenedColor", VertexFormat.TRIANGLES)
            .addPart("position", VertexFormat.FLOAT, 3)
            .addPart("texture", VertexFormat.FLOAT, 2)
            .addPart("textureWrap", VertexFormat.UNSIGNED_BYTE,1)
            .addPart("globalColor", VertexFormat.UNSIGNED_INT, 1);

    public static final VertexFormat position_color = new VertexFormat("ourcraft", "position_color", VertexFormat.TRIANGLES)
            .addPart("position", VertexFormat.FLOAT, 3)
            .addPart("color", VertexFormat.FLOAT, 4);

    public static final VertexFormat position_color_lines = new VertexFormat("ourcraft", "position_color_lines", VertexFormat.LINES)
            .addPart("position", VertexFormat.FLOAT, 3)
            .addPart("color", VertexFormat.FLOAT, 4);

    public static final VertexFormat position_RGB = new VertexFormat("ourcraft", "position_RGB", VertexFormat.TRIANGLES)
            .addPart("position", VertexFormat.FLOAT, 3)
            .addPart("color", VertexFormat.FLOAT, 3);


    @Override
    public String getModID() {
        return ENGINE_NAME;
    }
}
