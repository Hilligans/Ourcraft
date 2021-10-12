package dev.Hilligans.Client;

import dev.Hilligans.Block.Blocks;
import dev.Hilligans.Client.Key.KeyHandler;
import dev.Hilligans.Client.Key.KeyPress;
import dev.Hilligans.Client.Lang.Languages;
import dev.Hilligans.Client.Mouse.MouseHandler;
import dev.Hilligans.Client.Rendering.NewRenderer.GLRenderer;
import dev.Hilligans.Client.Rendering.NewRenderer.TextAtlas;
import dev.Hilligans.Client.Rendering.Screens.ContainerScreens.CreativeInventoryScreen;
import dev.Hilligans.Client.Rendering.Screens.ContainerScreens.InventoryScreen;
import dev.Hilligans.Client.Rendering.Screens.EscapeScreen;
import dev.Hilligans.Client.Rendering.Screens.JoinScreen;
import dev.Hilligans.Client.Rendering.World.Managers.ShaderManager;
import dev.Hilligans.Client.Rendering.World.Managers.VAOManager;
import dev.Hilligans.Client.Rendering.World.Managers.WorldTextureManager;
import dev.Hilligans.Client.Rendering.World.StringRenderer;
import dev.Hilligans.Container.Container;
import dev.Hilligans.Container.Slot;
import dev.Hilligans.Data.Other.BlockPos;
import dev.Hilligans.Data.Other.BlockStates.BlockState;
import dev.Hilligans.Data.Other.PlayerList;
import dev.Hilligans.Entity.LivingEntities.PlayerEntity;
import dev.Hilligans.GameInstance;
import dev.Hilligans.Item.ItemStack;
import dev.Hilligans.Network.ClientNetwork;
import dev.Hilligans.Network.Packet.AuthServerPackets.CGetToken;
import dev.Hilligans.Network.Packet.Client.CCloseScreen;
import dev.Hilligans.Network.Packet.Client.CDropItem;
import dev.Hilligans.Network.Packet.Client.COpenScreen;
import dev.Hilligans.Network.PacketBase;
import dev.Hilligans.Network.Protocols;
import dev.Hilligans.Resource.ResourceManager;
import dev.Hilligans.Server.MultiPlayerServer;
import dev.Hilligans.Tag.CompoundTag;
import dev.Hilligans.Util.NamedThreadFactory;
import dev.Hilligans.Util.Settings;
import dev.Hilligans.World.ClientWorld;
import dev.Hilligans.WorldSave.WorldLoader;
import dev.Hilligans.Client.Audio.SoundBuffer;
import dev.Hilligans.Client.Audio.SoundEngine;
import dev.Hilligans.Client.Audio.Sounds;
import dev.Hilligans.Client.Rendering.*;
import dev.Hilligans.ModHandler.Events.Client.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.openal.AL11;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;

