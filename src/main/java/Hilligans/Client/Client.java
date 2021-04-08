package Hilligans.Client;

import Hilligans.Block.BlockTypes.ColorBlock;
import Hilligans.Block.Blocks;
import Hilligans.Client.Key.KeyHandler;
import Hilligans.Client.Key.KeyPress;
import Hilligans.Client.Rendering.*;
import Hilligans.Client.Rendering.Screens.ContainerScreens.CreativeInventoryScreen;
import Hilligans.Client.Rendering.Screens.ContainerScreens.InventoryScreen;
import Hilligans.Client.Rendering.Screens.EscapeScreen;
import Hilligans.Client.Rendering.Screens.JoinScreen;
import Hilligans.Client.Rendering.Widgets.Widget;
import Hilligans.Client.Rendering.World.Managers.ShaderManager;
import Hilligans.Client.Rendering.World.Managers.VAOManager;
import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.Container.Container;
import Hilligans.Container.Slot;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.BlockState;
import Hilligans.Data.Other.ServerSidedData;
import Hilligans.Entity.Entity;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.ModHandler.Events.GLInitEvent;
import Hilligans.ModHandler.Events.OpenScreenEvent;
import Hilligans.Item.ItemStack;
import Hilligans.Network.ClientAuthNetworkHandler;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.Packet.AuthServerPackets.CGetToken;
import Hilligans.Network.Packet.Client.*;
import Hilligans.Network.PacketBase;
import Hilligans.Ourcraft;
import Hilligans.Tag.CompoundTag;
import Hilligans.Tag.Tag;
import Hilligans.Util.Settings;
import Hilligans.World.ClientWorld;
import Hilligans.WorldSave.WorldLoader;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;

