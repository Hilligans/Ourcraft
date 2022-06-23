package dev.Hilligans.ourcraft.Client;

import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Client.Input.InputHandler;
import dev.Hilligans.ourcraft.Client.Input.Key.KeyHandler;
import dev.Hilligans.ourcraft.Client.Input.Key.KeyPress;
import dev.Hilligans.ourcraft.Client.Input.Key.MouseHandler;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.OpenGL.OpenGLEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.GLRenderer;
import dev.Hilligans.ourcraft.Client.Rendering.Screens.ContainerScreens.CreativeInventoryScreen;
import dev.Hilligans.ourcraft.Client.Rendering.Screens.ContainerScreens.InventoryScreen;
import dev.Hilligans.ourcraft.Client.Rendering.Screens.EscapeScreen;
import dev.Hilligans.ourcraft.Client.Rendering.Screens.JoinScreen;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.ShaderManager;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.VAOManager;
import dev.Hilligans.ourcraft.Client.Rendering.World.StringRenderer;
import dev.Hilligans.ourcraft.Container.Container;
import dev.Hilligans.ourcraft.Container.Slot;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.Hilligans.ourcraft.Data.Other.PlayerList;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.Item.ItemStack;
import dev.Hilligans.ourcraft.Network.ClientNetwork;
import dev.Hilligans.ourcraft.Network.Packet.AuthServerPackets.CGetToken;
import dev.Hilligans.ourcraft.Network.Packet.Client.CCloseScreen;
import dev.Hilligans.ourcraft.Network.Packet.Client.CDropItem;
import dev.Hilligans.ourcraft.Network.Packet.Client.COpenScreen;
import dev.Hilligans.ourcraft.Network.PacketBase;
import dev.Hilligans.ourcraft.Client.Audio.SoundBuffer;
import dev.Hilligans.ourcraft.Client.Audio.SoundEngine;
import dev.Hilligans.ourcraft.Client.Rendering.*;
import dev.Hilligans.ourcraft.ModHandler.Events.Client.*;
import dev.Hilligans.ourcraft.Resource.ResourceManager;
import dev.Hilligans.ourcraft.Tag.CompoundNBTTag;
import dev.Hilligans.ourcraft.Util.Logger;
import dev.Hilligans.ourcraft.WorldSave.WorldLoader;
import dev.Hilligans.ourcraft.Server.MultiPlayerServer;
import dev.Hilligans.ourcraft.Util.Settings;
import dev.Hilligans.ourcraft.World.ClientWorld;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.openal.AL11;
import org.lwjgl.opengl.GL30;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Client {

    public long window;

    public boolean mouseLocked = false;

    public int playerId;
    public ConcurrentLinkedQueue<Integer> unloadQueue = new ConcurrentLinkedQueue<>();

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

    public MultiPlayerServer multiPlayerServer;
    public boolean rendering = false;

    public ClientNetwork network;
    public ClientNetwork authNetwork;
    public GameInstance gameInstance;

    public IGraphicsEngine<?,?> graphicsEngine;
    public InputHandler inputHandler;
    public IInputProvider mouseBind;

    public Client(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        logger = gameInstance.LOGGER.withKey("client");
        graphicsEngine = gameInstance.GRAPHICS_ENGINES.get("ourcraft:openglEngine");
        ((OpenGLEngine)graphicsEngine).client = this;
        soundEngine = new SoundEngine(gameInstance);
    }

    public Client setGraphicsEngine(IGraphicsEngine<?,?> graphicsEngine) {
        if(graphicsEngine != null) {
            this.graphicsEngine = graphicsEngine;
            System.out.println(graphicsEngine.getResourceName());
        }
        return this;
    }

    public void startClient() {
        network = new ClientNetwork(gameInstance.PROTOCOLS.get("Play"));
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
        soundEngine.init();
        soundEngine.setAttenuationModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);
        registerKeyHandlers();

        gameInstance.EVENT_BUS.postEvent(new ClientStartRenderingEvent());
        rWindow = window;
        graphicsEngine.createRenderLoop(gameInstance,window).run();

        cleanUp();
        System.exit(1);
    }

    public RenderWindow rWindow;

    public void cleanUp() {
        glfwTerminate();
        soundEngine.cleanup();
        for(SoundBuffer soundBuffer : gameInstance.SOUNDS.ELEMENTS) {
            soundBuffer.cleanup();
        }
    }

    public void draw(RenderWindow window, MatrixStack matrixStack, MatrixStack screenStack) {
        gameInstance.EVENT_BUS.postEvent(new RenderStartEvent(matrixStack,screenStack,this));
        if(renderWorld && !gameInstance.REBUILDING.get()) {
            gameInstance.EVENT_BUS.postEvent(new RenderWorldEvent(matrixStack,screenStack,this));
            rendering = true;
            clientWorld.tick();
           // graphicsEngine.renderWorld(matrixStack,clientWorld);

            BlockPos pos = clientWorld.traceBlockToBreak(Camera.pos.x, Camera.pos.y + Camera.playerBoundingBox.eyeHeight, Camera.pos.z, Camera.pitch, Camera.yaw);
            BlockState blockState = null;
            if (pos != null) {
                blockState = clientWorld.getBlockState(pos);
                int id = blockState.getBlock().blockProperties.blockShape.generateOutline(clientWorld, pos);
                //glUseProgram(shaderManager.lineShader);
                GL30.glBindVertexArray(id);
                matrixStack.push();
                //matrixStack.translateMinusOffset(pos.x, pos.y, pos.z);
                matrixStack.applyTransformation(shaderManager.lineShader);
                GLRenderer.glDrawElements(GL_LINES, 24, GL_UNSIGNED_INT, 0);
                matrixStack.pop();
                VAOManager.destroyBuffer(id);
            }
        }
        gameInstance.EVENT_BUS.postEvent(new RenderEndEvent(matrixStack,screenStack,this));
    }

    public int chunks = 0;

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

        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
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
                    }
                } else {

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
    }

    public static long timeSinceLastDraw = 0;
    public static float drawTime = 1000f * 1000000 / Settings.maxFps;

    public void processInput(long window) {
        if(screen == null) {
            if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS)
                Camera.strafeLeft();
            if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS)
                Camera.strafeRight();
            if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)
                Camera.moveForeWard();
            if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS)
                Camera.moveBackWard();
            if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS)
                Camera.moveUp();
            if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS)
                Camera.moveDown();
        }
    }

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


}
