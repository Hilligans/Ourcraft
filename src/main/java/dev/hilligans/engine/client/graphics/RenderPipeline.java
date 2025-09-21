package dev.hilligans.engine.client.graphics;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.application.IClientApplication;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsElement;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.util.argument.Argument;
import dev.hilligans.engine.util.registry.IRegistryElement;

import java.util.ArrayList;
import java.util.HashSet;

public class RenderPipeline implements IRegistryElement, IGraphicsElement {

    public static Argument<Boolean> noRender = Argument.existArg("--noRender");

    public String name;
    public ModContainer owner;
   // public RenderWindow window;

    public ArrayList<RenderTarget> renderTargets = new ArrayList<>();
    public ArrayList<RenderTarget> stagedRenderTargets = new ArrayList<>();


    public RenderPipeline(String name) {
        this.name = name;
    }

    public void render(IClientApplication client, MatrixStack worldStack, MatrixStack screenStack, GraphicsContext graphicsContext) {
        client.getRenderWindow().render(graphicsContext, client, worldStack, screenStack);
    }

    public void stageRenderTarget(RenderTarget renderTarget) {
        stagedRenderTargets.add(renderTarget);
    }

    public void addRenderTarget(RenderTarget renderTarget) {
        if(renderTarget.after != null) {
            int x = 0;
            for(RenderTarget target : renderTargets) {
                x++;
                if(target.name.equals(renderTarget.after) && target.owner.getModID().equals(renderTarget.targetedMod)) {
                    renderTargets.add(x, renderTarget);
                    return;
                }
            }
            throw new RuntimeException("Unknown render target: " + renderTarget.after + ":" + renderTarget.targetedMod);
        }

        if(renderTarget.before != null) {
            int x = 0;
            for(RenderTarget target : renderTargets) {
                if(target.name.equals(renderTarget.before) && target.owner.getModID().equals(target.targetedMod)) {
                    renderTargets.add(x, renderTarget);
                    return;
                }
                x++;
            }
            throw new RuntimeException("Unknown render target: " + renderTarget.before + ":" + renderTarget.targetedMod);
        }

        renderTargets.add(renderTarget);
    }

    public void build(RenderWindow window) {
        //this.window = window;
    }

    public PipelineInstance buildTargets(IGraphicsEngine<?, ?, ?> graphicsEngine) {
        ArrayList<RenderTask<?>> tasks = new ArrayList<>();

        ArrayList<RenderTarget> tempList = new ArrayList<>();
        HashSet<String> targets = new HashSet<>();
        int size;
        while((size = stagedRenderTargets.size()) != 0) {
            for(RenderTarget renderTarget : stagedRenderTargets) {
                if(renderTarget.before == null && renderTarget.after == null) {
                    targets.add(renderTarget.name + ":" + renderTarget.getResourceOwner());
                    addRenderTarget(renderTarget);
                    continue;
                }

                if(renderTarget.before != null && renderTarget.after != null) {
                    if(targets.contains(renderTarget.before + ":" + renderTarget.targetedMod) && targets.contains(renderTarget.after + ":" + renderTarget.targetedMod)) {
                        targets.add(renderTarget.name + ":" + renderTarget.getResourceOwner());
                        addRenderTarget(renderTarget);
                        continue;
                    }
                } else {
                    if (renderTarget.before != null && targets.contains(renderTarget.before + ":" + renderTarget.targetedMod)) {
                        targets.add(renderTarget.name + ":" + renderTarget.getResourceOwner());
                        addRenderTarget(renderTarget);
                        continue;
                    }
                    if (renderTarget.after != null && targets.contains(renderTarget.after + ":" + renderTarget.targetedMod)) {
                        targets.add(renderTarget.name + ":" + renderTarget.getResourceOwner());
                        addRenderTarget(renderTarget);
                        continue;
                    }
                }

                //alternate order to hopefully reduce cases where the user defines them in a backwards order
                tempList.addFirst(renderTarget);
            }

            if(tempList.size() == size) {
                throw new RuntimeException("Failed to build pipeline: " + getIdentifierName() + ", unlinkable dependencies: " + tempList.stream().map(IRegistryElement::getIdentifierName).toList());
            }

            ArrayList<RenderTarget> stage = this.stagedRenderTargets;
            stage.clear();
            this.stagedRenderTargets = tempList;
            tempList = stage;
        }

        for(RenderTarget renderTarget : renderTargets) {
            RenderTaskSource renderTask = owner.getGameInstance().RENDER_TASK.getExcept(renderTarget.getRenderTask());
            tasks.add(renderTask.getTask(graphicsEngine.getIdentifierName()));
        }
        return new PipelineInstance(this, tasks);
    }

    @Override
    public void assignOwner(ModContainer owner) {
        this.owner = owner;
    }

    @Override
    public void preLoad(GameInstance gameInstance) {
        if(!noRender.get(gameInstance)) {
            for (RenderTarget renderTarget : gameInstance.RENDER_TARGETS.ELEMENTS) {
                if (getIdentifierName().equals(renderTarget.renderPipeline)) {
                    stageRenderTarget(renderTarget);
                }
            }
        }
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getResourceOwner() {
        return owner.getModID();
    }

    @Override
    public String getResourceType() {
        return "render_pipeline";
    }

    @Override
    public String toString() {
        return "RenderPipeline{" +
                "name='" + name + '\'' +
               // ", window=" + window +
                ", renderTargets=" + renderTargets +
                '}';
    }

    @Override
    public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
    }

    @Override
    public void cleanup(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
    }
}
