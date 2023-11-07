package dev.hilligans.ourcraft.client.rendering.graphics;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.mod.handler.content.UnknownResourceException;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

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

    public RenderTask getTask(String engineName) {
        return getDefaultTask();
    }

    @Override
    public void load(GameInstance gameInstance) {
        target = gameInstance.RENDER_TARGETS.get(renderTargetName);
        if(target == null) {
            throw new UnknownResourceException("Failed to find resource in the registry by name: " + renderTargetName, gameInstance.RENDER_TARGETS, renderTargetName, source);
        }
    }

    @Override
    public void assignModContent(ModContent modContent) {
        this.source = modContent;
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
