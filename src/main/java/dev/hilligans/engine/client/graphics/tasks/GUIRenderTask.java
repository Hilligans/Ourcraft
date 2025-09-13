package dev.hilligans.engine.client.graphics.tasks;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.engine.client.graphics.MatrixStack;
import dev.hilligans.engine.client.graphics.PipelineState;
import dev.hilligans.engine.client.graphics.RenderTask;
import dev.hilligans.engine.client.graphics.RenderTaskSource;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IDefaultEngineImpl;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.world.StringRenderer;
import dev.hilligans.ourcraft.util.sections.ProfiledSection;

//import static dev.hilligans.ourcraft.util.FormattedString.FSTR;

public class GUIRenderTask extends RenderTaskSource {

    public GUIRenderTask() {
        super("gui_render_task", "ourcraft:gui_renderer");
    }

    @Override
    public RenderTask getDefaultTask() {
        return new RenderTask() {

            public int counter = 0;
            public ProfiledSection.StackFrame monitoredFrame;

            @Override
            public void draw(RenderWindow window, GraphicsContext graphicsContext, IGraphicsEngine<?, ?,?> engine, Client client, MatrixStack worldStack, MatrixStack screenStack, float delta) {
                IDefaultEngineImpl<?,?,?> impl = engine.getDefaultImpl();

                StringRenderer stringRenderer = engine.getStringRenderer();
                counter++;
                if (client.playerData.f3) {
                  //  stringRenderer.drawStringInternal(window,screenStack,"1",0,0,0.5f);
                    stringRenderer.drawStringInternal(window, graphicsContext, screenStack, "FPS:" + window.frameTracker.getFPS(), window.getWindowWidth() / 2, 29, 0.5f);
                  /*  stringRenderer.drawStringInternal(window, screenStack, "Biome:" + client.clientWorld.biomeMap.getBiome((int) Camera.pos.x, (int) Camera.pos.z).name, client.windowX / 2, 58, 0.5f);
                    stringRenderer.drawStringInternal(window, screenStack, "VelY:" + Camera.velY, client.windowX / 2, 87, 0.5f);
                    Runtime runtime = Runtime.getRuntime();
                    long usedMB = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
                    stringRenderer.drawStringInternal(window, screenStack, "Memory:" + usedMB + "MB", client.windowX / 2, 126, 0.5f);
                    */
                    stringRenderer.drawStringInternal(window, graphicsContext, screenStack, "Pitch:" + Math.toDegrees(window.camera.getPitch()), window.getWindowWidth() / 2, 155, 0.5f);
                    stringRenderer.drawStringInternal(window, graphicsContext, screenStack, "Yaw:" + Math.toDegrees(window.camera.getYaw()), window.getWindowWidth() / 2, 184, 0.5f);
                    /*
                    stringRenderer.drawStringInternal(window, screenStack, "Sounds:" + client.soundEngine.sounds.size(), client.windowX / 2, 213, 0.5f);
                    stringRenderer.drawStringInternal(window, screenStack, "Render Calls:" + GLRenderer.drawCalls, client.windowX / 2, 242, 0.5f);
                    stringRenderer.drawStringInternal(window, screenStack, "Vertices:" + GLRenderer.count, client.windowX / 2, 271, 0.5f);
                //    stringRenderer.drawStringInternal(window, screenStack, "Block:" + (client.blockState == null ? "null" : blockState.getBlock().getName() + ":" + blockState.readData()), windowX / 2, 300, 0.5f);
                    if (client.renderTime % 100 == 0) {
                        client.chunks = client.clientWorld.chunkContainer.getSize();
                    }
                    stringRenderer.drawStringInternal(window, screenStack, "Chunks:" + client.chunks, client.windowX / 2, 329, 0.5f);

                   */
                    stringRenderer.drawStringInternal(window, graphicsContext, screenStack, "X:" + client.rWindow.camera.getCameraPos().x, window.getWindowWidth() / 2, 358,0.5f);
                    stringRenderer.drawStringInternal(window, graphicsContext, screenStack, "Y:" + client.rWindow.camera.getCameraPos().y, window.getWindowWidth() / 2, 387,0.5f);
                    stringRenderer.drawStringInternal(window, graphicsContext, screenStack, "Z:" + client.rWindow.camera.getCameraPos().z, window.getWindowWidth() / 2, 416,0.5f);

//                    VertexMesh vertexMesh = new VertexMesh(Ourcraft.position_color_texture);


                    if(counter % 100 == 0) {
                        counter = 0;
                        if(graphicsContext.getSection() instanceof ProfiledSection profiledSection) {
                            monitoredFrame = profiledSection.getMonitor();
                        }
                    }
                    if(monitoredFrame != null) {
                        //recursiveDraw(stringRenderer, window, screenStack, monitoredFrame, new int[]{0}, monitoredFrame.totalTime);
                        recursiveDrawTimes(stringRenderer, window, graphicsContext, screenStack, monitoredFrame, new int[]{0});
                    }
                }
              //  ItemStack stack = client.playerData.inventory.getItem(client.playerData.handSlot);
             //   if (stack != null && stack.item != null) {
             //       int width = (int) (32 * Settings.guiSize);Topics
                  //  stack.item.renderHolding(window, screenStack, width, stack);
             //   }

              //  InventoryScreen.drawHotbar(window, screenStack);
              //  ChatWindow.render1(window, screenStack);
              //  Camera.renderPlayer(worldStack);


                if (client.screen != null) {
                    client.screen.render(window, screenStack, graphicsContext);
                  //  client.playerData.heldStack.renderStack(screenStack, (int) (Camera.newX - Settings.guiSize * 8), (int) (Camera.newY - Settings.guiSize * 8));
                }
            }

            public void recursiveDraw(StringRenderer stringRenderer, RenderWindow renderWindow, GraphicsContext graphicsContext, MatrixStack matrixStack, ProfiledSection.StackFrame stackFrame, int[] y, long time) {
                //stringRenderer.drawStringInternal(renderWindow, graphicsContext, matrixStack, FSTR."\{stackFrame.getIndentLevel()}\{stackFrame.sectionName}: %2.2f%%\{(double)stackFrame.totalTime/time*100}\n", 0, y[0], 0.5f);
                y[0] += 29;
                for(ProfiledSection.StackFrame child : stackFrame.frames) {
                    recursiveDraw(stringRenderer, renderWindow, graphicsContext, matrixStack, child, y, time);
                }
            }

            public void recursiveDrawTimes(StringRenderer stringRenderer, RenderWindow renderWindow, GraphicsContext graphicsContext, MatrixStack matrixStack, ProfiledSection.StackFrame stackFrame, int[] y) {
                //stringRenderer.drawStringInternal(renderWindow, graphicsContext, matrixStack, STR."\{stackFrame.getIndentLevel()}\{stackFrame.sectionName}: \{Ourcraft.getConvertedTime(stackFrame.totalTime)}\n", 0, y[0], 0.5f);
                y[0] += 29;
                for(ProfiledSection.StackFrame child : stackFrame.frames) {
                    recursiveDrawTimes(stringRenderer, renderWindow, graphicsContext, matrixStack, child, y);
                }
            }

            @Override
            public PipelineState getPipelineState() {
                return new PipelineState();
            }
        };
    }
}
