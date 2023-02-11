package dev.hilligans.ourcraft.Client.Rendering.Graphics.OpenGL;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsContext;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsEngineBase;
import dev.hilligans.ourcraft.Client.Rendering.NewRenderer.GLRenderer;
import dev.hilligans.ourcraft.Client.Rendering.Screens.JoinScreen;
import dev.hilligans.ourcraft.Client.Rendering.World.Managers.ShaderManager;
import dev.hilligans.ourcraft.Data.Primitives.Tuple;
import dev.hilligans.ourcraft.ModHandler.Events.Client.GLInitEvent;
import dev.hilligans.ourcraft.Util.Logger;
import dev.hilligans.ourcraft.World.ClientWorld;
import org.joml.Matrix3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.MemoryUtil;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL43.*;

public class OpenGLEngine extends GraphicsEngineBase<OpenGLWindow, OpenglDefaultImpl, GraphicsContext> {

    public Logger logger;

    public long window;
    public int texture;

    public Client client;

    public OpenglDefaultImpl engineImpl;


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

        glClearColor(window.clearColor.x, window.clearColor.y, window.clearColor.z, window.clearColor.w);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        window.camera.tick();
        MatrixStack matrixStack = window.camera.getMatrix();
        //matrixStack.applyColor();

        MatrixStack screenStack = window.camera.getScreenStack();
        //screenStack.applyColor();

        window.renderPipeline.render(client,matrixStack,screenStack, new GraphicsContext());
    }



    @Override
    public OpenGLWindow setup() {
        System.setProperty("java.awt.headless", "true");

        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        renderWindow = new OpenGLWindow(client, this, "Ourcraft", 1600, 800);
        renderWindow.setRenderPipeline(gameInstance.ARGUMENTS.getString("--renderPipeline", "ourcraft:new_world_pipeline"));
        windows.add(renderWindow);
        renderWindow.setup();
        GL.createCapabilities();

        client.glStarted = true;
        client.gameInstance.EVENT_BUS.postEvent(new GLInitEvent(window));
        gameInstance.build(this);
        client.shaderManager = new ShaderManager();

        setupStringRenderer("");

        glEnable(GL_DEBUG_OUTPUT);

        glDebugMessageCallback(new GLDebugMessageCallback() {
            @Override
            public void invoke(int source, int type, int id, int severity, int length, long message, long userParam) {
                if(severity == GL43.GL_DEBUG_SEVERITY_NOTIFICATION) {
                    System.out.println(MemoryUtil.memUTF8(message));
                } else {
                    System.err.println(MemoryUtil.memUTF8(message));
                }
            }
        }, 0);


        client.screen = new JoinScreen(client);
        client.screen.setWindow(renderWindow);
        client.clientWorld = new ClientWorld(client);

        glfwWindowHint(GLFW_SAMPLES, 4);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_FRONT);
        glFrontFace(GL_CW);
        glEnable(GL_PROGRAM_POINT_SIZE);
       // glDisable();

        graphicsData.build();
        //TextAtlas.instance.upload(this);

        return renderWindow;
    }

    @Override
    public void close() {
        glfwTerminate();
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
