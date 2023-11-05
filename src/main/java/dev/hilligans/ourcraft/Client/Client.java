package dev.hilligans.ourcraft.Client;

import dev.hilligans.ourcraft.Client.Input.InputHandler;
import dev.hilligans.ourcraft.Client.Input.Key.KeyHandler;
import dev.hilligans.ourcraft.Client.Input.Key.KeyPress;
import dev.hilligans.ourcraft.Client.Input.Key.MouseHandler;
import dev.hilligans.ourcraft.Client.Rendering.ContainerScreen;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.OpenGL.OpenGLEngine;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.VulkanEngine;
import dev.hilligans.ourcraft.Client.Rendering.Screen;
import dev.hilligans.ourcraft.Client.Rendering.ScreenBuilder;
import dev.hilligans.ourcraft.Client.Rendering.Screens.ContainerScreens.CreativeInventoryScreen;
import dev.hilligans.ourcraft.Client.Rendering.Screens.ContainerScreens.InventoryScreen;
import dev.hilligans.ourcraft.Client.Rendering.World.Managers.ShaderManager;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Container.Container;
import dev.hilligans.ourcraft.Container.Slot;
import dev.hilligans.ourcraft.Data.Other.PlayerList;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.Item.ItemStack;
import dev.hilligans.ourcraft.ModHandler.Events.Client.ClientStartRenderingEvent;
import dev.hilligans.ourcraft.ModHandler.Events.Client.OpenScreenEvent;
import dev.hilligans.ourcraft.Network.ClientNetwork;
import dev.hilligans.ourcraft.Network.IClientPacketHandler;
import dev.hilligans.ourcraft.Network.Packet.AuthServerPackets.CGetToken;
import dev.hilligans.ourcraft.Network.Packet.Client.CCloseScreen;
import dev.hilligans.ourcraft.Network.Packet.Client.CDropItem;
import dev.hilligans.ourcraft.Network.Packet.Client.COpenScreen;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Client.Audio.SoundBuffer;
import dev.hilligans.ourcraft.Client.Audio.SoundEngine;
import dev.hilligans.ourcraft.Resource.ResourceManager;
import dev.hilligans.ourcraft.Tag.CompoundNBTTag;
import dev.hilligans.ourcraft.Util.ArgumentContainer;
import dev.hilligans.ourcraft.Util.Logger;
import dev.hilligans.ourcraft.World.NewWorldSystem.CubicWorld;
import dev.hilligans.ourcraft.World.NewWorldSystem.IWorld;
import dev.hilligans.ourcraft.World.NewWorldSystem.SimpleWorld;
import dev.hilligans.ourcraft.WorldSave.WorldLoader;
import dev.hilligans.ourcraft.Server.MultiPlayerServer;
import dev.hilligans.ourcraft.Util.Settings;
import dev.hilligans.ourcraft.World.ClientWorld;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL11;

import java.nio.DoubleBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.lwjgl.glfw.GLFW.*;

public class Client implements IClientPacketHandler {

    public long window;

    public boolean mouseLocked = false;

    public int playerId;

    public int windowX = 1600;
    public int windowY = 800;
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
    public ShaderManager shaderManager;
    public SoundEngine soundEngine;

    public int texture;

    public boolean refreshTexture = true;

    public ClientPlayerData playerData = new ClientPlayerData();
    public ClientWorld clientWorld;

    //public IWorld newClientWorld = new SimpleWorld(0,"");
    public IWorld newClientWorld = new CubicWorld(0,"", 64);

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

    public void startClient() {
        network = new ClientNetwork(gameInstance.PROTOCOLS.get("Play")).debug(argumentContainer.getBoolean("--packetTrace", false));
        authNetwork = new ClientNetwork(gameInstance.PROTOCOLS.get("Auth"));

        CompoundNBTTag tag = WorldLoader.loadTag("clientData.dat");
        if(tag != null) {
            Thread thread = new Thread(() -> {
                try {
                    authNetwork.joinServer("hilligans.dev", "25588", this);
                } catch (Exception e) {}
            });
            thread.setName("authenticate");
            thread.start();
        }
        if(tag != null) {
            readUsernameAndPassword(tag);
        }
        authNetwork.sendPacket(new CGetToken(playerData.userName, playerData.login_token));
        RenderWindow window = graphicsEngine.startEngine();
        window.setClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        soundEngine.init(window.getCamera());
        soundEngine.setAttenuationModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);
        registerKeyHandlers();

        gameInstance.EVENT_BUS.postEvent(new ClientStartRenderingEvent());
        rWindow = window;
        System.err.println("Time to start running: " + (System.currentTimeMillis() - ClientMain.startTime));
        graphicsEngine.createRenderLoop(gameInstance,window).run();
        graphicsEngine.close();
        cleanUp();
        System.exit(1);
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
        screen1.setWindow(graphicsEngine.getWindows().get(0));
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
        screen1.resize(windowX,windowY);
        sendPacket(new COpenScreen(screen1));
    }

    public void openScreen(Container container) {

        //glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        ContainerScreen<?> containerScreen = container.getContainerScreen(this);
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
                        openScreen(new CreativeInventoryScreen(client));
                    } else {
                        openScreen(new InventoryScreen(client));
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

    @Override
    public Client getClient() {
        return this;
    }

    @Override
    public IWorld getWorld() {
        return newClientWorld;
    }
}
