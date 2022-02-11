package dev.Hilligans.ourcraft.Client.Rendering.Graphics;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.ICamera;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IInputProvider;

import java.util.ArrayList;

public abstract class RenderWindow {

    public FrameTracker frameTracker = new FrameTracker();
    public ArrayList<RenderTarget> renderTargets = new ArrayList<>();

    public IInputProvider inputProvider;
    public ICamera camera;

    public abstract void close();

    public abstract boolean shouldClose();

    public abstract void swapBuffers();

    public abstract Client getClient();

    public abstract IInputProvider getInputProvider();

    public void addRenderTarget(RenderTarget renderTarget) {
        if(renderTarget.after != null) {
            int x = 0;
            for(RenderTarget target : renderTargets) {
                x++;
                if(target.name.equals(renderTarget.after) && target.modContent.modID.equals(target.targetedMod)) {
                    renderTargets.add(x, renderTarget);
                    return;
                }
            }
            throw new RuntimeException("Unknown render target: " + renderTarget.after + ":" + renderTarget.targetedMod);
        }

        if(renderTarget.before != null) {
            int x = 0;
            for(RenderTarget target : renderTargets) {
                if(target.name.equals(renderTarget.before) && target.modContent.modID.equals(target.targetedMod)) {
                    renderTargets.add(x, renderTarget);
                    return;
                }
                x++;
            }
            throw new RuntimeException("Unknown render target: " + renderTarget.before + ":" + renderTarget.targetedMod);
        }

        renderTargets.add(renderTarget);
    }
}