import java.nio.DoubleBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL32.GL_PROGRAM_POINT_SIZE;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Client {

    public long window;

    public boolean mouseLocked = false;

    public int playerId;
    public ConcurrentLinkedQueue<Integer> unloadQueue = new ConcurrentLinkedQueue<>();

    public int  windowX = 1600;
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

    public MouseHandler mouseHandler;
    public Screen screen;
    public ShaderManager shaderManager;
    public SoundEngine soundEngine = new SoundEngine();

    public int texture;

    public boolean refreshTexture = true;

    public ClientPlayerData playerData = new ClientPlayerData();
    public ClientWorld clientWorld;

    public MultiPlayerServer multiPlayerServer;
    public boolean rendering = false;

    public ClientNetwork network;
    public ClientNetwork authNetwork;
    public GameInstance gameInstance;

    public Client(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void startClient() {
        network = new ClientNetwork(Protocols.PLAY);
        authNetwork = new ClientNetwork(Protocols.AUTH);

        CompoundTag tag = WorldLoader.loadTag("clientData.dat");
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

        createGL();

        soundEngine.init();
        soundEngine.setAttenuationModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);
        registerKeyHandlers();
        createCallbacks();

        gameInstance.EVENT_BUS.postEvent(new ClientStartRenderingEvent());

        while(!glfwWindowShouldClose(window)) {
            mouseLocked = screen == null;

            gameInstance.EVENT_BUS.postEvent(new RenderPreEvent());
            render();
            gameInstance.EVENT_BUS.postEvent(new RenderPostEvent(this));
            glfwSwapBuffers(window);
            rendering = false;
            soundEngine.tick();
            if(screenShot) {
                screenShot = false;
                ScreenShot.takeScreenShot();
            }

            glfwPollEvents();
            Camera.updateMouse();
        }
        cleanUp();
        System.exit(1);
    }

    public void cleanUp() {
        glfwTerminate();
        soundEngine.cleanup();
        for(SoundBuffer soundBuffer : Sounds.SOUNDS) {
            soundBuffer.cleanup();
        }
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

        glStarted = true;
        gameInstance.EVENT_BUS.postEvent(new GLInitEvent(window));

        shaderManager = new ShaderManager();

        Blocks.generateTextures();
        PlayerEntity.imageId = WorldTextureManager.loadAndRegisterTexture("player.png");
        StringRenderer.instance.buildChars();

        for(Texture texture : Textures.TEXTURES) {
            texture.register();
        }
        screen = new JoinScreen(this);
        texture = -1;
        Renderer.create(texture);
        clientWorld = new ClientWorld(client);

       // glEnable(GL_DEPTH);
        glfwWindowHint(GLFW_SAMPLES, 4);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_FRONT);
        glFrontFace(GL_CW);
        glEnable(GL_PROGRAM_POINT_SIZE);
        // glEnable(GL_MULTISAMPLE);
        //glSampleCoverage();
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);

        PlayerMovementThread playerMovementThread = new PlayerMovementThread(window);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, new NamedThreadFactory("player_movement"));
        executorService.scheduleAtFixedRate(playerMovementThread, 0, 5, TimeUnit.MILLISECONDS);
    }

    public void render() {
        glGetError();
        GLRenderer.resetFrame();
        long currentTime = System.currentTimeMillis();
        if(currentTime - timeSinceLastDraw < drawTime) {
            return;
        }
        countFPS();
        timeSinceLastDraw = currentTime;

        unloadQueue.forEach(VAOManager::destroyBuffer);
        unloadQueue.clear();

        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if(refreshTexture) {
            Blocks.generateTextures();
            texture = TextAtlas.instance.upload();
            refreshTexture = false;
        }

        glUseProgram(shaderManager.shaderProgram);
        glBindTexture(GL_TEXTURE_2D, texture);
        MatrixStack matrixStack = Camera.getWorldStack();
        matrixStack.applyColor();
        matrixStack.applyTransformation();


        MatrixStack screenStack = Camera.getScreenStack();
        screenStack.applyColor();
        screenStack.applyTransformation();

        draw(matrixStack,screenStack);
    }

    public void draw(MatrixStack matrixStack, MatrixStack screenStack) {
        gameInstance.EVENT_BUS.postEvent(new RenderStartEvent(matrixStack,screenStack,this));
        if(renderWorld && !gameInstance.REBUILDING.get()) {
            gameInstance.EVENT_BUS.postEvent(new RenderWorldEvent(matrixStack,screenStack,this));
            rendering = true;
            clientWorld.tick();
            clientWorld.render(matrixStack);

            BlockPos pos = clientWorld.traceBlockToBreak(Camera.pos.x, Camera.pos.y + Camera.playerBoundingBox.eyeHeight, Camera.pos.z, Camera.pitch, Camera.yaw);
            BlockState blockState = null;
            if (pos != null) {
                blockState = clientWorld.getBlockState(pos);
                int id = blockState.getBlock().blockProperties.blockShape.generateOutline(clientWorld, pos);
                glUseProgram(shaderManager.lineShader);
                GL30.glBindVertexArray(id);
                matrixStack.push();
                matrixStack.translateMinusOffset(pos.x, pos.y, pos.z);
                matrixStack.applyTransformation(shaderManager.lineShader);
                GLRenderer.glDrawElements(GL_LINES, 24, GL_UNSIGNED_INT, 0);
                matrixStack.pop();
                VAOManager.destroyBuffer(id);
            }

            glUseProgram(shaderManager.shaderProgram);
            if (playerData.f3) {
                StringRenderer.drawString(screenStack, Camera.getString(), windowX / 2, 0, 0.5f);
                StringRenderer.drawString(screenStack, "FPS:" + fps, windowX / 2, 29, 0.5f);
                StringRenderer.drawString(screenStack, "Biome:" + clientWorld.biomeMap.getBiome((int) Camera.pos.x, (int) Camera.pos.z).name, windowX / 2, 58, 0.5f);
                StringRenderer.drawString(screenStack, "VelY:" + Camera.velY, windowX / 2, 87, 0.5f);
                Runtime runtime = Runtime.getRuntime();
                long usedMB = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
                StringRenderer.drawString(screenStack, "Memory:" + usedMB + "MB",windowX / 2, 126, 0.5f);
                StringRenderer.drawString(screenStack, "Pitch:" + Math.toDegrees(Camera.pitch),windowX/2,155,0.5f);
                StringRenderer.drawString(screenStack, "Yaw:" + Math.toDegrees(Camera.yaw),windowX/2,184,0.5f);
                StringRenderer.drawString(screenStack, "Sounds:" + soundEngine.sounds.size(),windowX/2,213,0.5f);
                StringRenderer.drawString(screenStack, "Render Calls:" + GLRenderer.drawCalls, windowX/2,242,0.5f);
                StringRenderer.drawString(screenStack, "Vertices:" + GLRenderer.count, windowX/2,271,0.5f);
                StringRenderer.drawString(screenStack, "Block:" + (blockState == null ? "null" : blockState.getBlock().getName() + ":" + blockState.readData()),windowX/2,300,0.5f);
                if(renderTime % 100 == 0) {
                    chunks = clientWorld.chunkContainer.getSize();
                }
                StringRenderer.drawString(screenStack, "Chunks:" + chunks,windowX/2,329,0.5f);
            }
            ItemStack stack = playerData.inventory.getItem(playerData.handSlot);
            if(stack != null && stack.item != null) {
                int width = (int) (32 * Settings.guiSize);
                stack.item.renderHolding(screenStack,width,stack);
            }

            InventoryScreen.drawHotbar(screenStack);
            ChatWindow.render1(screenStack);
            Camera.renderPlayer(matrixStack);
        }

        if(screen != null) {
            screen.render(screenStack);
            playerData.heldStack.renderStack(screenStack, (int) (Camera.newX - Settings.guiSize * 8), (int) (Camera.newY - Settings.guiSize * 8));
        } else {
            Textures.CURSOR.drawCenteredTexture(screenStack,1.0f);
            if(KeyHandler.keyPressed[GLFW_KEY_TAB]) {
                if(playerList != null) {
                    playerList.render(screenStack);
                }
            }
            int s = 200;
            BlockPos blockPos = new BlockPos(Camera.renderPos);


       }
        gameInstance.EVENT_BUS.postEvent(new RenderEndEvent(matrixStack,screenStack,this));
    }

    int chunks = 0;

    public void closeScreen() {
        if(!playerData.heldStack.isEmpty()) {
            sendPacket(new CDropItem((short)-1,(byte)-1));
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

        sendPacket(new CCloseScreen(false));
    }

    public void openScreen(Screen screen1) {
        gameInstance.EVENT_BUS.postEvent(new OpenScreenEvent(screen1,screen));
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

    public void registerKeyHandlers() {

        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                Languages.setCurrentLanguage("test");
            }
        },KeyHandler.GLFW_KEY_F6);


        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                if(renderWorld) {
                    if (screen == null) {
                        openScreen(new EscapeScreen(client));
                    } else {
                        closeScreen();
                    }
                } else {
                    openScreen(new JoinScreen(client));
                }
            }
        },KeyHandler.GLFW_KEY_ESCAPE);

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
                Blocks.reload();
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

    static long timeSinceLastDraw = 0;
    static float drawTime = 1000f / Settings.maxFps;

    double lastTime;
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

    public void sendPacket(PacketBase packetBase) {
         network.sendPacket(packetBase);
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

    public long getRenderTime() {
         return renderTime;
    }


}
