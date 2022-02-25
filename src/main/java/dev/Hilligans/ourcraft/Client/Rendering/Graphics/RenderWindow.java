package dev.Hilligans.ourcraft.Client.Rendering.Graphics;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Input.InputHandler;
import dev.Hilligans.ourcraft.Client.Input.Key.KeyPress;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.ICamera;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IDefaultEngineImpl;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import dev.Hilligans.ourcraft.Util.Logger;

import java.util.ArrayList;

public abstract class RenderWindow {

    public FrameTracker frameTracker = new FrameTracker();
    public ArrayList<RenderTarget> renderTargets = new ArrayList<>();

    public ICamera camera;
    public IGraphicsEngine<?, ?, ?> graphicsEngine;
    public Logger logger;
    public InputHandler inputHandler = new InputHandler();

    public double mouseX;
    public double mouseY;

    public RenderWindow(IGraphicsEngine<?, ?, ?> graphicsEngine) {
        this.graphicsEngine = graphicsEngine;
        if(graphicsEngine != null) {
            Logger log = graphicsEngine.getLogger();
            if(log != null) {
                logger = log.withKey("window");
            }
        }
    }

    public abstract void close();

    public abstract boolean shouldClose();

    public abstract void swapBuffers();

    public abstract Client getClient();

    public InputHandler getInputProvider() {
        return inputHandler;
    }

    public abstract float getWindowWidth();

    public abstract float getWindowHeight();

    public abstract boolean isWindowFocused();

    public Logger getLogger() {
        return logger;
    }

    public float getAspectRatio() {
        return getWindowWidth() / getWindowHeight();
    }

    public void setMouseCursor(Image image) {}

    public void setWindowName(String name) {}

    public void setMousePosition(float x, float y) {}

    public Image renderToImage() {
        return null;
    }

    public void registerInput(KeyPress keyPress) {

    }

    public void addRenderTarget(RenderTarget renderTarget) {
        if(renderTarget.after != null) {
            int x = 0;
            for(RenderTarget target : renderTargets) {
                x++;
                if(target.name.equals(renderTarget.after) && target.modContent.getModID().equals(target.targetedMod)) {
                    renderTargets.add(x, renderTarget);
                    return;
                }
            }
            throw new RuntimeException("Unknown render target: " + renderTarget.after + ":" + renderTarget.targetedMod);
        }

        if(renderTarget.before != null) {
            int x = 0;
            for(RenderTarget target : renderTargets) {
                if(target.name.equals(renderTarget.before) && target.modContent.getModID().equals(target.targetedMod)) {
                    renderTargets.add(x, renderTarget);
                    return;
                }
                x++;
            }
            throw new RuntimeException("Unknown render target: " + renderTarget.before + ":" + renderTarget.targetedMod);
        }

        renderTargets.add(renderTarget);
    }

    public IDefaultEngineImpl<?> getEngineImpl() {
        return graphicsEngine.getDefaultImpl();
    }
}
