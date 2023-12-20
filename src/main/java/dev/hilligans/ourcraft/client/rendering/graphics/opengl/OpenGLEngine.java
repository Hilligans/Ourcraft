package dev.hilligans.ourcraft.client.rendering.graphics.opengl;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsEngineBase;
import dev.hilligans.ourcraft.client.rendering.newrenderer.GLRenderer;
import dev.hilligans.ourcraft.client.rendering.screens.JoinScreen;
import dev.hilligans.ourcraft.client.rendering.world.managers.ShaderManager;
import dev.hilligans.ourcraft.data.primitives.Tuple;
import dev.hilligans.ourcraft.mod.handler.events.client.GLInitEvent;
import dev.hilligans.ourcraft.util.Logger;
import dev.hilligans.ourcraft.util.sections.ProfiledSection;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLDebugMessageCallback;
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

    public boolean profiling = true;


    public OpenGLEngine() {
        engineImpl = new OpenglDefaultImpl(this);
    }

    @Override
    public OpenGLWindow createWindow() {
        return renderWindow;
    }

    public static OpenGLWindow renderWindow;

    @Override
    public void render(RenderWindow window, GraphicsContext graphicsContext) {
        //client.mouseLocked = client.screen == null;
        //glGetError();
        //GLRenderer.resetFrame();
        //long currentTime = System.nanoTime();
        //if(currentTime - Client.timeSinceLastDraw < Client.drawTime) {
        //    return;
        //}
        //window.frameTracker.count();
        //Client.timeSinceLastDraw = currentTime;

        glClearColor(window.clearColor.x, window.clearColor.y, window.clearColor.z, window.clearColor.w);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        window.camera.tick();
        MatrixStack matrixStack = window.camera.getMatrix();
        MatrixStack screenStack = window.camera.getScreenStack();

        window.renderPipeline(client, matrixStack, screenStack, graphicsContext);
    }

    public static int counter = 0;



    @Override
    public OpenGLWindow setup() {
        System.setProperty("java.awt.headless", "true");

        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        renderWindow = new OpenGLWindow(client, this, "Ourcraft", 1600, 800);
        renderWindow.setRenderPipeline(gameInstance.ARGUMENTS.getString("--renderPipeline", "ourcraft:menu_pipeline"));
        windows.add(renderWindow);
        renderWindow.setup();
        GL.createCapabilities();

        client.glStarted = true;
        client.gameInstance.EVENT_BUS.postEvent(new GLInitEvent(window));
        gameInstance.build(this, null);

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


        client.screen = new JoinScreen();
        client.screen.setWindow(renderWindow);

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
    public GraphicsContext getContext() {
        return new GraphicsContext();
    }


    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public GraphicsContext getGraphicsContext() {
        if(profiling) {
            return new GraphicsContext().setSection(new ProfiledSection());
        }
        return new GraphicsContext();
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
    public String getResourceOwner() {
        return "ourcraft";
    }
}
