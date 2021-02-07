package Hilligans;

import Hilligans.Block.Block;
import Hilligans.Client.*;
import Hilligans.Client.Rendering.Screen;
import Hilligans.Client.Rendering.Screens.EscapeScreen;
import Hilligans.Client.Rendering.Screens.InventoryScreen;
import Hilligans.Client.Rendering.World.Renderer;
import Hilligans.Client.Rendering.World.ShaderManager;
import Hilligans.Client.Rendering.World.TextureManager;
import Hilligans.Entity.Entity;
import Hilligans.Util.Settings;
import Hilligans.Util.Util;
import Hilligans.Block.Blocks;
import Hilligans.Client.Key.KeyHandler;
import Hilligans.Client.Key.KeyPress;
import Hilligans.Client.Rendering.Screens.JoinScreen;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.ClientNetworkInit;
import Hilligans.Network.Packet.Client.CSendBlockChanges;
import Hilligans.Client.Camera;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Block.BlockState;
import Hilligans.World.ClientWorld;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;

import java.nio.DoubleBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class ClientMain {

    public static long window;

    public static int playerId;

    public static int windowX = 1600;
    public static int windowY = 800;
    public static boolean joinServer = true;
    public static boolean valid = false;

    public static Screen screen;


    public static int shaderProgram;
    public static int colorShader;
    static int texture;

    public static int outLine;

    public static ClientWorld clientWorld;

    public static String name = "";

    public static void main(String[] args) {

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

        Blocks.generateTextures();

        outLine = TextureManager.loadAndRegisterTexture("outline.png");
        PlayerEntity.imageId = TextureManager.loadAndRegisterTexture("player.png");
        Renderer.cursorId = TextureManager.loadAndRegisterTexture("cursor.png");

        StringRenderer.instance.loadCharacters1();

        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                if(screen == null) {
                    openScreen(new EscapeScreen());
                    //glfwSetWindowShouldClose(window, true);
                } else {
                    closeScreen();
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
                    openScreen(new InventoryScreen());
                } else if(screen instanceof InventoryScreen) {
                    closeScreen();
                }
            }
        },GLFW_KEY_E);

        screen = new JoinScreen();


        texture = TextureManager.instance.registerTexture();

        clientWorld = new ClientWorld();

        shaderProgram = ShaderManager.registerShader(Util.shader,Util.fragmentShader1);
        colorShader = ShaderManager.registerShader(Util.coloredShader,Util.fragmentShader1);
        Renderer.register();
        Entity.register();


        glEnable(GL_DEPTH);
        if(!Settings.renderTransparency) {
            shaderProgram = ShaderManager.registerShader(Util.shader,Util.fragmentShader2);
            colorShader = ShaderManager.registerShader(Util.coloredShader,Util.fragmentShader2);



        } else {
        }

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);


        glCullFace(GL_FRONT);
        glFrontFace(GL_CW);

        PlayerMovementThread playerMovementThread = new PlayerMovementThread(window);

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(playerMovementThread, 0, 5, TimeUnit.MILLISECONDS);

        //Renderer.create(StringRenderer.instance.mappedCharacters);
        Renderer.create(texture);

        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        createCallbacks();

        while(!glfwWindowShouldClose(window)) {
            //processInput(window);

            mouseLocked = screen == null;
            render();

            countFPS();

            if(mouseLocked) {
                Camera.updateMouse();
            }
        }
        ClientNetworkInit.close();
        glfwTerminate();
        System.exit(1);


    }

    public static void render() {

        //System.out.println("RENDERING");

        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glUseProgram(shaderProgram);
        glBindTexture(GL_TEXTURE_2D, texture);
        MatrixStack matrixStack = Camera.getWorldStack();
        matrixStack.applyColor();
        MatrixStack screenStack = Camera.getScreenStack();
        screenStack.applyColor();


        clientWorld.tick();
        clientWorld.render(matrixStack);

        StringRenderer.drawString(screenStack,Camera.getString(),windowX/2,0,0.5f);
        StringRenderer.drawString(screenStack,"FPS:" + fps,windowX/2,29,0.5f);
        StringRenderer.drawString(screenStack,clientWorld.biomeMap.getBiome((int)Camera.pos.x,(int)Camera.pos.z).name,windowX/2,58,0.5f);

        ChatWindow.render1(screenStack);

        if(screen != null) {
            screen.render(screenStack);
        } else {
            BlockPlacer.render(screenStack);
        }




        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    static double lastTime = glfwGetTime();
    static int nbFrames = 0;

    static int fps;

    static void countFPS() {

            double currentTime = glfwGetTime();
            nbFrames++;
            if ( currentTime - lastTime >= 1.0 ){ // If last prinf() was more than 1 sec ago

                //System.out.println("fps " + (nbFrames));
                fps = nbFrames;
                nbFrames = 0;
                lastTime += 1.0;
            }
    }

    public static void processInput(long window)
    {
        if(screen == null) {
            if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS)
                Camera.strafeLeft();
            if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS)
                Camera.moveDown();
            if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS)
                Camera.strafeRight();
            if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS)
                Camera.moveUp();
            if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)
                Camera.moveForeWard();
            if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS)
                Camera.moveBackWard();
        }
    }

    static boolean mouseLocked = false;

    public static void joinServer() {
        try {
            ClientNetworkInit.joinServer("localhost", "25586");
            //ClientNetworkInit.joinServer("198.100.150.46", "25586");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeScreen() {
        if(screen != null) {
            glfwSetCursorPos(window, (double)windowX / 2, (double)windowY / 2);
            screen.close();
            screen = null;
        }
    }

    public static void openScreen(Screen screen1) {
        screen = screen1;
    }

    public static void createCallbacks() {

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
        });


        glfwSetScrollCallback(window, new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                if(yoffset == 1.0) {
                    BlockPlacer.increase();
                } else if(yoffset == -1.0) {
                    BlockPlacer.decrease();
                }
            }
        });


        glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if(button == GLFW_MOUSE_BUTTON_1) {
                    if (action == GLFW_PRESS) {
                        if(screen == null) {
                            BlockPos pos = clientWorld.traceBlockToBreak(Camera.pos.x, Camera.pos.y, Camera.pos.z,Camera.pitch,Camera.yaw);
                            if(pos != null) {
                                if (joinServer) {
                                    ClientNetworkHandler.sendPacket(new CSendBlockChanges(pos.x, pos.y, pos.z, Blocks.AIR.id));
                                   // clientWorld.entities.put(100,new ItemEntity(pos.x,pos.y,pos.z,100,clientWorld.getBlockState(pos).block));
                                } else {
                                    clientWorld.setBlockState(pos, new BlockState(Blocks.AIR));
                                }
                            }

                        } else {
                            DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
                            DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

                            glfwGetCursorPos(window, x, y);
                            screen.mouseClick((int)x.get(),(int)y.get());
                        }
                    }

                } else if(button == GLFW_MOUSE_BUTTON_2) {
                    if (action == GLFW_PRESS) {
                        BlockPos pos = clientWorld.traceBlock(Camera.pos.x, Camera.pos.y, Camera.pos.z,Camera.pitch,Camera.yaw);
                        if(pos != null) {
                            Block block = Blocks.getBlockWithID(BlockPlacer.id);
                            if(block.getAllowedMovement1(new Vector3f(),Camera.pos,pos,Camera.playerBoundingBox)) {
                                if (joinServer) {
                                    ClientNetworkHandler.sendPacket(new CSendBlockChanges(pos.x, pos.y, pos.z, BlockPlacer.id));
                                    clientWorld.setBlockState(pos, new BlockState(block));
                                } else {
                                    clientWorld.setBlockState(pos, new BlockState(block));
                                }
                            }
                        }
                    }
                }
            }
        });
    }


}
