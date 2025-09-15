package dev.hilligans.engine.client.graphics.api;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.StringRenderer;
import dev.hilligans.engine.util.Logger;
import dev.hilligans.engine.util.registry.IRegistryElement;
import dev.hilligans.engine.util.sections.ISection;

import java.util.ArrayList;

public interface IGraphicsEngine<Q extends RenderWindow, V extends IDefaultEngineImpl<Q,X,?>, X extends GraphicsContext> extends IRegistryElement {

    Q createWindow();

    void render(RenderWindow window, GraphicsContext graphicsContext);

    void renderScreen(MatrixStack screenStack);

    /**
     * @return Returns the default window for the graphics engine, this will always be a valid window.
     * Most of the time these windows were required to create the engine in the first place.
     */
    Q setup();

    void close();

    X createContext(Q window);

    default X createContext(Object window) {
        return createContext((Q)window);
    }

    ArrayList<Q> getWindows();

    GameInstance getGameInstance();

    Logger getLogger();

    boolean isRunning();

    default Runnable createRenderLoop(GameInstance gameInstance, RenderWindow w) {
        return () -> {
            try {
                GraphicsContext graphicsContext = createContext(w);
                System.out.println(w);
                ISection section = graphicsContext.getSection();
                try(var $0 = section.startSection("base")) {
                    out: {
                        while (true) {
                            try (var $1 = section.startSection("loop")) {
                                for (RenderWindow window : getWindows()) {
                                    if (window.shouldClose()) {
                                        break out;
                                    }
                                    try (var $9 = section.startSection(window.getWindowName())) {
                                        try (var $2 = section.startSection("tick")) {
                                            window.getClient().tick(graphicsContext);
                                        }
                                        try (var $2 = section.startSection("render")) {
                                            render(window, graphicsContext);
                                        }
                                        try (var $2 = section.startSection("swapBuffers")) {
                                            window.swapBuffers(graphicsContext);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            } finally {
                w.cleanup();
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

    default RenderWindow startEngine() {
        if(!isRunning()) {
            return setup();
        } else {
            return createWindow();
        }
    }

    @Override
    default void cleanup() {
    }

    @Override
    default String getResourceType() {
        return "graphics_engine";
    }
}


