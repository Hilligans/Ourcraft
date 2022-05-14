package dev.Hilligans.ourcraft.Client.Rendering.Graphics.OpenGL;

import dev.Hilligans.ourcraft.Client.Camera;
import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.PlayerMovementThread;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsEngineBase;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.VertexFormat;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.GLRenderer;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.TextAtlas;
import dev.Hilligans.ourcraft.Client.Rendering.Renderer;
import dev.Hilligans.ourcraft.Client.Rendering.Screens.JoinScreen;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.ShaderManager;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.VAOManager;
import dev.Hilligans.ourcraft.Client.Rendering.World.StringRenderer;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Data.Primitives.Tuple;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Content.UnknownResourceException;
import dev.Hilligans.ourcraft.ModHandler.Events.Client.GLInitEvent;
import dev.Hilligans.ourcraft.Util.Logger;
import dev.Hilligans.ourcraft.Util.NamedThreadFactory;
import dev.Hilligans.ourcraft.World.ClientWorld;
import org.joml.Matrix4d;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.lwjgl.system.MemoryUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL43.GL_DEBUG_OUTPUT;
import static org.lwjgl.opengl.GL43.glDebugMessageCallback;

public class OpenGLEngine extends GraphicsEngineBase<OpenGLWindow, OpenglDefaultImpl> {

    public Logger logger;

    public long window;
    public int texture;

    public Client client;

    public OpenglDefaultImpl engineImpl;

    public ArrayList<OpenGLWindow> windows = new ArrayList<>();

    public HashMap<String, Integer> programs = new HashMap<>();

    public OpenGLEngine() {
        engineImpl = new OpenglDefaultImpl(this);
    }

    @Override
    public OpenGLWindow createWindow() {
        return renderWindow;
    }

    public static OpenGLWindow renderWindow;

    @Override
    public void render(OpenGLWindow window) {
        client.mouseLocked = client.screen == null;
        glGetError();
        GLRenderer.resetFrame();
        long currentTime = System.nanoTime();
        if(currentTime - Client.timeSinceLastDraw < Client.drawTime) {
            return;
        }
        window.frameTracker.count();
        Client.timeSinceLastDraw = currentTime;

        client.unloadQueue.forEach(VAOManager::destroyBuffer);
        client.unloadQueue.clear();

        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


        glUseProgram(client.shaderManager.shaderProgram);

        MatrixStack matrixStack = window.camera.getMatrix();

        matrixStack.applyColor();
        matrixStack.applyTransformation();

        MatrixStack screenStack = window.camera.getScreenStack();

        screenStack.applyColor();
        screenStack.applyTransformation();

        window.renderPipeline.render(client,matrixStack,screenStack);
    }

    @Override
    public OpenGLWindow setup() {
        System.setProperty("java.awt.headless", "true");

        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        renderWindow = new OpenGLWindow(client, this);
        windows.add(renderWindow);
        renderWindow.setup();
        GL.createCapabilities();

        client.glStarted = true;
        client.gameInstance.EVENT_BUS.postEvent(new GLInitEvent(window));

        client.shaderManager = new ShaderManager();

        setupStringRenderer("");

        glEnable(GL_DEBUG_OUTPUT);
        glDebugMessageCallback(new GLDebugMessageCallback() {
            @Override
            public void invoke(int source, int type, int id, int severity, int length, long message, long userParam) {
                System.out.println("OpenGL ERROR Callback ");
                System.out.println(new String(MemoryUtil.memCharBuffer(message,length).reset().array()));
            }
        }, 0);


        client.screen = new JoinScreen(client);
        client.screen.setWindow(renderWindow);
        client.clientWorld = new ClientWorld(client);

        glfwWindowHint(GLFW_SAMPLES, 4);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_FRONT);
        glFrontFace(GL_CW);
        glEnable(GL_PROGRAM_POINT_SIZE);

        graphicsData.build();
        TextAtlas.instance.upload(this);

        PlayerMovementThread playerMovementThread = new PlayerMovementThread(window);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, new NamedThreadFactory("player_movement"));
        executorService.scheduleAtFixedRate(playerMovementThread, 0, 5, TimeUnit.MILLISECONDS);

        return renderWindow;
    }

    @Override
    public void close() {

    }

    @Override
    public ArrayList<OpenGLWindow> getWindows() {
        return windows;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public boolean isCompatible() {
        return true;
    }

    @Override
    public OpenglDefaultImpl getDefaultImpl() {
        return engineImpl;
    }

    Tuple<String,Integer>[] programCache = new Tuple[2];
    @Override
    public synchronized int getProgram(String name) {
        if(programCache[0] != null && programCache[0].getTypeA().equals(name)) {
            return programCache[0].typeB;
        }
        if(programCache[1] != null && programCache[1].typeA.equals(name)) {
            Tuple<String,Integer> f = programCache[0];
            programCache[0] = programCache[1];
            programCache[1] = f;
            return programCache[0].typeB;
        }
        return 0;
    }

    @Override
    public void renderScreen(MatrixStack screenStack) {
    }

    @Override
    public String getResourceName() {
        return "openglEngine";
    }

    @Override
    public String getIdentifierName() {
        return "ourcraft:openglEngine";
    }

    @Override
    public String getUniqueName() {
        return "graphicsEngine.ourcraft.openglEngine";
    }
}
