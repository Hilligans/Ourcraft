package dev.hilligans.engine.client.graphics.multigl;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.DefaultMeshBuilder;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.GraphicsEngineBase;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.engine.util.Logger;

public class MultiGLEngine extends GraphicsEngineBase<MultiGLWindow, MultiGLDefaultImpl, MultiGLContext> {

    public GameInstance gameInstance;
    public MultiGLDefaultImpl multiGLDefault;

    @Override
    public MultiGLWindow createWindow() {
        return null;
    }

    @Override
    public void render(RenderWindow window, GraphicsContext graphicsContext) {

    }

    @Override
    public void renderScreen(MatrixStack screenStack) {

    }

    @Override
    public MultiGLWindow setup() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public MultiGLContext createContext(MultiGLWindow window) {
        return null;
    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public boolean isCompatible() {
        return true;
    }

    @Override
    public MultiGLDefaultImpl getDefaultImpl() {
        return multiGLDefault;
    }

    @Override
    public int getProgram(String name) {
        return 0;
    }

    @Override
    public String getResourceName() {
        return "";
    }

    @Override
    public String getResourceOwner() {
        return "";
    }
}
