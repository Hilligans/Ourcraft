package dev.Hilligans.ourcraft.Client.Rendering.Graphics;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Events.Client.RenderPostEvent;
import dev.Hilligans.ourcraft.ModHandler.Events.Client.RenderPreEvent;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.ClientWorld;

import java.util.ArrayList;

public interface IGraphicsEngine<T, Q extends RenderWindow> extends IRegistryElement {

    Q createWindow();

    T getChunkGraphicsContainer(Chunk chunk);

    T createChunkGraphicsContainer();

    void putChunkGraphicsContainer(Chunk chunk, T container);

    void render(Q window);

    void renderWorld(MatrixStack matrixStack, ClientWorld world);

    void renderScreen(MatrixStack screenStack);

    void setup();

    void close();

    ArrayList<Q> getWindows();

    GameInstance getGameInstance();

    default Runnable createRenderLoop(GameInstance gameInstance, RenderWindow window) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    //Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //System.exit(0);
                while (!window.shouldClose()) {
                    gameInstance.EVENT_BUS.postEvent(new RenderPreEvent());
                    render((Q) window);
                    gameInstance.EVENT_BUS.postEvent(new RenderPostEvent(window.getClient()));
                    window.swapBuffers();
                }
            }
        };
    }

    boolean isCompatible();

    default void setWindowsFrameRate(int frameRate) {
        for(Q window : getWindows()) {
            window.frameTracker.setMaxFrameRate(frameRate);
        }
    }
}
