package dev.Hilligans.ourcraft;

import dev.Hilligans.ourcraft.Biome.Biomes;
import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Client.Audio.Sounds;
import dev.Hilligans.ourcraft.Client.ChatWindow;
import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Input.HandlerProviders.ControllerHandlerProvider;
import dev.Hilligans.ourcraft.Client.Input.HandlerProviders.KeyPressHandlerProvider;
import dev.Hilligans.ourcraft.Client.Input.HandlerProviders.MouseHandlerProvider;
import dev.Hilligans.ourcraft.Client.Input.Input;
import dev.Hilligans.ourcraft.Client.Input.RepeatingInput;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.*;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.FixedFunctionGL.FixedFunctionGLEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.OpenGL.OpenGLEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Tasks.GUIRenderTask;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Tasks.NewWorldRenderTask;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Tasks.WorldRenderTask;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Tasks.WorldTransparentRenderTask;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.VulkanEngine;
import dev.Hilligans.ourcraft.Client.Rendering.ScreenBuilder;
import dev.Hilligans.ourcraft.Client.Rendering.Screens.EscapeScreen;
import dev.Hilligans.ourcraft.Client.Rendering.Screens.FrameTimeScreen;
import dev.Hilligans.ourcraft.Client.Rendering.Screens.JoinScreen;
import dev.Hilligans.ourcraft.Client.Rendering.Screens.TagEditorScreen;
import dev.Hilligans.ourcraft.Client.Rendering.Textures;
import dev.Hilligans.ourcraft.Item.Data.ToolLevel;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.ModHandler.Identifier;
import dev.Hilligans.ourcraft.Network.Protocols;
import dev.Hilligans.ourcraft.Resource.Loaders.StringLoader;
import dev.Hilligans.ourcraft.Resource.RegistryLoaders.JsonRegistryLoader;
import dev.Hilligans.ourcraft.Resource.Loaders.ImageLoader;
import dev.Hilligans.ourcraft.Resource.Loaders.JsonLoader;
import dev.Hilligans.ourcraft.Resource.ResourceManager;
import dev.Hilligans.ourcraft.Schematic.LitematicaSchematicLoader;
import dev.Hilligans.ourcraft.Util.NamedThreadFactory;
import at.favre.lib.crypto.bcrypt.BCrypt;
import dev.Hilligans.ourcraft.Util.Side;
import org.json.JSONArray;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.nio.DoubleBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

