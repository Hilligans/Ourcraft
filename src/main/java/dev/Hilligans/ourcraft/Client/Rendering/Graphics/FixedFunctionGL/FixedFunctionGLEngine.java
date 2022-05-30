package dev.Hilligans.ourcraft.Client.Rendering.Graphics.FixedFunctionGL;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsEngineBase;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.GLRenderer;
import dev.Hilligans.ourcraft.Client.Rendering.Screens.JoinScreen;
import dev.Hilligans.ourcraft.Client.Rendering.World.Managers.ShaderManager;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Util.Logger;
import dev.Hilligans.ourcraft.World.ClientWorld;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnable;

public class FixedFunctionGLEngine extends GraphicsEngineBase<FixedFunctionGLWindow, FixedFunctionGLDefaultImpl> {

    public FixedFunctionGLDefaultImpl engineImpl;
    public Client client;

    public FixedFunctionGLEngine() {
        engineImpl = new FixedFunctionGLDefaultImpl();
        engineImpl.engine = this;
    }

    @Override
    public FixedFunctionGLWindow createWindow() {
        return null;
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


        MatrixStack matrixStack = window.camera.getMatrix();
        matrixStack.applyColor();
        matrixStack.applyTransformation();

        MatrixStack screenStack = window.camera.getScreenStack();
        screenStack.applyColor();
        screenStack.applyTransformation();

        window.renderPipeline.render(client,matrixStack,screenStack);
    }

    @Override
    public void renderScreen(MatrixStack screenStack) {

    }



    @Override
    public FixedFunctionGLWindow setup() {

        client = ClientMain.getClient();



        glfwInit();
       // glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,1);
      //  glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR,2);
     //   glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.out));

        FixedFunctionGLWindow renderWindow = new FixedFunctionGLWindow(client, this);
        windows.add(renderWindow);
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
    public int getProgram(String name) {
        return 0;
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