import java.nio.DoubleBuffer;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_CW;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Client {

    public long window;

    public boolean mouseLocked = false;

    public int playerId;

    public int windowX = 1600;
    public int windowY = 800;
    public boolean joinServer = true;
    public boolean valid = false;
    public boolean screenShot = false;
    public boolean renderWorld = false;

    public Screen screen;

    public ShaderManager shaderManager;

    public int texture;

    public boolean refreshTexture = false;

    public ClientPlayerData playerData = new ClientPlayerData();


    public ClientWorld clientWorld;

    public Client() {}

    public void startClient() {
        register();
        CompoundTag tag = WorldLoader.loadTag("clientData.dat");
        if(tag != null) {
            readUsernameAndPassword(tag);
            ClientAuthNetworkHandler.sendPacketDirect(new CGetToken(playerData.userName,playerData.login_token));
        }

        createGL();
        registerKeyHandlers();
        createCallbacks();



        while(!glfwWindowShouldClose(window)) {
            mouseLocked = screen == null;
            render();
            glfwPollEvents();
            Camera.updateMouse();
        }
        ClientNetworkHandler.close();
        glfwTerminate();
        System.exit(1);
    }


    public static void register() {
        PacketBase.register();
        Container.register();
        Tag.register();
        Widget.register();
        Entity.register();

    }

    public void createGL() {
        System.setProperty("java.awt.headless", "true");

        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        window = glfwCreateWindow(windowX,windowY,"Ourcraft",NULL,NULL);
        if(window == NULL) {
            System.out.println("Failed to create window");
            glfwTerminate();
            return;
        }
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        Ourcraft.EVENT_BUS.postEvent(new GLInitEvent(window));

        shaderManager = new ShaderManager();

        Blocks.generateTextures();
        PlayerEntity.imageId = WorldTextureManager.loadAndRegisterTexture("player.png");
        StringRenderer.instance.loadCharacters1();
        for(Texture texture : Textures.TEXTURES) {
            texture.register();
        }
        screen = new JoinScreen();
        texture = WorldTextureManager.instance.registerTexture();
        Renderer.create(texture);
        clientWorld = new ClientWorld();

        glEnable(GL_DEPTH);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_FRONT);
        glFrontFace(GL_CW);
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);

        PlayerMovementThread playerMovementThread = new PlayerMovementThread(window);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(playerMovementThread, 0, 5, TimeUnit.MILLISECONDS);
    }

    public void render() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - timeSinceLastDraw < drawTime) {
            return;
        }
        countFPS();
        timeSinceLastDraw = currentTime;
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if(refreshTexture) {
            Blocks.generateTextures();
            texture = WorldTextureManager.instance.registerTexture();
            refreshTexture = false;
        }

        ServerSidedData.getInstance().tick();

        glUseProgram(shaderManager.shaderProgram);
        glBindTexture(GL_TEXTURE_2D, texture);
        MatrixStack matrixStack = Camera.getWorldStack();
        matrixStack.applyColor();
        matrixStack.applyTransformation();

        MatrixStack screenStack = Camera.getScreenStack();
        screenStack.applyColor();
        screenStack.applyTransformation();


        if(renderWorld) {
            clientWorld.tick();
            clientWorld.render(matrixStack);

            BlockPos pos = clientWorld.traceBlockToBreak(Camera.pos.x, Camera.pos.y + Camera.playerBoundingBox.eyeHeight, Camera.pos.z, Camera.pitch, Camera.yaw);
            if (pos != null) {
                BlockState blockState = clientWorld.getBlockState(pos);
                int id = blockState.getBlock().blockShape.generateOutline(clientWorld, pos);
                glUseProgram(shaderManager.lineShader);
                GL30.glBindVertexArray(id);
                matrixStack.push();
                matrixStack.translate(pos.x, pos.y, pos.z);
                matrixStack.applyTransformation(shaderManager.lineShader);
                glDrawElements(GL_LINES, 24, GL_UNSIGNED_INT, 0);
                matrixStack.pop();
                VAOManager.destroyBuffer(id);
            }

            glUseProgram(shaderManager.shaderProgram);

            if (playerData.f3) {
                StringRenderer.drawString(screenStack, Camera.getString(), windowX / 2, 0, 0.5f);
                StringRenderer.drawString(screenStack, "FPS:" + fps, windowX / 2, 29, 0.5f);
                StringRenderer.drawString(screenStack, clientWorld.biomeMap.getBiome((int) Camera.pos.x, (int) Camera.pos.z).name, windowX / 2, 58, 0.5f);
                StringRenderer.drawString(screenStack, "vel y:" + Camera.velY, windowX / 2, 87, 0.5f);
                Runtime runtime = Runtime.getRuntime();
                long usedMB = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
                StringRenderer.drawString(screenStack, "Memory:" + usedMB + "MB",windowX / 2, 126, 0.5f);
            }

            InventoryScreen.drawHotbar(screenStack);
            ChatWindow.render1(screenStack);
        }

        if(screen != null) {
            screen.render(screenStack);
            playerData.heldStack.renderStack(screenStack, (int) (Camera.newX - Settings.guiSize * 8), (int) (Camera.newY - Settings.guiSize * 8));
        }


        glfwSwapBuffers(window);

        if(screenShot) {
            screenShot = false;
            ScreenShot.takeScreenShot();
        }
    }

    public void closeScreen() {
        if(!playerData.heldStack.isEmpty()) {
            ClientNetworkHandler.sendPacketDirect(new CDropItem((short)-1,(byte)-1));
            playerData.heldStack = ItemStack.emptyStack();
        }
        if(screen != null) {
            glfwSetCursorPos(window, (double)windowX / 2, (double)windowY / 2);
            screen.close(false);
            screen = null;
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        }
        if(playerData.openContainer != null) {
            playerData.openContainer.closeContainer();
        }

        ClientNetworkHandler.sendPacketDirect(new CCloseScreen(false));
    }

    public void openScreen(Screen screen1) {
        Ourcraft.EVENT_BUS.postEvent(new OpenScreenEvent(screen1,screen));
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
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
        ClientNetworkHandler.sendPacketDirect(new COpenScreen(screen1));
    }

    public void openScreen(Container container) {
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        ContainerScreen<?> containerScreen = container.getContainerScreen();
        Ourcraft.EVENT_BUS.postEvent(new OpenScreenEvent(containerScreen,screen));
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

    public void registerKeyHandlers() {

       /*  KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                openScreen(new TagEditorScreen());
            }
        }, GLFW_KEY_H);

        */

        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                if(renderWorld) {
                    if (screen == null) {
                        openScreen(new EscapeScreen());
                    } else {
                        closeScreen();
                    }
                } else {
                    openScreen(new JoinScreen());
                }
            }
        },KeyHandler.GLFW_KEY_ESCAPE);

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
                playerData.f3 = !playerData.f3;
            }
        },GLFW_KEY_F3);

        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                if(screen != null) {
                    if(playerData.openContainer != null) {
                        Slot slot = playerData.openContainer.getSlotAt((int)Camera.newX,(int)Camera.newY);
                        if(slot != null) {
                            if(KeyHandler.keyPressed[GLFW_KEY_LEFT_CONTROL]) {
                                slot.setContents(ItemStack.emptyStack());
                                ClientNetworkHandler.sendPacketDirect(new CDropItem(slot.id,(byte)-1));
                            } else {
                                slot.getContents().removeCount(1);
                                ClientNetworkHandler.sendPacketDirect(new CDropItem(slot.id,(byte)1));
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

    public  void createCallbacks() {
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
        });


        glfwSetScrollCallback(window, new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                if(screen != null) {
                    screen.mouseScroll(0,0,(float)yoffset);
                }
                if(yoffset == 1.0) {

                    playerData.handSlot--;
                    if(playerData.handSlot <= -1) {
                        playerData.handSlot = 8;
                    }
                } else if(yoffset == -1.0) {

                    playerData.handSlot++;
                    if(playerData.handSlot >= 9) {
                        playerData.handSlot = 0;
                    }
                }
            }
        });


        glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (action == GLFW_PRESS) {
                    if (screen == null) {
                        if (button == GLFW_MOUSE_BUTTON_1) {
                            BlockPos pos = clientWorld.traceBlockToBreak(Camera.pos.x, Camera.pos.y + Camera.playerBoundingBox.eyeHeight, Camera.pos.z, Camera.pitch, Camera.yaw);
                            if (pos != null) {
                                if (joinServer) {
                                    ClientNetworkHandler.sendPacketDirect(new CSendBlockChanges(pos.x, pos.y, pos.z, Blocks.AIR.id));
                                    // clientWorld.entities.put(100,new ItemEntity(pos.x,pos.y,pos.z,100,clientWorld.getBlockState(pos).block));
                                } else {
                                    clientWorld.setBlockState(pos, Blocks.AIR.getDefaultState());
                                }
                            }

                        } else if (button == GLFW_MOUSE_BUTTON_2) {
                            BlockPos blockPos = clientWorld.traceBlockToBreak(Camera.pos.x,Camera.pos.y + Camera.playerBoundingBox.eyeHeight,Camera.pos.z,Camera.pitch,Camera.yaw);
                            if(blockPos != null) {
                                BlockState blockState = clientWorld.getBlockState(blockPos);
                                if (blockState != null && blockState.getBlock().activateBlock(clientWorld, null, blockPos)) {
                                    ClientNetworkHandler.sendPacketDirect(new CUseItem((byte) playerData.handSlot));
                                    return;
                                }
                            }
                            ItemStack itemStack = playerData.inventory.getItem(playerData.handSlot);
                            if(!itemStack.isEmpty()) {
                                if(itemStack.item.onActivate(clientWorld,null)) {
                                    ClientNetworkHandler.sendPacketDirect(new CUseItem((byte) playerData.handSlot));
                                    if(!playerData.creative) {
                                        itemStack.removeCount(1);
                                    }
                                }
                            } else {
                                ClientNetworkHandler.sendPacketDirect(new CUseItem((byte) playerData.handSlot));
                            }
                        }
                    } else {
                        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
                        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

                        glfwGetCursorPos(window, x, y);
                        screen.mouseClick((int) x.get(), (int) y.get(), button);
                    }
                }
            }
        });

        glfwSetWindowFocusCallback(window, (window, focused) -> {
            if(!focused) {
                if(screen == null) {
                    if (renderWorld) {
                        openScreen(new EscapeScreen());
                    } else {
                        openScreen(new JoinScreen());
                    }
                }
            }
        });
    }

    static long timeSinceLastDraw = 0;
    static float drawTime = 1000f / Settings.maxFps;

    double lastTime = glfwGetTime();
    int nbFrames = 0;

    int fps;

     void countFPS() {

        double currentTime = glfwGetTime();
        nbFrames++;
        if ( currentTime - lastTime >= 1.0 ){

            fps = nbFrames;
            nbFrames = 0;
            lastTime += 1.0;
        }
    }

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


    public DoubleBuffer getMousePos() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, x, y);
        return BufferUtils.createDoubleBuffer(2).put(x.get()).put(y.get());
    }

    public CompoundTag readUsernameAndPassword(CompoundTag tag) {
         playerData.userName = tag.getFullString("username").val;
         playerData.login_token = tag.getFullString("loginToken").val;
         playerData.email = tag.getFullString("email").val;
         return tag;
    }

    public CompoundTag writeUsernameAndPassword(CompoundTag tag) {
         tag.putFullString("username",playerData.userName);
         tag.putFullString("loginToken",playerData.login_token);
         tag.putFullString("email",playerData.email);
         return tag;
    }

    public void saveUsernameAndPassword() {
         CompoundTag tag = new CompoundTag();
         writeUsernameAndPassword(tag);
         WorldLoader.save(tag,"clientData.dat");
    }

}
