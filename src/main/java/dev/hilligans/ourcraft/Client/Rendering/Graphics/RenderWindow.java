package dev.hilligans.ourcraft.Client.Rendering.Graphics;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.Input.InputHandler;
import dev.hilligans.ourcraft.Client.Input.InputHandlerProvider;
import dev.hilligans.ourcraft.Client.Input.Key.KeyPress;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.*;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.Implementations.PlayerCamera;
import dev.hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import dev.hilligans.ourcraft.Client.Rendering.World.StringRenderer;
import dev.hilligans.ourcraft.Util.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector4f;

public abstract class RenderWindow {

    public FrameTracker frameTracker = new FrameTracker();
    public RenderPipeline renderPipeline;

    public ICamera camera;
    public IGraphicsEngine<?,?,?> graphicsEngine;
    public Logger logger;
    public InputHandler inputHandler;

    public String queuedPipeline;

    public double mouseX;
    public double mouseY;

    public RenderWindow(IGraphicsEngine<?,?,?> graphicsEngine) {
        this.graphicsEngine = graphicsEngine;
        if(graphicsEngine != null) {
            Logger log = graphicsEngine.getLogger();
            if(log != null) {
                logger = log.withKey("window");
            }
        }
        setRenderPipeline("ourcraft:menu_pipeline");
        camera = new PlayerCamera();
    }

    public void render(GraphicsContext graphicsContext, Client client, MatrixStack worldStack, MatrixStack screenStack) {
        for(RenderTask renderTask : renderPipeline.renderTasks) {
            PipelineState pipelineState = renderTask.getPipelineState();
            graphicsContext.setPipelineState(false);
            if(pipelineState != null) {
                graphicsEngine.getDefaultImpl().setState(this, graphicsContext, pipelineState);
                graphicsContext.setPipelineState(true);
            }
            renderTask.draw(this, graphicsContext, this.getGraphicsEngine(), client, worldStack, screenStack, 1);
        }
    }

    public void setRenderPipeline(RenderPipeline renderPipeline) {
        if(renderPipeline == null) {
            throw new NullPointerException();
        }
        this.renderPipeline = renderPipeline;
        this.renderPipeline.build(this);
    }

    public void setRenderPipeline(String name) {
        setRenderPipeline(graphicsEngine.getGameInstance().RENDER_PIPELINES.get(name));
    }

    public void queueRenderPipeline(String name) {
        this.queuedPipeline = name;
    }

    public abstract long getWindowID();

    public abstract void close();

    public abstract boolean shouldClose();

    public void swapBuffers() {
        if(queuedPipeline != null) {
            setRenderPipeline(queuedPipeline);
            queuedPipeline = null;
        }
    }

    public abstract Client getClient();

    public InputHandler getInputProvider() {
        return inputHandler;
    }

    public abstract float getWindowWidth();

    public abstract float getWindowHeight();

    public abstract boolean isWindowFocused();

    public abstract String getWindowingName();

    public Logger getLogger() {
        return logger;
    }

    public float getAspectRatio() {
        return getWindowWidth() / getWindowHeight();
    }

    public void setMouseCursor(Image image) {}

    public void setWindowName(String name) {}

    public void setMousePosition(float x, float y) {}

    public void setClearColor(float r, float g, float b, float a) {}

    public Image renderToImage() {
        return null;
    }

    public StringRenderer getStringRenderer() {
        return graphicsEngine.getStringRenderer();
    }

    public void registerInput(KeyPress keyPress) {

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
