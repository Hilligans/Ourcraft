package dev.Hilligans.ourcraft.Client.Rendering.Graphics;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.GraphicsContext;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;

import java.util.ArrayList;

public class RenderPipeline implements IRegistryElement {

    public String name;
    public ModContent modContent;
    public RenderWindow window;

    public ArrayList<RenderTarget> renderTargets = new ArrayList<>();
    public ArrayList<RenderTask> renderTasks = new ArrayList<>();

    public RenderPipeline(String name) {
        this.name = name;
    }

    public void render(Client client, MatrixStack worldStack, MatrixStack screenStack, GraphicsContext graphicsContext) {
        for(RenderTask renderTask : renderTasks) {
            renderTask.draw(window,graphicsContext,window.getGraphicsEngine(),client,worldStack,screenStack);
        }
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
        this.window = window;
        for(RenderTarget renderTarget : renderTargets) {
            for (RenderTaskSource renderTaskSource : modContent.getGameInstance().RENDER_TASK.ELEMENTS) {
                if(renderTaskSource.renderTargetName.equals(renderTarget.getIdentifierName())) {
                    renderTasks.add(renderTaskSource.getTask(window.getGraphicsEngine().getIdentifierName()));
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
    public String getIdentifierName() {
        return modContent.getModID() + ":" + name;
    }

    @Override
    public String getUniqueName() {
        return "block." + modContent.getModID() + "." + name;
    }
}
