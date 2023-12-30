package dev.hilligans.ourcraft.client;

import dev.hilligans.ourcraft.client.input.InputHandler;
import dev.hilligans.ourcraft.client.input.key.KeyHandler;
import dev.hilligans.ourcraft.client.input.key.KeyPress;
import dev.hilligans.ourcraft.client.input.key.MouseHandler;
import dev.hilligans.ourcraft.client.rendering.ContainerScreen;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IInputProvider;
import dev.hilligans.ourcraft.client.rendering.graphics.implementations.splitwindows.SplitWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.implementations.splitwindows.SubWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.opengl.OpenGLEngine;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.vulkan.VulkanEngine;
import dev.hilligans.ourcraft.client.rendering.Screen;
import dev.hilligans.ourcraft.client.rendering.ScreenBuilder;
import dev.hilligans.ourcraft.client.rendering.screens.JoinScreen;
import dev.hilligans.ourcraft.client.rendering.screens.container.screens.CreativeInventoryScreen;
import dev.hilligans.ourcraft.client.rendering.screens.container.screens.InventoryScreen;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.container.Container;
import dev.hilligans.ourcraft.data.other.PlayerList;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.item.ItemStack;
import dev.hilligans.ourcraft.mod.handler.events.client.OpenScreenEvent;
import dev.hilligans.ourcraft.network.ClientNetwork;
import dev.hilligans.ourcraft.network.IClientPacketHandler;
import dev.hilligans.ourcraft.network.packet.auth.CGetToken;
import dev.hilligans.ourcraft.network.packet.client.CCloseScreen;
import dev.hilligans.ourcraft.network.packet.client.CDropItem;
import dev.hilligans.ourcraft.network.packet.client.COpenScreen;
import dev.hilligans.ourcraft.network.PacketBase;
import dev.hilligans.ourcraft.client.audio.SoundBuffer;
import dev.hilligans.ourcraft.client.audio.SoundEngine;
import dev.hilligans.ourcraft.tag.CompoundNBTTag;
import dev.hilligans.ourcraft.util.ArgumentContainer;
import dev.hilligans.ourcraft.util.Logger;
import dev.hilligans.ourcraft.world.newworldsystem.CubicWorld;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;
import dev.hilligans.ourcraft.save.WorldLoader;
import dev.hilligans.ourcraft.server.MultiPlayerServer;
import dev.hilligans.ourcraft.util.Settings;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL11;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Client implements IClientPacketHandler {

    public long window;

    public boolean mouseLocked = false;

    public int playerId;

    public boolean joinServer = true;
    public boolean valid = false;
    public boolean screenShot = false;
    public boolean renderWorld = false;
    public PlayerList playerList;
    public boolean glStarted = false;
    public long renderTime = 0;
    public String serverIP = "";
    Client client = this;
    public Logger logger;

    public MouseHandler mouseHandler;
    public Screen screen;
    public SoundEngine soundEngine;

    public ClientPlayerData playerData = new ClientPlayerData();
    //public IWorld newClientWorld = new SimpleWorld(0,"");
    public IWorld newClientWorld;

    public MultiPlayerServer multiPlayerServer;
    public boolean rendering = false;

    public ClientNetwork network;
    public ClientNetwork authNetwork;
    public GameInstance gameInstance;

    public IGraphicsEngine<?,?,?> graphicsEngine;
    public InputHandler inputHandler;
    public IInputProvider mouseBind;

    public int renderDistance = 12;
    public int renderYDistance = 8;
    public ArgumentContainer argumentContainer;
    public ChatMessages chatMessages = new ChatMessages();

    public Client(GameInstance gameInstance, ArgumentContainer argumentContainer) {
        this.gameInstance = gameInstance;
        this.newClientWorld = new CubicWorld(gameInstance, 0,"", 64);
        logger = gameInstance.LOGGER.withKey("client");
        graphicsEngine = gameInstance.GRAPHICS_ENGINES.get("ourcraft:openglEngine");
        ((OpenGLEngine)graphicsEngine).client = this;
        soundEngine = new SoundEngine(gameInstance);
        this.argumentContainer = argumentContainer;
    }

    public Client setGraphicsEngine(IGraphicsEngine<?,?,?> graphicsEngine) {
        if(graphicsEngine != null) {

            this.graphicsEngine = graphicsEngine;
            if(graphicsEngine instanceof VulkanEngine engine) {
                engine.client = this;
            }
            System.out.println(graphicsEngine.getResourceName());
        }
        return this;
    }

    public void setupClient() {
        network = new ClientNetwork(gameInstance.PROTOCOLS.get("Play")).debug(argumentContainer.getBoolean("--packetTrace", false));
        /*authNetwork = new ClientNetwork(gameInstance.PROTOCOLS.get("Auth"));

        CompoundNBTTag tag = WorldLoader.loadTag("clientData.dat");
        if(tag != null) {
            Thread thread = new Thread(() -> {
                try {
                    authNetwork.joinServer("hilligans.dev", "25588", this);
                } catch (Exception e) {}
            });
            thread.setName("authenticate");
            thread.setDaemon(true);
            thread.start();
        }
        if(tag != null) {
            readUsernameAndPassword(tag);
        }
        authNetwork.sendPacket(new CGetToken(playerData.userName, playerData.login_token));
         */

        RenderWindow window = graphicsEngine.startEngine();
        window.setClient(this);
        client.screen = new JoinScreen();
        client.screen.setWindow(window);

        window.setClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        if(soundEngine.camera == null) {
            soundEngine.init(window.getCamera());
            soundEngine.setAttenuationModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);
        }
        //registerKeyHandlers();

        // SplitWindow splitWindow = new SplitWindow(graphicsEngine, window);
        // SubWindow subWindow = new SubWindow(graphicsEngine, splitWindow, window.getWindowWidth()/2, window.getWindowHeight()/2, graphicsEngine.getContext());
        // subWindow.setRenderPipeline(window.renderPipeline);
        // splitWindow.setRenderPipeline("ourcraft:split_window_pipeline");
        // splitWindow.addWindow(subWindow);

        rWindow = window;
    }

    public void startClient() {
        setupClient();

        System.err.println("Time to start running: " + (System.currentTimeMillis() - ClientMain.startTime));
        graphicsEngine.createRenderLoop(gameInstance, rWindow).run();
        graphicsEngine.close();
        cleanUp();
    }

    public RenderWindow rWindow;

    public void cleanUp() {
        soundEngine.cleanup();
        for(SoundBuffer soundBuffer : gameInstance.SOUNDS.ELEMENTS) {
            soundBuffer.cleanup();
        }
    }

    public void closeScreen() {
        if(!playerData.heldStack.isEmpty()) {
            sendPacket(new CDropItem((short)-1,(byte)-1));
            playerData.heldStack = ItemStack.emptyStack();
        }
        if(screen != null) {
            //glfwSetCursorPos(window, (double)windowX / 2, (double)windowY / 2);
           // screen.close(false);
          //  screen = null;
          //  glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        }
        if(playerData.openContainer != null) {
            playerData.openContainer.closeContainer();
        }

        sendPacket(new CCloseScreen(false));
    }

    public void openScreen(Screen screen1) {
        RenderWindow renderWindow = rWindow;
        screen1.setWindow(renderWindow);
        gameInstance.EVENT_BUS.postEvent(new OpenScreenEvent(screen1,screen));
        //glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        if(screen != null) {
            screen.close(true);
        }
        screen = screen1;
        if(screen1 instanceof ContainerScreen) {
            if(!(screen1 instanceof InventoryScreen)) {

            }
            ContainerScreen<?> screen2 = (ContainerScreen<?>) screen1;
            Container container = screen2.getContainer();
            screen2.setContainer(container);
            if(playerData.openContainer != null) {
                playerData.openContainer.closeContainer();
            }
            playerData.openContainer = container;
        }
        screen1.resize(renderWindow.getWindowWidth(),renderWindow.getWindowHeight());
        sendPacket(new COpenScreen(screen1));
    }

    public void openScreen(Container container) {

        //glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        ContainerScreen<?> containerScreen = container.getContainerScreen();
        gameInstance.EVENT_BUS.postEvent(new OpenScreenEvent(containerScreen,screen));
        if(screen != null) {
            screen.close(true);
        }
        screen = containerScreen;
        containerScreen.setContainer(container);
        if(playerData.openContainer != null) {
            playerData.openContainer.closeContainer();
        }
        playerData.openContainer = container;
    }

    public void openScreen(String screenName) {
        ScreenBuilder screenBuilder = gameInstance.SCREEN_BUILDERS.get(screenName);
        if(screenBuilder == null) {
            throw new RuntimeException("Failed to find screen: " + screenName);
        }
        openScreen(screenBuilder.get(this));
    }

    public void registerKeyHandlers() {

        /*
        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                clientWorld.reloadChunks();
            }
        },KeyHandler.GLFW_KEY_F9);

        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                ResourceManager.reload();
                clientWorld.reloadChunks();
            }
        },KeyHandler.GLFW_KEY_F8);
         */

        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                System.exit(1);
            }
        },KeyHandler.GLFW_KEY_F11);

        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                if(screen == null) {
                    if(playerData.creative) {
                        openScreen(new CreativeInventoryScreen());
                    } else {
                        openScreen(new InventoryScreen());
                    }
                } else if(screen instanceof ContainerScreen) {
                    closeScreen();
                }
            }
        },GLFW_KEY_E);

        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                if(screen != null) {
                    if(playerData.openContainer != null) {
                        /*
                        Slot slot = playerData.openContainer.getSlotAt((int)Camera.newX,(int)Camera.newY);
                        if(slot != null) {
                            if(KeyHandler.keyPressed[GLFW_KEY_LEFT_CONTROL]) {
                                slot.setContents(ItemStack.emptyStack());
                                sendPacket(new CDropItem(slot.id,(byte)-1));
                            } else {
                                slot.getContents().removeCount(1);
                                sendPacket(new CDropItem(slot.id,(byte)1));
                            }
                        }

                         */
                    }
                }
            }
        },GLFW_KEY_Q);

        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                screenShot = true;
            }
        }, GLFW_KEY_F2);
    }

    public void createCallbacks() {
        /*
        mouseHandler = new MouseHandler(this);

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            if(height % 2 == 1) {
                height += 1;
            }
            if(width % 2 == 1) {
                width += 1;
            }
            GL30.glViewport(0, 0, width, height);
            windowX = width;
            windowY = height;
            if(playerData.openContainer != null) {
                playerData.openContainer.resize();
            }
            if(screen != null) {
                screen.resize(windowX,windowY);
            }
        });


        glfwSetScrollCallback(window, new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                if(screen != null) {
                    screen.mouseScroll(0,0,(float)yoffset);
                }
                if(renderWorld) {
                    if(Camera.thirdPerson && KeyHandler.keyPressed[GLFW_KEY_LEFT_ALT]) {
                        Camera.addToThirdPerson((float) (yoffset / -2.0f));
                    } else {
                        if (yoffset == 1.0) {

                            playerData.handSlot--;
                            if (playerData.handSlot <= -1) {
                                playerData.handSlot = 8;
                            }
                        } else if (yoffset == -1.0) {

                            playerData.handSlot++;
                            if (playerData.handSlot >= 9) {
                                playerData.handSlot = 0;
                            }
                        }
                    }
                }
            }
        });
        glfwSetMouseButtonCallback(window, mouseHandler::invoke);
        glfwSetWindowFocusCallback(window, (window, focused) -> {
            if(!focused) {
                if(screen == null) {
                    if (renderWorld) {
                        openScreen(new EscapeScreen(client));
                    } else {
                        openScreen(new JoinScreen(client));
                    }
                }
            }
        });

         */
    }

    public static long timeSinceLastDraw = 0;
    public static float drawTime = 1000f * 1000000 / Settings.maxFps;

    public void sendPacket(PacketBase packetBase) {
         network.sendPacket(packetBase);
    }

    public DoubleBuffer getMousePos() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        //glfwGetCursorPos(window, x, y);
        return BufferUtils.createDoubleBuffer(2);
        //return BufferUtils.createDoubleBuffer(2).put(x.get()).put(y.get());
    }

    public CompoundNBTTag readUsernameAndPassword(CompoundNBTTag tag) {
         playerData.userName = tag.getFullString("username").val;
         playerData.login_token = tag.getFullString("loginToken").val;
         playerData.email = tag.getFullString("email").val;
         return tag;
    }

    public CompoundNBTTag writeUsernameAndPassword(CompoundNBTTag tag) {
         tag.putFullString("username",playerData.userName);
         tag.putFullString("loginToken",playerData.login_token);
         tag.putFullString("email",playerData.email);
         return tag;
    }

    public void saveUsernameAndPassword() {
         CompoundNBTTag tag = new CompoundNBTTag();
         writeUsernameAndPassword(tag);
         WorldLoader.save(tag,"clientData.dat");
    }

    public long getRenderTime() {
         return renderTime;
    }

    public ClientPlayerData getPlayerData() {
        return playerData;
    }

    @Override
    public Client getClient() {
        return this;
    }

    @Override
    public IWorld getWorld() {
        return newClientWorld;
    }

    @Override
    public GameInstance getGameInstance() {
        return gameInstance;
    }
}
