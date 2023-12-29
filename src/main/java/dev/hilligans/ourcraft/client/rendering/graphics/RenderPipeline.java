package dev.hilligans.ourcraft.client.rendering.graphics;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsElement;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

import java.util.ArrayList;

public class RenderPipeline implements IRegistryElement, IGraphicsElement {

    public String name;
    public ModContent modContent;
   // public RenderWindow window;

    public ArrayList<RenderTarget> renderTargets = new ArrayList<>();
    public ArrayList<RenderTask> renderTasks = new ArrayList<>();

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
                if(target.name.equals(renderTarget.after) && target.modContent.getModID().equals(renderTarget.targetedMod)) {
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

    public void build(RenderWindow window) {
        //this.window = window;
    }

    public void buildTargets(IGraphicsEngine<?, ?, ?> graphicsEngine) {
        for(RenderTarget renderTarget : renderTargets) {
            for (RenderTaskSource renderTaskSource : modContent.getGameInstance().RENDER_TASK.ELEMENTS) {
                if(renderTaskSource.renderTargetName.equals(renderTarget.getIdentifierName())) {
                    renderTasks.add(renderTaskSource.getTask(graphicsEngine.getIdentifierName()));
                }
            }
        }
    }

    @Override
    public void assignModContent(ModContent modContent) {
        this.modContent = modContent;
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
        return modContent.getModID();
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
                ", renderTasks=" + renderTasks +
                '}';
    }

    @Override
    public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        for(RenderTask renderTask : renderTasks) {
            renderTask.load(gameInstance, graphicsEngine, graphicsContext);
        }
    }

    @Override
    public void cleanup(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        for(RenderTask renderTask : renderTasks) {
            renderTask.cleanup(gameInstance, graphicsEngine, graphicsContext);
        }
    }
}