import static dev.Hilligans.ourcraft.Block.Blocks.*;
import static dev.Hilligans.ourcraft.Client.Input.Key.KeyHandler.GLFW_KEY_ESCAPE;
import static dev.Hilligans.ourcraft.Client.Input.Key.KeyHandler.GLFW_KEY_F3;
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

    public static void registerDefaultContent(ModContent modContent) {
        modContent.registerResourceLoader(new JsonLoader(), new ImageLoader(), new StringLoader());
        modContent.registerResourceLoader(new LitematicaSchematicLoader());

        modContent.registerBlocks(AIR,STONE,DIRT,GRASS,BEDROCK,IRON_ORE,LEAVES,LOG,SAND,CACTUS,CHEST,COLOR_BLOCK,STAIR_BLOCK,GRASS_PLANT,WEEPING_VINE,MAPLE_LOG,MAPLE_PLANKS,PINE_LOG,PINE_PLANKS,SPRUCE_LOG,SPRUCE_PLANKS,BIRCH_LOG,BIRCH_PLANKS,OAK_LOG,OAK_PLANKS,WILLOW_LOG,WILLOW_PLANKS,ACACIA_LOG,ACACIA_PLANKS,POPLAR_LOG,POPLAR_PLANKS,ELM_LOG,ELM_WOOD,PALM_LOG,PALM_WOOD,REDWOOD_LOG,REDWOOD_WOOD,SAPLING);
        modContent.registerBiome(Biomes.PLAINS,Biomes.SANDY_HILLS,Biomes.DESERT,Biomes.FOREST);
        Sounds.reg();
        modContent.registerSounds(Sounds.BLOCK_BREAK, Sounds.MUSIC);
        //modContent.registerTexture(Textures.TEXTURES.toArray(new Texture[0]));
        Protocols.register(modContent);

        modContent.registerRegistryLoader(new JsonRegistryLoader(new Identifier("tool_tiers", "ourcraft"), "Data/ToolTiers.json", (modContent1, jsonObject, string) -> {
            JSONArray elements = jsonObject.getJSONArray("material");
            ToolLevel[] levels = new ToolLevel[elements.length()];
            for(int x = 0; x < elements.length(); x++) {
                levels[x] = new ToolLevel(new Identifier(elements.getString(x), modContent1.getModID()));
            }
            String[] parts = jsonObject.getString("location").split(" ");
            switch (parts[0]) {
                case "start" -> modContent1.gameInstance.MATERIAL_LIST.addToStart(levels);
                case "end" -> modContent1.gameInstance.MATERIAL_LIST.addToEnd(levels);
                case "before" -> modContent1.gameInstance.MATERIAL_LIST.insertBefore(parts[1], levels);
                case "after" -> modContent1.gameInstance.MATERIAL_LIST.insertAfter(parts[1], levels);
            }
            modContent.gameInstance.registerToolLevels(levels);
        }).rerunOnInstanceClear());

        modContent.registerRegistryLoader(new JsonRegistryLoader(new Identifier("blocks", "ourcraft"), "Data/Blocks.json", (modContent12, jsonObject, key) -> {
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


        modContent.registerRegistryLoader(new JsonRegistryLoader(new Identifier("screens", "ourcraft"), "Data/Screens.json", (modContent12, jsonObject, key) -> {
            modContent12.registerScreenBuilder(new ScreenBuilder(key, jsonObject));
        }).rerunOnInstanceClear());

        if(modContent.gameInstance.side.equals(Side.CLIENT)) {
            Textures.addData(modContent);

            modContent.registerGraphicsEngine(new VulkanEngine());
            modContent.registerGraphicsEngine(new OpenGLEngine());
            modContent.registerGraphicsEngine(new FixedFunctionGLEngine());

            modContent.registerRenderPipelines(new RenderPipeline("world_pipeline"));

            modContent.registerRenderTarget(new RenderTarget("solid_world_renderer", "ourcraft:world_pipeline")
                    .setPipelineState(new PipelineState().setDepth(true)));
            modContent.registerRenderTarget(new RenderTarget("entity_renderer", "ourcraft:world_pipeline").afterTarget("solid_world_renderer","ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            modContent.registerRenderTarget(new RenderTarget("particle_renderer", "ourcraft:world_pipeline").afterTarget("entity_renderer", "ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            modContent.registerRenderTarget(new RenderTarget("translucent_world_renderer", "ourcraft:world_pipeline").afterTarget("particle_renderer", "ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            modContent.registerRenderTarget(new RenderTarget("gui_renderer", "ourcraft:world_pipeline").afterTarget("translucent_world_renderer", "ourcraft")
                    .setPipelineState(new PipelineState()));

            modContent.registerRenderPipelines(new RenderPipeline("new_world_pipeline"));

            modContent.registerRenderTarget(new RenderTarget("new_solid_world_renderer", "ourcraft:new_world_pipeline")
                    .setPipelineState(new PipelineState().setDepth(true)));
            modContent.registerRenderTarget(new RenderTarget("entity_renderer", "ourcraft:new_world_pipeline").afterTarget("new_solid_world_renderer","ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            modContent.registerRenderTarget(new RenderTarget("particle_renderer", "ourcraft:new_world_pipeline").afterTarget("entity_renderer", "ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            modContent.registerRenderTarget(new RenderTarget("translucent_world_renderer", "ourcraft:new_world_pipeline").afterTarget("particle_renderer", "ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            modContent.registerRenderTarget(new RenderTarget("gui_renderer", "ourcraft:new_world_pipeline").afterTarget("translucent_world_renderer", "ourcraft")
                    .setPipelineState(new PipelineState()));

            modContent.registerRenderTask(new GUIRenderTask());
            modContent.registerRenderTask(new WorldRenderTask());
            modContent.registerRenderTask(new WorldTransparentRenderTask());
            modContent.registerRenderTask(new NewWorldRenderTask());

            modContent.registerVertexFormat(position_texture_color, position_color_texture, position_texture_globalColor, position_texture, position_texture_animatedWrap_shortenedColor, position_color);


//            modContent.registerShader(new ShaderSource("world_shader","ourcraft:position_texture_color", "Shaders/WorldVertexShader.glsl","Shaders/WorldFragmentShader.glsl"));
            modContent.registerShader(new ShaderSource("world_shader","ourcraft:position_color_texture", "Shaders/WorldVertexShader.glsl","Shaders/WorldFragmentShader.glsl").uniformNames("transform", "color"));
            modContent.registerShader(new ShaderSource("position_color_shader", "ourcraft:position_color", "Shaders/WorldVertexColorShader.glsl","Shaders/WorldFragmentShader.glsl").uniformNames("transform", "color"));
            modContent.registerShader(new ShaderSource("position_texture", "ourcraft:position_texture", "Shaders/PositionTexture.vsh","Shaders/PositionTexture.fsh").uniformNames("transform", "color"));

            modContent.registerInputHandlerProviders(new ControllerHandlerProvider(), new KeyPressHandlerProvider(), new MouseHandlerProvider());

            modContent.registerKeybinds(new Input("ourcraft:mouse_button_handler::0") {
                @Override
                public void press(RenderWindow renderWindow, float strength) {
                    Client client = renderWindow.getClient();
                    if(client.screen != null) {
                        DoubleBuffer doubleBuffer = getMousePos(renderWindow.getWindowID());
                        client.screen.mouseClick((int) doubleBuffer.get(0), (int) doubleBuffer.get(1), GLFW.GLFW_MOUSE_BUTTON_1);
                    }
                }
            });

            modContent.registerKeybinds(new Input("ourcraft:key_press_handler::" + GLFW_KEY_ESCAPE) {
                @Override
                public void press(RenderWindow renderWindow, float strength) {
                    Client client = renderWindow.getClient();
                    System.out.println("yes");
                    if(client.renderWorld) {
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

            modContent.registerKeybinds(new Input("ourcraft:key_press_handler::" + GLFW_KEY_F3) {
                @Override
                public void press(RenderWindow renderWindow, float strength) {
                    Client client = renderWindow.getClient();
                    client.playerData.f3 = !client.playerData.f3;
                }
            });

            modContent.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_W,
                    (window, strength) -> window.getCamera().moveForeWard(-0.05f * strength)));

            modContent.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_A,
                    (window, strength) -> window.getCamera().moveLeft(0.05f * strength)));

            modContent.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_S,
                    (window, strength) -> window.getCamera().moveBackWard(0.05f * strength)));

            modContent.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_D,
                    (window, strength) -> window.getCamera().moveRight(0.05f * strength)));

            modContent.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_SPACE,
                    (window, strength) -> window.getCamera().moveUp(0.05f * strength)));

            modContent.registerKeybinds(new RepeatingInput("ourcraft:key_press_handler::" + GLFW_KEY_LEFT_SHIFT,
                    (window, strength) -> window.getCamera().moveUp(-0.05f * strength)));

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
                    if(client.renderWorld) {
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
}
