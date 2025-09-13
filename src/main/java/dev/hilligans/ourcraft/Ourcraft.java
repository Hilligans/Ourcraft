package dev.hilligans.ourcraft;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.application.IApplication;
import dev.hilligans.engine.client.graphics.*;
import dev.hilligans.engine.client.graphics.tasks.EngineLoadingTask;
import dev.hilligans.engine.client.graphics.tasks.GUIRenderTask;
import dev.hilligans.engine.client.graphics.tasks.SplitWindowRenderTask;
import dev.hilligans.ourcraft.biome.Biome;
import dev.hilligans.ourcraft.biome.Biomes;
import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.block.Blocks;
import dev.hilligans.ourcraft.client.ChatWindow;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.audio.SoundBuffer;
import dev.hilligans.ourcraft.client.audio.SoundCategory;
import dev.hilligans.ourcraft.client.audio.Sounds;
import dev.hilligans.engine.client.input.Input;
import dev.hilligans.engine.client.input.InputHandlerProvider;
import dev.hilligans.engine.client.input.RepeatingInput;
import dev.hilligans.engine.client.input.providers.ControllerHandlerProvider;
import dev.hilligans.engine.client.input.providers.KeyPressHandlerProvider;
import dev.hilligans.engine.client.input.providers.MouseHandlerProvider;
import dev.hilligans.engine.client.input.handlers.MouseHandler;
import dev.hilligans.engine.client.input.key.KeyHandler;
import dev.hilligans.ourcraft.client.rendering.ScreenBuilder;
import dev.hilligans.ourcraft.client.rendering.Texture;
import dev.hilligans.ourcraft.client.rendering.Textures;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.api.ILayoutEngine;
import dev.hilligans.engine.client.graphics.fixedfunctiongl.FixedFunctionGLEngine;
import dev.hilligans.engine.client.graphics.implementations.WorldCamera;
import dev.hilligans.engine.client.graphics.nuklear.NuklearLayoutEngine;
import dev.hilligans.engine.client.graphics.opengl.OpenGLEngine;
import dev.hilligans.ourcraft.client.rendering.graphics.*;
import dev.hilligans.engine.client.graphics.vulkan.VulkanEngine;
import dev.hilligans.ourcraft.client.rendering.screens.*;
import dev.hilligans.ourcraft.command.Commands;
import dev.hilligans.engine.command.ICommand;
import dev.hilligans.ourcraft.data.descriptors.Tag;
import dev.hilligans.engine.data.Tuple;
import dev.hilligans.ourcraft.entity.Entities;
import dev.hilligans.ourcraft.entity.EntityType;
import dev.hilligans.ourcraft.item.Items;
import dev.hilligans.ourcraft.item.data.ToolLevel;
import dev.hilligans.engine.mod.handler.Identifier;
import dev.hilligans.engine.mod.handler.ModClass;
import dev.hilligans.engine.mod.handler.content.CoreExtensionView;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.mod.handler.content.RegistryView;
import dev.hilligans.engine.network.Protocol;
import dev.hilligans.ourcraft.network.Protocols;
import dev.hilligans.engine.network.engine.INetworkEngine;
import dev.hilligans.engine.network.engine.NettyEngine;
import dev.hilligans.ourcraft.recipe.IRecipe;
import dev.hilligans.ourcraft.recipe.helper.RecipeView;
import dev.hilligans.engine.resource.ResourceManager;
import dev.hilligans.engine.resource.loaders.ImageLoader;
import dev.hilligans.engine.resource.loaders.JsonLoader;
import dev.hilligans.engine.resource.loaders.ResourceLoader;
import dev.hilligans.engine.resource.loaders.StringLoader;
import dev.hilligans.engine.resource.registry.loaders.JsonRegistryLoader;
import dev.hilligans.engine.resource.registry.loaders.RegistryLoader;
import dev.hilligans.ourcraft.schematic.LitematicaSchematicLoader;
import dev.hilligans.engine.authentication.IAuthenticationScheme;
import dev.hilligans.engine.authentication.UnauthenticatedScheme;
import dev.hilligans.ourcraft.settings.Setting;
import dev.hilligans.engine.test.ITest;
import dev.hilligans.engine.test.standard.DuplicateRegistryTest;
import dev.hilligans.engine.util.argument.ArgumentContainer;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import dev.hilligans.ourcraft.util.registry.Registry;
import dev.hilligans.ourcraft.world.Feature;
import dev.hilligans.ourcraft.world.newworldsystem.ChainedBlockChunkStream;
import dev.hilligans.ourcraft.world.newworldsystem.ChunkStream;
import org.json.JSONArray;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.nio.DoubleBuffer;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.glfw.GLFW.*;

