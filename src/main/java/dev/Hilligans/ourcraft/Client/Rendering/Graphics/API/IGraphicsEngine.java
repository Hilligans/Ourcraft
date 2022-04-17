package dev.Hilligans.ourcraft.Client.Rendering.Graphics.API;

import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.World.StringRenderer;
import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Events.Client.RenderPostEvent;
import dev.Hilligans.ourcraft.ModHandler.Events.Client.RenderPreEvent;
import dev.Hilligans.ourcraft.Util.Logger;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.ClientWorld;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public interface IGraphicsEngine<T, Q extends RenderWindow, V extends IDefaultEngineImpl<Q>> extends IRegistryElement {

    Q createWindow();

    T getChunkGraphicsContainer(Chunk chunk);

    T createChunkGraphicsContainer();

    void putChunkGraphicsContainer(Chunk chunk, T container);

    void render(Q window);

    void renderWorld(MatrixStack matrixStack, ClientWorld world);

    void renderScreen(MatrixStack screenStack);

    /**
     * @return Returns the default window for the graphics engine, this will always be a valid window.
     * Most of the time these windows were required to create the engine in the first place.
     */
    Q setup();

    void close();

    ArrayList<Q> getWindows();

    GameInstance getGameInstance();

    Logger getLogger();

    default Runnable createRenderLoop(GameInstance gameInstance, RenderWindow window) {
        return new Runnable() {
            @Override
            public void run() {
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

    V getDefaultImpl();

    StringRenderer getStringRenderer();

    void setupStringRenderer(String defaultLanguage);
}
