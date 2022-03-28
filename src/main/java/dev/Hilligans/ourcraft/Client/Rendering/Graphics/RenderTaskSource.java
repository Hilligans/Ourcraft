package dev.Hilligans.ourcraft.Client.Rendering.Graphics;

import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.ModHandler.Content.UnknownResourceException;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;

import java.util.HashMap;

public abstract class RenderTaskSource implements IRegistryElement {

    public HashMap<String, RenderTask> tasks = new HashMap<>();
    public RenderTask defaultTask;
    public String renderTargetName;
    public String name;
    public RenderTarget target;
    public ModContent source;

    public RenderTaskSource(String name, String renderTarget) {
        this.name = name;
        this.renderTargetName = renderTarget;
        defaultTask = getDefaultTask();
        tasks.put("default", defaultTask);
    }

    public abstract RenderTask getDefaultTask();

    @Override
    public void load(GameInstance gameInstance) {
        target = gameInstance.RENDER_TARGETS.get(renderTargetName);
        if(target == null) {
            throw new UnknownResourceException("Failed to find resource in the registry by name: " + renderTargetName, gameInstance.RENDER_TARGETS, renderTargetName, source);
        }
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getIdentifierName() {
        return source.modID + ":" + name;
    }

    @Override
    public String getUniqueName() {
        return "render_task." + source.modID + "." + name;
    }
}