public class Ourcraft extends ModClass {

    public static final GameInstance GAME_INSTANCE = new GameInstance();
    public static ArgumentContainer argumentContainer;
    public static final ArgumentContainer getArgumentContainer() {
        return argumentContainer;
    }
    public static String path = System.getProperty("user.dir");

    public static String hashString(String password, String salt) {
        return new String(BCrypt.withDefaults().hash(12,"abcdefghjklmmopq".getBytes(), (password + salt).getBytes()), StandardCharsets.UTF_8);
    }

    public static long getTime() {
        return System.nanoTime();
    }

    public static String getConvertedTime(long time) {
        return String.format("%2.2fms", time/1000000f);
    }

    public static synchronized ResourceManager getResourceManager() {
        return GAME_INSTANCE.RESOURCE_MANAGER;
    }

    public static File getFile(String path) {
        return new File(Ourcraft.path + "/" + path);
    }


    @Override
    public String getModID() {
        return "ourcraft";
    }

    @Override
    public void registerRegistries(RegistryView view) {
        Tuple<Class<? extends IRegistryElement>, String>[] elements = new Tuple[]{
                new Tuple(Block.class, "block"),
                new Tuple(Items.class, "item"),
                new Tuple(Biome.class, "biome"),
                new Tuple(Tag.class, "tag"),
                new Tuple(IRecipe.class, "recipe"),
                new Tuple(RecipeView.class, "recipe_view"),
                new Tuple(IGraphicsEngine.class, "graphics_engine"),
                new Tuple(Protocol.class, "protocol"),
                new Tuple(Setting.class, "setting"),
                new Tuple(ResourceLoader.class, "resource_loader"),
                new Tuple(SoundBuffer.class, "sound"),
                new Tuple(ToolLevel.class, "tool_level"),
                new Tuple(RegistryLoader.class, "registry_loader"),
                new Tuple(ScreenBuilder.class, "screen"),
                new Tuple(Feature.class, "feature"),
                new Tuple(RenderTarget.class, "render_target"),
                new Tuple(RenderPipeline.class, "render_pipeline"),
                new Tuple(RenderTaskSource.class, "render_task"),
                new Tuple(Input.class, "key_bind"),
                new Tuple(VertexFormat.class, "vertex_format"),
                new Tuple(InputHandlerProvider.class, "input"),
                new Tuple(Texture.class, "texture"),
                new Tuple(ShaderSource.class, "shader"),
                new Tuple(ILayoutEngine.class, "layout_engine"),
                new Tuple(SoundCategory.class, "sound_category"),
                new Tuple(EntityType.class, "entity_type"),
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

    public void registerCoreExtensions(CoreExtensionView view) {
        view.registerResourceLoader(new JsonLoader(), new ImageLoader(), new StringLoader());

        view.registerRegistryLoader(new JsonRegistryLoader(new Identifier("tool_tiers", "ourcraft"), "Data/ToolTiers.json", (modContent1, jsonObject, string) -> {
            JSONArray elements = jsonObject.getJSONArray("material");
            ToolLevel[] levels = new ToolLevel[elements.length()];
            for (int x = 0; x < elements.length(); x++) {
                levels[x] = new ToolLevel(new Identifier(elements.getString(x), modContent1.getModID()));
            }
            String[] parts = jsonObject.getString("location").split(" ");
            switch (parts[0]) {
                case "start" -> modContent1.gameInstance.MATERIAL_LIST.addToStart(levels);
                case "end" -> modContent1.gameInstance.MATERIAL_LIST.addToEnd(levels);
                case "before" -> modContent1.gameInstance.MATERIAL_LIST.insertBefore(parts[1], levels);
                case "after" -> modContent1.gameInstance.MATERIAL_LIST.insertAfter(parts[1], levels);
            }
            view.getGameInstance().registerToolLevels(levels);
        }).rerunOnInstanceClear());

        view.registerRegistryLoader(new JsonRegistryLoader(new Identifier("blocks", "ourcraft"), "Data/Blocks.json", (modContent12, jsonObject, key) -> {
            try {
                Block block = new Block(key, "Data/" + jsonObject.optString("data"), jsonObject.optJSONObject("overrides"));
                JSONArray textures = jsonObject.getJSONArray("textures");
                for (int x = 0; x < textures.length(); x++) {
                    block.blockProperties.addTexture(textures.getString(x), x, textures.length());
                }
                modContent12.registerBlock(block);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));


        view.registerRegistryLoader(new JsonRegistryLoader(new Identifier("screens", "ourcraft"), "Data/Screens.json", (modContent12, jsonObject, key) -> {
            modContent12.registerScreenBuilder(new ScreenBuilder(key, jsonObject));
        }).rerunOnInstanceClear());

        if (view.getGameInstance().side.isClient()) {
            Textures.addData(view);

            view.registerGraphicsEngine(new VulkanEngine());
            view.registerGraphicsEngine(new OpenGLEngine());
            view.registerGraphicsEngine(new FixedFunctionGLEngine());

            view.registerVertexFormat(position_texture_color, position_color_texture, position_texture_globalColor, position_texture, position_texture_animatedWrap_shortenedColor, position_color);
            view.registerVertexFormat(position2_texture_color, position_color_lines);

//            view.registerShader(new ShaderSource("world_shader","ourcraft:position_texture_color", "Shaders/WorldVertexShader.glsl","Shaders/WorldFragmentShader.glsl"));
            view.registerShader(new ShaderSource("world_shader", "ourcraft:position_color_texture", "Shaders/WorldVertexShader.glsl", "Shaders/WorldFragmentShader.glsl").withUniform("transform", "4fv").withUniform("color", "4f"));
            view.registerShader(new ShaderSource("position_color_shader", "ourcraft:position_color", "Shaders/WorldVertexColorShader.glsl", "Shaders/WorldFragmentShader.glsl").withUniform("transform", "4fv").withUniform("color", "4f"));
            view.registerShader(new ShaderSource("position_texture", "ourcraft:position_texture", "Shaders/PositionTexture.vsh", "Shaders/PositionTexture.fsh").withUniform("transform", "4fv").withUniform("color", "4f"));
            view.registerShader(new ShaderSource("position_color_lines_shader", "ourcraft:position_color_lines", "Shaders/WorldVertexColorShader.glsl", "Shaders/WorldFragmentColorShader.glsl").withUniform("transform", "4fv").withUniform("color", "4f"));
            view.registerShader(new ShaderSource("nk_shader", "position2_texture_color", "Shaders/NkVertexShader.glsl", "Shaders/NkFragmentShader.glsl").withUniform("transform", "4fv"));

            view.registerRenderPipelines(new RenderPipeline("engine_loading_pipeline"));

            view.registerRenderTarget(new RenderTarget("engine_loading_target", "ourcraft:engine_loading_pipeline")
                    .setPipelineState(new PipelineState()));

            view.registerRenderTask(new EngineLoadingTask());

            view.registerInputHandlerProviders(new ControllerHandlerProvider(), new KeyPressHandlerProvider(), new MouseHandlerProvider());

            view.registerLayoutEngine(new NuklearLayoutEngine());

            if (view.getGameInstance().side.isClient()) {
                view.registerKeybinds(new Input("ourcraft:mouse_handler::0") {
                    @Override
                    public void press(RenderWindow renderWindow, float strength) {
                        Client client = renderWindow.getClient();
                        if (client.screen != null) {
                            DoubleBuffer doubleBuffer = getMousePos(renderWindow.getWindowID());
                            client.screen.mouseClick((int) doubleBuffer.get(0), (int) doubleBuffer.get(1), GLFW.GLFW_MOUSE_BUTTON_1);
                        }
                    }
                });

                view.registerKeybinds(new Input("ourcraft:key_press_handler::" + KeyHandler.GLFW_KEY_ESCAPE) {
                    @Override
                    public void press(RenderWindow renderWindow, float strength) {
                        Client client = renderWindow.getClient();
                        if (client.renderWorld) {
                            if (client.screen == null) {
                                client.openScreen(new EscapeScreen());
                            } else {
                                client.closeScreen();
                            }
                        } else {
                            client.openScreen(new JoinScreen());
                        }
                    }
                });

                view.registerKeybinds(new Input("ourcraft:key_press_handler::" + KeyHandler.GLFW_KEY_F3) {
                    @Override
                    public void press(RenderWindow renderWindow, float strength) {
                        Client client = renderWindow.getClient();
                        client.playerData.f3 = !client.playerData.f3;
                    }
                }.onlyWithPipelines("ourcraft:new_world_pipeline"));

                view.registerKeybinds(new Input("ourcraft:mouse_handler::" + MouseHandler.MOUSE_X) {
                    @Override
                    public void press(RenderWindow window, float strength) {
                        window.getCamera().addRotation(0, strength/400);
                        window.setMousePosition(window.getWindowWidth()/2, window.getWindowHeight()/2);
                        //GLFW.glfwSetCursorPos(window.getWindowID(), window.getWindowWidth()/2,window.getWindowHeight()/2);
                    }
                }.onlyWithPipelines("ourcraft:new_world_pipeline"));

                view.registerKeybinds(new Input("ourcraft:mouse_handler::" + MouseHandler.MOUSE_Y) {
                    @Override
                    public void press(RenderWindow window, float strength) {
                        window.getCamera().addRotation(-strength/400,0);
                        window.setMousePosition(window.getWindowWidth()/2, window.getWindowHeight()/2);
                        //GLFW.glfwSetCursorPos(window.getWindowID(), window.getWindowWidth()/2,window.getWindowHeight()/2);
                    }
                }.onlyWithPipelines("ourcraft:new_world_pipeline"));

                view.registerKeybinds(new Input("ourcraft:key_press_handler::" + GLFW_KEY_SEMICOLON) {
                    @Override
                    public void press(RenderWindow renderWindow, float strength) {
                        super.press(renderWindow, strength);
                        renderWindow.client.openScreen(new TestScreen());
                    }
                });

                view.registerKeybinds(new Input("ourcraft:key_press_handler::" + GLFW_KEY_F8){
                    @Override
                    public void press(RenderWindow renderWindow, float strength) {
                        super.press(renderWindow, strength);
                        renderWindow.getClient().getPlayerData().debugChunkRendering = !renderWindow.getClient().getPlayerData().debugChunkRendering;
                    }
                });

                view.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_W,
                        (window, strength) -> window.getCamera().moveForward(5f * strength)).onlyWithPipelines("ourcraft:new_world_pipeline"));

                view.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_A,
                        (window, strength) -> window.getCamera().moveLeft(5f * strength)).onlyWithPipelines("ourcraft:new_world_pipeline"));

                view.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_S,
                        (window, strength) -> window.getCamera().moveBackward(5f * strength)).onlyWithPipelines("ourcraft:new_world_pipeline"));

                view.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_D,
                        (window, strength) -> window.getCamera().moveRight(5f * strength)).onlyWithPipelines("ourcraft:new_world_pipeline"));

                view.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_SPACE,
                        (window, strength) -> window.getCamera().moveUp(5f * strength)).onlyWithPipelines("ourcraft:new_world_pipeline"));

                view.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_LEFT_SHIFT,
                        (window, strength) -> window.getCamera().moveUp(-5f * strength)).onlyWithPipelines("ourcraft:new_world_pipeline"));

                view.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_DOWN,
                        (window, strength) -> window.getCamera().addRotation(0.1f * strength, 0)));

                view.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_UP,
                        (window, strength) -> window.getCamera().addRotation(-0.1f * strength, 0)));

                view.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_LEFT,
                        (window, strength) -> window.getCamera().addRotation(0, -0.3f * strength)));

                view.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_RIGHT,
                        (window, strength) -> window.getCamera().addRotation(0, 0.3f * strength)));

                view.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_L,
                        (window, strength) -> ((WorldCamera)window.getCamera()).roll -= 0.1f * strength));

                view.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_O,
                        (window, strength) -> ((WorldCamera)window.getCamera()).roll += 0.1f * strength));


                view.registerKeybinds(new Input("ourcraft:key_press_handler::" + GLFW_KEY_H) {
                    @Override
                    public void press(RenderWindow renderWindow, float strength) {
                        Client client = renderWindow.getClient();
                        client.openScreen(new TagEditorScreen());
                    }
                });

                view.registerKeybinds(new Input("ourcraft:key_press_handler::" + GLFW_KEY_P) {
                    @Override
                    public void press(RenderWindow renderWindow, float strength) {
                        Client client = renderWindow.getClient();
                        client.openScreen(new FrameTimeScreen());
                    }
                });

                view.registerKeybinds(new Input("ourcraft:key_press_handler::" + GLFW_KEY_T) {
                    @Override
                    public void press(RenderWindow renderWindow, float strength) {
                        Client client = renderWindow.getClient();
                        if (client.renderWorld) {
                            client.openScreen(new ChatWindow());
                        }
                    }
                });
            }
            view.registerApplication(new Client(view.getGameInstance(), view.getGameInstance().getArgumentContainer()));
        }
    }

    @Override
    public void registerContent(ModContainer modContent) {
        chainedChunkStream.assignOwner(modContent);
        modContent.registerResourceLoader(new LitematicaSchematicLoader());

        modContent.registerTest(new DuplicateRegistryTest());

        if(modContent.getGameInstance().getSide().isClient()) {
            modContent.registerRenderTask(new GUIRenderTask());
            modContent.registerRenderTask(new WorldRenderTask());
            modContent.registerRenderTask(new WorldTransparentRenderTask());
            modContent.registerRenderTask(new ChatRenderTask());
            modContent.registerRenderTask(new SplitWindowRenderTask());
            modContent.registerRenderTask(new ChunkDebugRenderTask());

            modContent.registerRenderPipelines(new RenderPipeline("new_world_pipeline"));
            modContent.registerRenderTarget(new RenderTarget("debug_world_renderer", "ourcraft:new_world_pipeline")
                    .setPipelineState(new PipelineState().setDepth(false)));
            modContent.registerRenderTarget(new RenderTarget("new_solid_world_renderer", "ourcraft:new_world_pipeline").afterTarget("debug_world_renderer", "ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            modContent.registerRenderTarget(new RenderTarget("entity_renderer", "ourcraft:new_world_pipeline").afterTarget("new_solid_world_renderer", "ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            modContent.registerRenderTarget(new RenderTarget("particle_renderer", "ourcraft:new_world_pipeline").afterTarget("entity_renderer", "ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            modContent.registerRenderTarget(new RenderTarget("translucent_world_renderer", "ourcraft:new_world_pipeline").afterTarget("particle_renderer", "ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            modContent.registerRenderTarget(new RenderTarget("chat_renderer", "ourcraft:new_world_pipeline").afterTarget("translucent_world_renderer", "ourcraft")
                    .setPipelineState(new PipelineState()));
            modContent.registerRenderTarget(new RenderTarget("gui_renderer", "ourcraft:new_world_pipeline").afterTarget("chat_renderer", "ourcraft")
                    .setPipelineState(new PipelineState()));


            modContent.registerRenderPipelines(new RenderPipeline("menu_pipeline"));
            modContent.registerRenderPipelines(new RenderPipeline("split_window_pipeline"));


            modContent.registerRenderTarget(new RenderTarget("gui_renderer", "ourcraft:menu_pipeline")
                    .setPipelineState(new PipelineState()));

            modContent.registerRenderTarget(new RenderTarget("split_window_renderer", "ourcraft:split_window_pipeline")
                    .setPipelineState(new PipelineState()));
        }

        modContent.registerNetworkEngine(new NettyEngine());



        modContent.registerBlocks(Blocks.AIR, Blocks.STONE, Blocks.DIRT, Blocks.GRASS, Blocks.BEDROCK, Blocks.IRON_ORE, Blocks.LEAVES, Blocks.LOG, Blocks.SAND, Blocks.CACTUS, Blocks.CHEST, Blocks.STAIR_BLOCK, Blocks.GRASS_PLANT, Blocks.WEEPING_VINE, Blocks.MAPLE_LOG, Blocks.MAPLE_PLANKS, Blocks.PINE_LOG, Blocks.PINE_PLANKS, Blocks.SPRUCE_LOG, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_LOG, Blocks.BIRCH_PLANKS, Blocks.OAK_LOG, Blocks.OAK_PLANKS, Blocks.WILLOW_LOG, Blocks.WILLOW_PLANKS, Blocks.ACACIA_LOG, Blocks.ACACIA_PLANKS, Blocks.POPLAR_LOG, Blocks.POPLAR_PLANKS, Blocks.ELM_LOG, Blocks.ELM_WOOD, Blocks.PALM_LOG, Blocks.PALM_WOOD, Blocks.REDWOOD_LOG, Blocks.REDWOOD_WOOD, Blocks.SAPLING);
        modContent.registerBlock(Blocks.RED);
        modContent.registerBlock(Blocks.WATER);
        modContent.registerBiome(Biomes.PLAINS,Biomes.SANDY_HILLS,Biomes.DESERT,Biomes.FOREST);

        modContent.registerEntityType(Entities.PLAYER);


        Sounds.reg();
        modContent.registerSounds(Sounds.BLOCK_BREAK, Sounds.MUSIC);
        //modContent.registerTexture(Textures.TEXTURES.toArray(new Texture[0]));
        Protocols.register(modContent);
        Commands.register(modContent);

        modContent.registerAuthenticationScheme(new UnauthenticatedScheme());
    }

    public static DoubleBuffer getMousePos(long window) {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(window, x, y);
        return BufferUtils.createDoubleBuffer(2).put(x.get()).put(y.get());
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

    public static ChunkStream chainedChunkStream = new ChainedBlockChunkStream("");
}
