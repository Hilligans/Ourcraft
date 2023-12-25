package dev.hilligans.ourcraft.client.rendering.graphics.api;

import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.GraphicsData;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.world.StringRenderer;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.util.Logger;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import dev.hilligans.ourcraft.util.sections.ISection;

import java.util.ArrayList;

public interface IGraphicsEngine<Q extends RenderWindow, V extends IDefaultEngineImpl<Q,X>, X extends GraphicsContext> extends IRegistryElement {

    Q createWindow();

    void render(RenderWindow window, GraphicsContext graphicsContext);

    void renderScreen(MatrixStack screenStack);

    /**
     * @return Returns the default window for the graphics engine, this will always be a valid window.
     * Most of the time these windows were required to create the engine in the first place.
     */
    Q setup();

    void close();

    X getContext();

    ArrayList<Q> getWindows();

    GameInstance getGameInstance();

    Logger getLogger();

    GraphicsContext getGraphicsContext();

    default Runnable createRenderLoop(GameInstance gameInstance, RenderWindow window) {
        return () -> {
            try {
                GraphicsContext graphicsContext = getGraphicsContext();
                ISection section = graphicsContext.getSection();
                try(var $0 = section.startSection("base")) {
                    while (!window.shouldClose()) {
                        try (var $1 = section.startSection("loop")) {
                            try (var $2 = section.startSection("render")) {
                                render(window, graphicsContext);
                            }
                            try (var $2 = section.startSection("swapBuffers")) {
                                window.swapBuffers(graphicsContext);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
            System.out.println("Closing");
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

    int getProgram(String name);

    GraphicsData getGraphicsData();

    default RenderWindow startEngine() {
        RenderWindow window = setup();
        //getGameInstance().build(this);
        return window;
    }

    @Override
    default void cleanup() {
    }

    @Override
    default String getResourceType() {
        return "graphics_engine";
    }
}


