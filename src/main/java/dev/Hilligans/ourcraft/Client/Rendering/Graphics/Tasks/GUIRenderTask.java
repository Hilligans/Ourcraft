package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Tasks;

import dev.Hilligans.ourcraft.Client.Camera;
import dev.Hilligans.ourcraft.Client.ChatWindow;
import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Input.Key.KeyHandler;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IDefaultEngineImpl;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.OpenGL.OpenGLEngine;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.PipelineState;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderTask;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderTaskSource;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.GLRenderer;
import dev.Hilligans.ourcraft.Client.Rendering.Screens.ContainerScreens.InventoryScreen;
import dev.Hilligans.ourcraft.Client.Rendering.Textures;
import dev.Hilligans.ourcraft.Client.Rendering.World.StringRenderer;
import dev.Hilligans.ourcraft.Item.ItemStack;
import dev.Hilligans.ourcraft.Util.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;

public class GUIRenderTask extends RenderTaskSource {

    public GUIRenderTask() {
        super("gui_render_task", "ourcraft:gui_renderer");
    }

    @Override
    public RenderTask getDefaultTask() {
        return new RenderTask() {
            @Override
            public void draw(RenderWindow window, IGraphicsEngine<?, ?> engine, Client client, MatrixStack worldStack, MatrixStack screenStack) {
                IDefaultEngineImpl<?> impl = engine.getDefaultImpl();

                impl.setState(window, new PipelineState());

                StringRenderer stringRenderer = engine.getStringRenderer();

                if (client.playerData.f3) {
                    stringRenderer.drawStringInternal(window,screenStack,"1",0,0,0.5f);
                    stringRenderer.drawStringInternal(window, screenStack, "FPS:" + window.frameTracker.getFPS(), client.windowX / 2, 29, 0.5f);
                    stringRenderer.drawStringInternal(window, screenStack, "Biome:" + client.clientWorld.biomeMap.getBiome((int) Camera.pos.x, (int) Camera.pos.z).name, client.windowX / 2, 58, 0.5f);
                    stringRenderer.drawStringInternal(window, screenStack, "VelY:" + Camera.velY, client.windowX / 2, 87, 0.5f);
                    Runtime runtime = Runtime.getRuntime();
                    long usedMB = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
                    stringRenderer.drawStringInternal(window, screenStack, "Memory:" + usedMB + "MB", client.windowX / 2, 126, 0.5f);
                    stringRenderer.drawStringInternal(window, screenStack, "Pitch:" + Math.toDegrees(Camera.pitch), client.windowX / 2, 155, 0.5f);
                    stringRenderer.drawStringInternal(window, screenStack, "Yaw:" + Math.toDegrees(Camera.yaw), client.windowX / 2, 184, 0.5f);
                    stringRenderer.drawStringInternal(window, screenStack, "Sounds:" + client.soundEngine.sounds.size(), client.windowX / 2, 213, 0.5f);
                    stringRenderer.drawStringInternal(window, screenStack, "Render Calls:" + GLRenderer.drawCalls, client.windowX / 2, 242, 0.5f);
                    stringRenderer.drawStringInternal(window, screenStack, "Vertices:" + GLRenderer.count, client.windowX / 2, 271, 0.5f);
                //    stringRenderer.drawStringInternal(window, screenStack, "Block:" + (client.blockState == null ? "null" : blockState.getBlock().getName() + ":" + blockState.readData()), windowX / 2, 300, 0.5f);
                    if (client.renderTime % 100 == 0) {
                        client.chunks = client.clientWorld.chunkContainer.getSize();
                    }
                    stringRenderer.drawStringInternal(window, screenStack, "Chunks:" + client.chunks, client.windowX / 2, 329, 0.5f);
                }
                ItemStack stack = client.playerData.inventory.getItem(client.playerData.handSlot);
                if (stack != null && stack.item != null) {
                    int width = (int) (32 * Settings.guiSize);
                  //  stack.item.renderHolding(window, screenStack, width, stack);
                }

              //  InventoryScreen.drawHotbar(window, screenStack);
              //  ChatWindow.render1(window, screenStack);
              //  Camera.renderPlayer(worldStack);


                if (client.screen != null) {
                    client.screen.render(window, screenStack);
                  //  client.playerData.heldStack.renderStack(screenStack, (int) (Camera.newX - Settings.guiSize * 8), (int) (Camera.newY - Settings.guiSize * 8));
                } else {
                   // Textures.CURSOR.drawCenteredTexture(window, screenStack,1.0f);
                    if (KeyHandler.keyPressed[GLFW_KEY_TAB]) {
                        if (client.playerList != null) {
                          //  client.playerList.render(screenStack);
                        }
                    }
                }
            }
        };
    }
}
