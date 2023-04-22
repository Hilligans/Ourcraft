package dev.hilligans.ourcraft;

import dev.hilligans.ourcraft.Biome.Biomes;
import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.Client.Audio.Sounds;
import dev.hilligans.ourcraft.Client.ChatWindow;
import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.Input.HandlerProviders.ControllerHandlerProvider;
import dev.hilligans.ourcraft.Client.Input.HandlerProviders.KeyPressHandlerProvider;
import dev.hilligans.ourcraft.Client.Input.HandlerProviders.MouseHandlerProvider;
import dev.hilligans.ourcraft.Client.Input.Input;
import dev.hilligans.ourcraft.Client.Input.RepeatingInput;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.FixedFunctionGL.FixedFunctionGLEngine;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Implementations.WorldCamera;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.OpenGL.OpenGLEngine;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Tasks.GUIRenderTask;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Tasks.WorldRenderTask;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Tasks.WorldTransparentRenderTask;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.VulkanEngine;
import dev.hilligans.ourcraft.Client.Rendering.ScreenBuilder;
import dev.hilligans.ourcraft.Client.Rendering.Screens.EscapeScreen;
import dev.hilligans.ourcraft.Client.Rendering.Screens.FrameTimeScreen;
import dev.hilligans.ourcraft.Client.Rendering.Screens.JoinScreen;
import dev.hilligans.ourcraft.Client.Rendering.Screens.TagEditorScreen;
import dev.hilligans.ourcraft.Client.Rendering.Textures;
import dev.hilligans.ourcraft.Item.Data.ToolLevel;
import dev.hilligans.ourcraft.ModHandler.Content.CoreExtensionView;
import dev.hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.hilligans.ourcraft.ModHandler.Identifier;
import dev.hilligans.ourcraft.Network.Protocols;
import dev.hilligans.ourcraft.Resource.Loaders.StringLoader;
import dev.hilligans.ourcraft.Resource.RegistryLoaders.JsonRegistryLoader;
import dev.hilligans.ourcraft.Resource.Loaders.ImageLoader;
import dev.hilligans.ourcraft.Resource.Loaders.JsonLoader;
import dev.hilligans.ourcraft.Resource.ResourceManager;
import dev.hilligans.ourcraft.Schematic.LitematicaSchematicLoader;
import dev.hilligans.ourcraft.Util.NamedThreadFactory;
import at.favre.lib.crypto.bcrypt.BCrypt;
import dev.hilligans.ourcraft.Util.Side;
import dev.hilligans.ourcraft.World.NewWorldSystem.ChainedBlockChunkStream;
import dev.hilligans.ourcraft.World.NewWorldSystem.ChunkStream;
import dev.hilligans.ourcraft.Block.Blocks;
import dev.hilligans.ourcraft.Client.Input.Key.KeyHandler;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.*;
import org.json.JSONArray;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.nio.DoubleBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.lwjgl.glfw.GLFW.*;

public class Ourcraft {

