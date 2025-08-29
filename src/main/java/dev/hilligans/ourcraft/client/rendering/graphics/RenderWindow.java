package dev.hilligans.ourcraft.client.rendering.graphics;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.input.InputHandler;
import dev.hilligans.ourcraft.client.input.InputHandlerProvider;
import dev.hilligans.ourcraft.client.input.key.KeyPress;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.api.*;
import dev.hilligans.ourcraft.client.rendering.graphics.implementations.FreeCamera;
import dev.hilligans.ourcraft.client.rendering.graphics.implementations.PlayerCamera;
import dev.hilligans.ourcraft.client.rendering.newrenderer.Image;
import dev.hilligans.ourcraft.client.rendering.world.StringRenderer;
import dev.hilligans.ourcraft.util.Logger;
import dev.hilligans.ourcraft.util.sections.ISection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class RenderWindow {

    public FrameTracker frameTracker = new FrameTracker();
    public RenderPipeline renderPipeline;
    public PipelineInstance pipelineInstance;

    public ICamera camera;
    public IGraphicsEngine<?,?,?> graphicsEngine;
    public Logger logger;
    public InputHandler inputHandler;
    public Client client;
    public String queuedPipeline;
    public Vector4f clearColor = new Vector4f();
    public String windowName;

    public static AtomicInteger windowID = new AtomicInteger();

    public ArrayList<Runnable> resourceCleanup = new ArrayList<>();

    public RenderWindow(IGraphicsEngine<?,?,?> graphicsEngine) {
        this.graphicsEngine = graphicsEngine;
        if(graphicsEngine != null) {
            Logger log = graphicsEngine.getLogger();
            if(log != null) {
                logger = log.withKey("window");
            }
        }
        setRenderPipeline("ourcraft:engine_loading_pipeline");
        camera = new FreeCamera(this);
        this.windowName = "window "+windowID.getAndIncrement();
    }

    public void renderPipeline(Client client, MatrixStack worldStack, MatrixStack screenStack, GraphicsContext graphicsContext) {
        renderPipeline.render(client, worldStack, screenStack, graphicsContext);
    }

    public void render(GraphicsContext graphicsContext, Client client, MatrixStack worldStack, MatrixStack screenStack) {
        ISection section = graphicsContext.getSection();
        for(RenderTask renderTask : pipelineInstance.tasks) {
            try(var $ = section.startSection(renderTask.getIdentifierName())) {
                PipelineState pipelineState = renderTask.getPipelineState();
                graphicsContext.setPipelineState(false);
                if (pipelineState != null) {
                    graphicsEngine.getDefaultImpl().setState(graphicsContext, pipelineState);
                    graphicsContext.setPipelineState(true);
                }
                renderTask.draw(this, graphicsContext, this.getGraphicsEngine(), getClient(), worldStack, screenStack, 1);
            }
        }
    }

    public void setRenderPipeline(RenderPipeline renderPipeline) {
        if(renderPipeline == null) {
            throw new NullPointerException();
        }
        this.renderPipeline = renderPipeline;
        this.pipelineInstance = this.renderPipeline.buildTargets(graphicsEngine);
        this.pipelineInstance.load(graphicsEngine.getGameInstance(), graphicsEngine, graphicsEngine.getContext());
    }

    public void setRenderPipeline(String name) {
        setRenderPipeline(graphicsEngine.getGameInstance().getExcept(name, RenderPipeline.class));
    }

    public void queueRenderPipeline(String name) {
        this.queuedPipeline = name;
    }

    public abstract long getWindowID();

    public abstract void close();

    public abstract boolean shouldClose();

    public void swapBuffers(GraphicsContext graphicsContext) {
        if(queuedPipeline != null) {
            setRenderPipeline(queuedPipeline);
            queuedPipeline = null;
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public InputHandler getInputProvider() {
        return inputHandler;
    }

    public abstract String getClipboardString();

    public abstract void setMousePosition(int x, int y);

    public abstract int getWindowWidth();

    public abstract int getWindowHeight();

    public abstract boolean isWindowFocused();

    public abstract String getWindowingName();

    public String getWindowName() {
        return windowName;
    }

    public Logger getLogger() {
        return logger;
    }

    public float getAspectRatio() {
        return (float) getWindowWidth() / getWindowHeight();
    }

    public void setMouseCursor(Image image) {}

    public void setWindowName(String name) {}

    public void setClearColor(float r, float g, float b, float a) {
        this.clearColor.set(r,g,b,a);
    }

    public Image renderToImage() {
        return null;
    }

    public StringRenderer getStringRenderer() {
        return graphicsEngine.getStringRenderer();
    }

    public void registerInput(KeyPress keyPress) {

    }

    public GraphicsContext getGraphicsContext() {
        return graphicsEngine.getContext();
    }

    public RenderWindow addResourceCleanup(Runnable runnable) {
        resourceCleanup.add(runnable);
        return this;
    }

    public void cleanup() {
        for(Runnable runnable : resourceCleanup) {
            runnable.run();
        }
        resourceCleanup.clear();
    }

    public ICamera getCamera() {
        return camera;
    }

    public void setup() {
        setupInputs();
        renderPipeline.build(this);
    }

    @NotNull
    public IDefaultEngineImpl<?,?> getEngineImpl() {
        return graphicsEngine.getDefaultImpl();
    }

    @NotNull
    public IGraphicsEngine<?,?,?> getGraphicsEngine() {
        return graphicsEngine;
    }

    public boolean hasSubWindow() {
        return false;
    }

    @Nullable
    public RenderWindow[] getSubWindows() {
        return null;
    }

    public Vector4f getViewport() {
        return new Vector4f(0, 0, getWindowWidth(), getWindowHeight());
    }

    public Vector2f getScissor() {
        return new Vector2f(0,0);
    }

    public GameInstance getGameInstance() {
        return graphicsEngine.getGameInstance();
    }

    public void setupInputs() {
        inputHandler = new InputHandler(graphicsEngine.getGameInstance(), this);
        for(InputHandlerProvider provider : graphicsEngine.getGameInstance().INPUT_HANDLER_PROVIDERS.ELEMENTS) {
            IInputProvider p = provider.getProvider(graphicsEngine.getIdentifierName(), getWindowingName());
            if(p != null) {
                p.setWindow(this, inputHandler);
                inputHandler.add(p);
            }
        }
        inputHandler.completeSetup();
    }

    public void tick() {
        for(IInputProvider provider : inputHandler.inputProviders) {
            provider.tick();
        }
        if(frameTracker.getFrame(0) == 0) {
            return;
        }
        try {
            inputHandler.tick(frameTracker.getFrame(0) / 1000f);
        } catch (Exception e) {
            System.out.println(frameTracker.getFrame(0));
        }
    }
}
