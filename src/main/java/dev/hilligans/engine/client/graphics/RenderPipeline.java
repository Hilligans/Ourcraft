package dev.hilligans.engine.client.graphics;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsElement;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

import java.util.ArrayList;

public class RenderPipeline implements IRegistryElement, IGraphicsElement {

    public String name;
    public ModContainer owner;
   // public RenderWindow window;

    public ArrayList<RenderTarget> renderTargets = new ArrayList<>();

    public RenderPipeline(String name) {
        this.name = name;
    }

    public void render(Client client, MatrixStack worldStack, MatrixStack screenStack, GraphicsContext graphicsContext) {
        client.rWindow.render(graphicsContext, client, worldStack, screenStack);
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
        ArrayList<RenderTask> tasks = new ArrayList<>();
        for(RenderTarget renderTarget : renderTargets) {
            for (RenderTaskSource renderTaskSource : owner.getGameInstance().RENDER_TASK.ELEMENTS) {
                if(renderTaskSource.renderTargetName.equals(renderTarget.getIdentifierName())) {
                    tasks.add(renderTaskSource.getTask(graphicsEngine.getIdentifierName()));
                }
            }
        }
        return new PipelineInstance(this, tasks);
    }

    @Override
    public void assignOwner(ModContainer owner) {
        this.owner = owner;
    }

    @Override
    public void load(GameInstance gameInstance) {
        for(RenderTarget renderTarget : gameInstance.RENDER_TARGETS.ELEMENTS) {
            if(getIdentifierName().equals(renderTarget.renderPipeline)) {
                addRenderTarget(renderTarget);
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