    public static final GameInstance GAME_INSTANCE = new GameInstance();
    public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2,new NamedThreadFactory("random_executor"));

    public static String path = System.getProperty("user.dir");

    public static String hashString(String password, String salt) {
        return new String(BCrypt.withDefaults().hash(12,"abcdefghjklmmopq".getBytes(), (password + salt).getBytes()), StandardCharsets.UTF_8);
    }

    public static synchronized ResourceManager getResourceManager() {
        return GAME_INSTANCE.RESOURCE_MANAGER;
    }

    public static File getFile(String path) {
        return new File(Ourcraft.path + "/" + path);
    }

    public static void registerCoreExtensions(CoreExtensionView view) {
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

        if (view.getGameInstance().side.equals(Side.CLIENT)) {
            Textures.addData(view.getModContent());

            view.registerGraphicsEngine(new VulkanEngine());
            view.registerGraphicsEngine(new OpenGLEngine());
            view.registerGraphicsEngine(new FixedFunctionGLEngine());

            view.registerRenderPipelines(new RenderPipeline("world_pipeline"));

            view.registerRenderTarget(new RenderTarget("solid_world_renderer", "ourcraft:world_pipeline")
                    .setPipelineState(new PipelineState().setDepth(true)));
            view.registerRenderTarget(new RenderTarget("entity_renderer", "ourcraft:world_pipeline").afterTarget("solid_world_renderer", "ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            view.registerRenderTarget(new RenderTarget("particle_renderer", "ourcraft:world_pipeline").afterTarget("entity_renderer", "ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            view.registerRenderTarget(new RenderTarget("translucent_world_renderer", "ourcraft:world_pipeline").afterTarget("particle_renderer", "ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            view.registerRenderTarget(new RenderTarget("gui_renderer", "ourcraft:world_pipeline").afterTarget("translucent_world_renderer", "ourcraft")
                    .setPipelineState(new PipelineState()));

            view.registerRenderPipelines(new RenderPipeline("new_world_pipeline"));

            view.registerRenderTarget(new RenderTarget("new_solid_world_renderer", "ourcraft:new_world_pipeline")
                    .setPipelineState(new PipelineState().setDepth(true)));
            view.registerRenderTarget(new RenderTarget("entity_renderer", "ourcraft:new_world_pipeline").afterTarget("new_solid_world_renderer", "ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            view.registerRenderTarget(new RenderTarget("particle_renderer", "ourcraft:new_world_pipeline").afterTarget("entity_renderer", "ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            view.registerRenderTarget(new RenderTarget("translucent_world_renderer", "ourcraft:new_world_pipeline").afterTarget("particle_renderer", "ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            view.registerRenderTarget(new RenderTarget("gui_renderer", "ourcraft:new_world_pipeline").afterTarget("translucent_world_renderer", "ourcraft")
                    .setPipelineState(new PipelineState()));

            view.registerRenderTask(new GUIRenderTask());
            //view.registerRenderTask(new WorldRenderTask());
            view.registerRenderTask(new WorldRenderTask());
            view.registerRenderTask(new WorldTransparentRenderTask());

            view.registerVertexFormat(position_texture_color, position_color_texture, position_texture_globalColor, position_texture, position_texture_animatedWrap_shortenedColor, position_color);


//            view.registerShader(new ShaderSource("world_shader","ourcraft:position_texture_color", "Shaders/WorldVertexShader.glsl","Shaders/WorldFragmentShader.glsl"));
            view.registerShader(new ShaderSource("world_shader", "ourcraft:position_color_texture", "Shaders/WorldVertexShader.glsl", "Shaders/WorldFragmentShader.glsl").withUniform("transform", "4fv").withUniform("color", "4f"));
            view.registerShader(new ShaderSource("position_color_shader", "ourcraft:position_color", "Shaders/WorldVertexColorShader.glsl", "Shaders/WorldFragmentShader.glsl").withUniform("transform", "4fv").withUniform("color", "4f"));
            view.registerShader(new ShaderSource("position_texture", "ourcraft:position_texture", "Shaders/PositionTexture.vsh", "Shaders/PositionTexture.fsh").withUniform("transform", "4fv").withUniform("color", "4f"));

            view.registerInputHandlerProviders(new ControllerHandlerProvider(), new KeyPressHandlerProvider(), new MouseHandlerProvider());
        }
    }

    public static void registerDefaultContent(ModContent modContent) {
        //temporarily
        registerCoreExtensions(modContent.getCoreView());

        chainedChunkStream.assignModContent(modContent);
        modContent.registerResourceLoader(new JsonLoader(), new ImageLoader(), new StringLoader());
        modContent.registerResourceLoader(new LitematicaSchematicLoader());

        modContent.registerBlocks(Blocks.AIR, Blocks.STONE, Blocks.DIRT, Blocks.GRASS, Blocks.BEDROCK, Blocks.IRON_ORE, Blocks.LEAVES, Blocks.LOG, Blocks.SAND, Blocks.CACTUS, Blocks.CHEST, Blocks.COLOR_BLOCK, Blocks.STAIR_BLOCK, Blocks.GRASS_PLANT, Blocks.WEEPING_VINE, Blocks.MAPLE_LOG, Blocks.MAPLE_PLANKS, Blocks.PINE_LOG, Blocks.PINE_PLANKS, Blocks.SPRUCE_LOG, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_LOG, Blocks.BIRCH_PLANKS, Blocks.OAK_LOG, Blocks.OAK_PLANKS, Blocks.WILLOW_LOG, Blocks.WILLOW_PLANKS, Blocks.ACACIA_LOG, Blocks.ACACIA_PLANKS, Blocks.POPLAR_LOG, Blocks.POPLAR_PLANKS, Blocks.ELM_LOG, Blocks.ELM_WOOD, Blocks.PALM_LOG, Blocks.PALM_WOOD, Blocks.REDWOOD_LOG, Blocks.REDWOOD_WOOD, Blocks.SAPLING);
        modContent.registerBlock(Blocks.RED);
        modContent.registerBlock(Blocks.WATER);
        modContent.registerBiome(Biomes.PLAINS,Biomes.SANDY_HILLS,Biomes.DESERT,Biomes.FOREST);

        Sounds.reg();
        modContent.registerSounds(Sounds.BLOCK_BREAK, Sounds.MUSIC);
        //modContent.registerTexture(Textures.TEXTURES.toArray(new Texture[0]));
        Protocols.register(modContent);

        if (modContent.getGameInstance().side.equals(Side.CLIENT)) {
            modContent.registerKeybinds(new Input("ourcraft:mouse_button_handler::0") {
                @Override
                public void press(RenderWindow renderWindow, float strength) {
                    Client client = renderWindow.getClient();
                    if (client.screen != null) {
                        DoubleBuffer doubleBuffer = getMousePos(renderWindow.getWindowID());
                        client.screen.mouseClick((int) doubleBuffer.get(0), (int) doubleBuffer.get(1), GLFW.GLFW_MOUSE_BUTTON_1);
                    }
                }
            });

            modContent.registerKeybinds(new Input("ourcraft:key_press_handler::" + KeyHandler.GLFW_KEY_ESCAPE) {
                @Override
                public void press(RenderWindow renderWindow, float strength) {
                    Client client = renderWindow.getClient();
                    if (client.renderWorld) {
                        if (client.screen == null) {
                            client.openScreen(new EscapeScreen(client));
                        } else {
                            client.closeScreen();
                        }
                    } else {
                        client.openScreen(new JoinScreen(client));
                    }
                }
            });

            modContent.registerKeybinds(new Input("ourcraft:key_press_handler::" + KeyHandler.GLFW_KEY_F3) {
                @Override
                public void press(RenderWindow renderWindow, float strength) {
                    Client client = renderWindow.getClient();
                    client.playerData.f3 = !client.playerData.f3;
                }
            });

            modContent.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_W,
                    (window, strength) -> window.getCamera().moveForward(5f * strength)));

            modContent.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_A,
                    (window, strength) -> window.getCamera().moveLeft(5f * strength)));

            modContent.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_S,
                    (window, strength) -> window.getCamera().moveBackward(5f * strength)));

            modContent.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_D,
                    (window, strength) -> window.getCamera().moveRight(5f * strength)));

            modContent.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_SPACE,
                    (window, strength) -> window.getCamera().moveUp(5f * strength)));

            modContent.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_LEFT_SHIFT,
                    (window, strength) -> window.getCamera().moveUp(-5f * strength)));

            modContent.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_DOWN,
                    (window, strength) -> window.getCamera().addRotation(0.1f * strength, 0)));

            modContent.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_UP,
                    (window, strength) -> window.getCamera().addRotation(-0.1f * strength, 0)));

            modContent.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_LEFT,
                    (window, strength) -> window.getCamera().addRotation(0, -0.3f * strength)));

            modContent.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_RIGHT,
                    (window, strength) -> window.getCamera().addRotation(0, 0.3f * strength)));

            modContent.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_L,
                    (window, strength) -> ((WorldCamera)window.getCamera()).roll -= 0.1f * strength));

            modContent.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_O,
                    (window, strength) -> ((WorldCamera)window.getCamera()).roll += 0.1f * strength));


            modContent.registerKeybinds(new Input("ourcraft:key_press_handler::" + GLFW_KEY_H) {
                @Override
                public void press(RenderWindow renderWindow, float strength) {
                    Client client = renderWindow.getClient();
                    client.openScreen(new TagEditorScreen(client));
                }
            });

            modContent.registerKeybinds(new Input("ourcraft:key_press_handler::" + GLFW_KEY_P) {
                @Override
                public void press(RenderWindow renderWindow, float strength) {
                    Client client = renderWindow.getClient();
                    client.openScreen(new FrameTimeScreen(client));
                }
            });

            modContent.registerKeybinds(new Input("ourcraft:key_press_handler::" + GLFW_KEY_T) {
                @Override
                public void press(RenderWindow renderWindow, float strength) {
                    Client client = renderWindow.getClient();
                    if (client.renderWorld) {
                        client.openScreen(new ChatWindow());
                    }
                }
            });
        }
        Ourcraft.getResourceManager().gameInstance = modContent.gameInstance;
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

    public static final VertexFormat position_RGB = new VertexFormat("ourcraft", "position_RGB", VertexFormat.TRIANGLES)
            .addPart("position", VertexFormat.FLOAT, 3)
            .addPart("color", VertexFormat.FLOAT, 3);

    public static ChunkStream chainedChunkStream = new ChainedBlockChunkStream("");
}