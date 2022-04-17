package dev.Hilligans.ourcraft.Client.Rendering.Graphics.FixedFunctionGL;

import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Client.Camera;
import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.PlayerMovementThread;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IDefaultEngineImpl;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.OpenGL.OpenGLGraphicsContainer;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.OpenGL.OpenGLWindow;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.OpenGL.OpenglDefaultImpl;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.GLRenderer;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.TextAtlas;
import dev.Hilligans.ourcraft.Client.Rendering.Renderer;
import dev.Hilligans.ourcraft.Client.Rendering.Screens.JoinScreen;
import dev.Hilligans.ourcraft.Client.Rendering.Texture;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.ShaderManager;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.VAOManager;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.WorldTextureManager;
import dev.Hilligans.ourcraft.Client.Rendering.World.StringRenderer;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Events.Client.GLInitEvent;
import dev.Hilligans.ourcraft.Util.Logger;
import dev.Hilligans.ourcraft.Util.NamedThreadFactory;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.lwjgl.system.MemoryUtil;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class FixedFunctionGLEngine implements IGraphicsEngine<OpenGLGraphicsContainer,FixedFunctionGLWindow, FixedFunctionGLDefaultImpl> {

    public FixedFunctionGLDefaultImpl engineImpl;
    public GameInstance gameInstance;
    public StringRenderer stringRenderer;

    public FixedFunctionGLEngine() {
        engineImpl = new FixedFunctionGLDefaultImpl();
    }

    @Override
    public FixedFunctionGLWindow createWindow() {
        return null;
    }

    @Override
    public OpenGLGraphicsContainer getChunkGraphicsContainer(Chunk chunk) {
        return null;
    }

    @Override
    public OpenGLGraphicsContainer createChunkGraphicsContainer() {
        return null;
    }

    @Override
    public void putChunkGraphicsContainer(Chunk chunk, OpenGLGraphicsContainer container) {

    }

    @Override
    public void render(FixedFunctionGLWindow window) {

        Client client = window.getClient();
        client.mouseLocked = client.screen == null;
        glGetError();
        GLRenderer.resetFrame();
        long currentTime = System.nanoTime();
        if(currentTime - Client.timeSinceLastDraw < Client.drawTime) {
            return;
        }
        window.frameTracker.count();
        Client.timeSinceLastDraw = currentTime;

        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


        MatrixStack matrixStack = Camera.getWorldStack();
        matrixStack.applyColor();
        matrixStack.applyTransformation();

        MatrixStack screenStack = Camera.getScreenStack();
        screenStack.applyColor();
        screenStack.applyTransformation();

        client.draw(window, matrixStack,screenStack);
    }

    @Override
    public void renderWorld(MatrixStack matrixStack, ClientWorld world) {

    }

    @Override
    public void renderScreen(MatrixStack screenStack) {

    }



    @Override
    public FixedFunctionGLWindow setup() {

        Client client = ClientMain.getClient();



        glfwInit();
      //  glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,3);
      //  glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,3);
    //    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.out));

        FixedFunctionGLWindow renderWindow = new FixedFunctionGLWindow(client, this);
        renderWindow.setup();
        GL.createCapabilities();

        client.glStarted = true;
        client.shaderManager = new ShaderManager();

        glfwMakeContextCurrent(renderWindow.window);

        setupStringRenderer("");

        client.screen = new JoinScreen(client);
        client.screen.setWindow(renderWindow);
        client.clientWorld = new ClientWorld(client);

        glfwWindowHint(GLFW_SAMPLES, 4);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_DEPTH_TEST);
      //  glEnable(GL_CULL_FACE);
     //   glCullFace(GL_FRONT);
        glFrontFace(GL_CW);

        return renderWindow;
    }

    @Override
    public void close() {

    }

    @Override
    public ArrayList<FixedFunctionGLWindow> getWindows() {
        return null;
    }

    @Override
    public GameInstance getGameInstance() {
        return gameInstance;
    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public boolean isCompatible() {
        return true;
    }

    @Override
    public FixedFunctionGLDefaultImpl getDefaultImpl() {
        return engineImpl;
    }

    @Override
    public StringRenderer getStringRenderer() {
        return stringRenderer;
    }

    @Override
    public void setupStringRenderer(String defaultLanguage) {
        stringRenderer = new StringRenderer(this);
    }

    @Override
    public void load(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    @Override
    public String getResourceName() {
        return "fixedFunctionOpenglEngine";
    }

    @Override
    public String getIdentifierName() {
        return "ourcraft:fixedFunctionOpenglEngine";
    }

    @Override
    public String getUniqueName() {
        return "graphicsEngine.ourcraft.fixedFunctionOpenglEngine";
    }
}
