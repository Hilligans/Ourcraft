package dev.hilligans.ourcraft;

import at.favre.lib.crypto.bcrypt.BCrypt;
import dev.hilligans.engine.authentication.IAuthenticationScheme;
import dev.hilligans.engine.client.graphics.*;
import dev.hilligans.engine.util.argument.Argument;
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
import dev.hilligans.engine.client.input.RepeatingInput;
import dev.hilligans.engine.client.input.handlers.MouseHandler;
import dev.hilligans.engine.client.input.key.KeyHandler;
import dev.hilligans.ourcraft.client.rendering.ScreenBuilder;
import dev.hilligans.ourcraft.client.rendering.Textures;
import dev.hilligans.ourcraft.client.rendering.WorldCamera;
import dev.hilligans.ourcraft.client.rendering.graphics.*;
import dev.hilligans.ourcraft.client.rendering.screens.*;
import dev.hilligans.ourcraft.command.Commands;
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
import dev.hilligans.ourcraft.network.Protocols;
import dev.hilligans.ourcraft.recipe.IRecipe;
import dev.hilligans.ourcraft.recipe.helper.RecipeView;
import dev.hilligans.engine.resource.registry.loaders.JsonRegistryLoader;
import dev.hilligans.ourcraft.schematic.LitematicaSchematicLoader;
import dev.hilligans.ourcraft.server.MultiPlayerServer;
import dev.hilligans.ourcraft.settings.Setting;
import dev.hilligans.engine.test.standard.DuplicateRegistryTest;
import dev.hilligans.engine.util.registry.IRegistryElement;
import dev.hilligans.engine.util.registry.Registry;
import dev.hilligans.ourcraft.world.Feature;
import dev.hilligans.ourcraft.world.newworldsystem.ChainedBlockChunkStream;
import dev.hilligans.ourcraft.world.newworldsystem.ChunkStream;
import org.json.JSONArray;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class Ourcraft implements ModClass {

    public static final Argument<Boolean> integratedServer = Argument.existArg("--integratedServer")
            .help("Whether or not to launch an integrated server.");

    public static String path = System.getProperty("user.dir");

    public static String hashString(String password, String salt) {
        return new String(BCrypt.withDefaults().hash(12,"abcdefghjklmmopq".getBytes(), (password + salt).getBytes()), StandardCharsets.UTF_8);
    }

    @Override
    public String getModID() {
        return "ourcraft";
    }

    @Override
    public void registerRegistries(RegistryView view) {
        register(view,
                new Tuple(Block.class, "block"),
                new Tuple(Items.class, "item"),
                new Tuple(Biome.class, "biome"),
                new Tuple(Tag.class, "tag"),
                new Tuple(IRecipe.class, "recipe"),
                new Tuple(RecipeView.class, "recipe_view"),
                new Tuple(Setting.class, "setting"),
                new Tuple(SoundBuffer.class, "sound"),
                new Tuple(ToolLevel.class, "tool_level"),
                new Tuple(ScreenBuilder.class, "screen"),
                new Tuple(Feature.class, "feature"),
                new Tuple(SoundCategory.class, "sound_category"),
                new Tuple(EntityType.class, "entity_type"));
    }

    public void registerCoreExtensions(CoreExtensionView view) {
        view.registerRegistryLoader(new JsonRegistryLoader("tool_tiers", "Data/ToolTiers.json", (modContent1, jsonObject, string) -> {
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

            //modContent1.registerToolLevel(levels);
        }));

        /*view.registerRegistryLoader(new JsonRegistryLoader(new Identifier("blocks", "ourcraft"), "Data/Blocks.json", (modContent12, jsonObject, key) -> {
            try {
                Block block = new Block(key, "Data/" + jsonObject.optString("data"), jsonObject.optJSONObject("overrides"));
                JSONArray textures = jsonObject.getJSONArray("textures");
                for (int x = 0; x < textures.length(); x++) {
                    block.blockProperties.addTexture(textures.getString(x), x, textures.length());
                }
                //modContent12.registerBlock(block);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
         */


        //view.registerRegistryLoader(new JsonRegistryLoader(new Identifier("screens", "ourcraft"), "Data/Screens.json", (modContent12, jsonObject, key) -> {
            //modContent12.registerScreenBuilder(new ScreenBuilder(key, jsonObject));
        //}));

        if (view.getGameInstance().side.isClient()) {
            Textures.addData(view);


//            view.registerShader(new ShaderSource("world_shader","ourcraft:position_texture_color", "Shaders/WorldVertexShader.glsl","Shaders/WorldFragmentShader.glsl"));


            if (view.getGameInstance().side.isClient()) {
                view.registerKeybinds(new Input("left_click", "ourcraft:mouse_handler::0") {
                    @Override
                    public void press(RenderWindow renderWindow, double strength) {
                        Client client = (Client) renderWindow.getClient();
                        if (client.screen != null) {
                            DoubleBuffer doubleBuffer = getMousePos(renderWindow.getWindowID());
                            client.screen.mouseClick((int) doubleBuffer.get(0), (int) doubleBuffer.get(1), GLFW.GLFW_MOUSE_BUTTON_1);
                        }
                    }
                });

                view.registerKeybinds(new Input("escape_menu", "ourcraft:key_press_handler::" + KeyHandler.GLFW_KEY_ESCAPE) {
                    @Override
                    public void press(RenderWindow renderWindow, double strength) {
                        Client client = (Client) renderWindow.getClient();
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

                view.registerKeybinds(new Input("f3", "ourcraft:key_press_handler::" + KeyHandler.GLFW_KEY_F3) {
                    @Override
                    public void press(RenderWindow renderWindow, double strength) {
                        Client client = (Client) renderWindow.getClient();
                        client.playerData.f3 = !client.playerData.f3;
                    }
                }.onlyWithPipelines("ourcraft:new_world_pipeline"));

                view.registerKeybinds(new Input("rotate_yaw", "ourcraft:mouse_handler::" + MouseHandler.MOUSE_X) {
                    @Override
                    public void press(RenderWindow window, double strength) {
                        window.getCamera().addRotation((float) 0, (float) (strength/400));
                        window.setMousePosition(window.getWindowWidth()/2, window.getWindowHeight()/2);
                        //GLFW.glfwSetCursorPos(window.getWindowID(), window.getWindowWidth()/2,window.getWindowHeight()/2);
                    }
                }.onlyWithPipelines("ourcraft:new_world_pipeline"));

                view.registerKeybinds(new Input("rotate_pitch", "ourcraft:mouse_handler::" + MouseHandler.MOUSE_Y) {
                    @Override
                    public void press(RenderWindow window, double strength) {
                        window.getCamera().addRotation((float) (-strength/400),0);
                        window.setMousePosition(window.getWindowWidth()/2, window.getWindowHeight()/2);
                        //GLFW.glfwSetCursorPos(window.getWindowID(), window.getWindowWidth()/2,window.getWindowHeight()/2);
                    }
                }.onlyWithPipelines("ourcraft:new_world_pipeline"));

                view.registerKeybinds(new Input("open_test_screen", "ourcraft:key_press_handler::" + GLFW_KEY_SEMICOLON) {
                    @Override
                    public void press(RenderWindow renderWindow, double strength) {
                        super.press(renderWindow, strength);
                        renderWindow.client.openScreen(new TestScreen());
                    }
                });

                view.registerKeybinds(new Input("chunk_outlines", "ourcraft:key_press_handler::" + GLFW_KEY_F8){
                    @Override
                    public void press(RenderWindow renderWindow, double strength) {
                        super.press(renderWindow, strength);
                        Client client = (Client) renderWindow.getClient();
                        client.getPlayerData().debugChunkRendering = !client.getPlayerData().debugChunkRendering;
                    }
                });

                view.registerKeybinds(new RepeatingInput("move_forward", "ourcraft:key_press_handler::" + GLFW_KEY_W,
                        (window, strength) -> window.getCamera().moveForward((float) (5.6f * strength))).onlyWithPipelines("ourcraft:new_world_pipeline"));

                view.registerKeybinds(new RepeatingInput("move_left", "ourcraft:key_press_handler::" + GLFW_KEY_A,
                        (window, strength) -> window.getCamera().moveLeft((float) (5f * strength))).onlyWithPipelines("ourcraft:new_world_pipeline"));

                view.registerKeybinds(new RepeatingInput("move_backward", "ourcraft:key_press_handler::" + GLFW_KEY_S,
                        (window, strength) -> window.getCamera().moveBackward((float) (5f * strength))).onlyWithPipelines("ourcraft:new_world_pipeline"));

                view.registerKeybinds(new RepeatingInput("move_right", "ourcraft:key_press_handler::" + GLFW_KEY_D,
                        (window, strength) -> window.getCamera().moveRight((float) (5f * strength))).onlyWithPipelines("ourcraft:new_world_pipeline"));

                view.registerKeybinds(new RepeatingInput("jump", "ourcraft:key_press_handler::" + GLFW_KEY_SPACE,
                        (window, strength) -> window.getCamera().moveUp((float) (5f * strength))).onlyWithPipelines("ourcraft:new_world_pipeline"));

                view.registerKeybinds(new RepeatingInput("crouch", "ourcraft:key_press_handler::" + GLFW_KEY_LEFT_SHIFT,
                        (window, strength) -> window.getCamera().moveUp((float) (-5f * strength))).onlyWithPipelines("ourcraft:new_world_pipeline"));

                view.registerKeybinds(new RepeatingInput("pitch_down", "ourcraft:key_press_handler::" + GLFW_KEY_DOWN,
                        (window, strength) -> window.getCamera().addRotation((float) (0.1f * strength), 0)));

                view.registerKeybinds(new RepeatingInput("pitch_up", "ourcraft:key_press_handler::" + GLFW_KEY_UP,
                        (window, strength) -> window.getCamera().addRotation((float) (-0.1f * strength), 0)));

                view.registerKeybinds(new RepeatingInput("yaw_down", "ourcraft:key_press_handler::" + GLFW_KEY_LEFT,
                        (window, strength) -> window.getCamera().addRotation((float) 0, (float) (-0.3f * strength))));

                view.registerKeybinds(new RepeatingInput("yaw_up", "ourcraft:key_press_handler::" + GLFW_KEY_RIGHT,
                        (window, strength) -> window.getCamera().addRotation((float) 0, (float) (0.3f * strength))));

                view.registerKeybinds(new RepeatingInput("roll_down", "ourcraft:key_press_handler::" + GLFW_KEY_L,
                        (window, strength) -> ((WorldCamera)window.getCamera()).roll -= 0.1f * strength));

                view.registerKeybinds(new RepeatingInput("roll_up", "ourcraft:key_press_handler::" + GLFW_KEY_O,
                        (window, strength) -> ((WorldCamera)window.getCamera()).roll += 0.1f * strength));


                view.registerKeybinds(new Input("open_tag_editor", "ourcraft:key_press_handler::" + GLFW_KEY_H) {
                    @Override
                    public void press(RenderWindow renderWindow, double strength) {
                        Client client = (Client) renderWindow.getClient();
                        client.openScreen(new TagEditorScreen());
                    }
                });

                view.registerKeybinds(new Input("open_frame_times", "ourcraft:key_press_handler::" + GLFW_KEY_P) {
                    @Override
                    public void press(RenderWindow renderWindow, double strength) {
                        Client client = (Client) renderWindow.getClient();
                        client.openScreen(new FrameTimeScreen());
                    }
                });

                view.registerKeybinds(new Input("open_chat", "ourcraft:key_press_handler::" + GLFW_KEY_T) {
                    @Override
                    public void press(RenderWindow renderWindow, double strength) {
                        Client client = (Client) renderWindow.getClient();
                        if (client.renderWorld) {
                            client.openScreen(new ChatWindow());
                        }
                    }
                });
            }
            view.registerApplication(new Client(view.getGameInstance(), view.getGameInstance().getArgumentContainer()));
        }
        if(integratedServer.get(view.getGameInstance())) {
            view.registerApplication(new MultiPlayerServer(view.getGameInstance()));
        }
    }

    @Override
    public void registerContent(ModContainer modContent) {
        chainedChunkStream.assignOwner(modContent);
        modContent.registerResourceLoader(new LitematicaSchematicLoader());

        modContent.registerTest(new DuplicateRegistryTest());

        if(modContent.getGameInstance().getSide().isClient()) {
            modContent.registerRenderTask(new WorldRenderTask());
            modContent.registerRenderTask(new WorldTransparentRenderTask());
            modContent.registerRenderTask(new ChatRenderTask());
            modContent.registerRenderTask(new ChunkDebugRenderTask());

            modContent.registerRenderPipelines(new RenderPipeline("new_world_pipeline"));
            modContent.registerRenderTarget(new RenderTarget("debug_world_renderer", "ourcraft:new_world_pipeline", "ourcraft:chunk_debug_render_task")
                    .setPipelineState(new PipelineState().setDepth(false)));
            modContent.registerRenderTarget(new RenderTarget("new_solid_world_renderer", "ourcraft:new_world_pipeline", "ourcraft:new_world_render_task").afterTarget("debug_world_renderer", "ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            modContent.registerRenderTarget(new RenderTarget("entity_renderer", "ourcraft:new_world_pipeline", "ourcraft:blank_render_task").afterTarget("new_solid_world_renderer", "ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            modContent.registerRenderTarget(new RenderTarget("particle_renderer", "ourcraft:new_world_pipeline", "ourcraft:blank_render_task").afterTarget("entity_renderer", "ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            modContent.registerRenderTarget(new RenderTarget("translucent_world_renderer", "ourcraft:new_world_pipeline", "ourcraft:world_transparent_render_task").afterTarget("particle_renderer", "ourcraft")
                    .setPipelineState(new PipelineState().setDepth(true)));
            modContent.registerRenderTarget(new RenderTarget("chat_renderer", "ourcraft:new_world_pipeline", "ourcraft:chat_render_task").afterTarget("translucent_world_renderer", "ourcraft")
                    .setPipelineState(new PipelineState()));
            modContent.registerRenderTarget(new RenderTarget("gui_renderer", "ourcraft:new_world_pipeline", "ourcraft:gui_render_task").afterTarget("chat_renderer", "ourcraft")
                    .setPipelineState(new PipelineState()));


            modContent.registerRenderPipelines(new RenderPipeline("menu_pipeline"));
            modContent.registerRenderPipelines(new RenderPipeline("split_window_pipeline"));


            modContent.registerRenderTarget(new RenderTarget("gui_renderer", "ourcraft:menu_pipeline", "ourcraft:gui_render_task")
                    .setPipelineState(new PipelineState()));

            modContent.registerRenderTarget(new RenderTarget("split_window_renderer", "ourcraft:split_window_pipeline", "ourcraft:split_window_render_task")
                    .setPipelineState(new PipelineState()));
        }



        modContent.registerBlocks(Blocks.AIR, Blocks.STONE, Blocks.DIRT, Blocks.GRASS, Blocks.BEDROCK, Blocks.IRON_ORE, Blocks.LEAVES, Blocks.LOG, Blocks.SAND, Blocks.CACTUS, Blocks.CHEST, Blocks.STAIR_BLOCK, Blocks.GRASS_PLANT, Blocks.WEEPING_VINE, Blocks.MAPLE_LOG, Blocks.MAPLE_PLANKS, Blocks.PINE_LOG, Blocks.PINE_PLANKS, Blocks.SPRUCE_LOG, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_LOG, Blocks.BIRCH_PLANKS, Blocks.OAK_LOG, Blocks.OAK_PLANKS, Blocks.WILLOW_LOG, Blocks.WILLOW_PLANKS, Blocks.ACACIA_LOG, Blocks.ACACIA_PLANKS, Blocks.POPLAR_LOG, Blocks.POPLAR_PLANKS, Blocks.ELM_LOG, Blocks.ELM_WOOD, Blocks.PALM_LOG, Blocks.PALM_WOOD, Blocks.REDWOOD_LOG, Blocks.REDWOOD_WOOD, Blocks.SAPLING);
        modContent.registerBlock(Blocks.RED);
        modContent.registerBlock(Blocks.WATER);
        modContent.registerBiome(Biomes.PLAINS,Biomes.SANDY_HILLS,Biomes.DESERT,Biomes.FOREST);

        modContent.registerEntityType(Entities.PLAYER);


        Sounds.reg();
        modContent.registerSounds(Sounds.BLOCK_BREAK, Sounds.MUSIC);
        Protocols.register(modContent);
        Commands.register(modContent);

    }

    public static DoubleBuffer getMousePos(long window) {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(window, x, y);
        return BufferUtils.createDoubleBuffer(2).put(x.get()).put(y.get());
    }

    public static ChunkStream chainedChunkStream = new ChainedBlockChunkStream("");
}
