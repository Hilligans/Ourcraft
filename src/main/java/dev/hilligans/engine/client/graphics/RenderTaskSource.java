package dev.hilligans.engine.client.graphics;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsElement;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.mod.handler.content.UnknownResourceException;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

import java.util.HashMap;

public abstract class RenderTaskSource implements IRegistryElement, IGraphicsElement {

    public HashMap<String, RenderTask> tasks = new HashMap<>();
    public RenderTask defaultTask;
    public String renderTargetName;
    public String name;
    public RenderTarget target;
    public ModContainer source;

    public RenderTaskSource(String name, String renderTarget) {
        this.name = name;
        this.renderTargetName = renderTarget;
        defaultTask = getDefaultTask();
        tasks.put("default", defaultTask);
    }

    public abstract RenderTask getDefaultTask();

    public RenderTask getTask(String engineName) {
        return getDefaultTask().setNameIdentifierName(getIdentifierName());
    }

    @Override
    public void load(GameInstance gameInstance) {
        target = gameInstance.get(renderTargetName, RenderTarget.class);
        if(target == null) {
            throw new UnknownResourceException("Failed to find resource in the registry by name: " + renderTargetName, gameInstance.RENDER_TARGETS, renderTargetName, source);
        }
    }

    @Override
    public void assignOwner(ModContainer source) {
        this.source = source;
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getResourceOwner() {
        return source.getModID();
    }

    @Override
    public String getResourceType() {
        return "render_task";
    }

    @Override
    public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
    }

    @Override
    public void cleanup(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
    }
}
