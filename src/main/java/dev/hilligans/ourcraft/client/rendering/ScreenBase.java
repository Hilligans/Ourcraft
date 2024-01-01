package dev.hilligans.ourcraft.client.rendering;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.input.key.CharPress;
import dev.hilligans.ourcraft.client.input.key.KeyHandler;
import dev.hilligans.ourcraft.client.input.key.KeyPress;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.graphics.ShaderSource;
import dev.hilligans.ourcraft.client.rendering.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.widgets.Widget;
import dev.hilligans.ourcraft.network.packet.client.CCloseScreen;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

public abstract class ScreenBase implements Screen {

    public ArrayList<Widget> widgets = new ArrayList<>();
    public ArrayList<CharPress> charPresses = new ArrayList<>();
    public ArrayList<KeyPress> keyPresses = new ArrayList<>();

    //public Client client;
    public ShaderSource defaultShader;

    public ScreenBase() {}

    public ScreenBase(Client client) {
        //this.client = client;
    }

    public RenderWindow window;

    public void drawScreen(RenderWindow window, MatrixStack matrixStack, GraphicsContext graphicsContext) {}

    public void render(RenderWindow window, MatrixStack matrixStack, GraphicsContext graphicsContext) {
        drawScreen(window, matrixStack, graphicsContext);
        for(Widget widget : widgets) {
            widget.render(window, graphicsContext, matrixStack, 0, 0);
        }
    }

    public void registerCharPress(CharPress charPress) {
        charPresses.add(charPress);
        KeyHandler.register(charPress);
    }

    public void registerKeyPress(KeyPress keyPress, int id) {
        keyPresses.add(keyPress);
        KeyHandler.register(keyPress,id);

    }

    @Override
    public void close(boolean replaced) {
        for(Widget widget : widgets) {
            widget.screenClose();
        }

        for(CharPress charPress : charPresses) {
            KeyHandler.remove(charPress);
        }

        for(KeyPress keyPress : keyPresses) {
            KeyHandler.remove(keyPress);
        }
        getClient().sendPacket(new CCloseScreen(replaced));
    }

    @Override
    public void mouseClick(int x, int y, int mouseButton) {
        if(mouseButton == GLFW_MOUSE_BUTTON_1) {
            for (Widget widget : widgets) {
                widget.isFocused = false;
                if (widget.isInBounds(x, y)) {
                    widget.activate(x - widget.x, y - widget.y);
                }
            }
        }
    }

    public void mouseScroll(int x, int y, float amount) {
        for(Widget widget : widgets) {
            widget.mouseScroll(x,y,amount);
        }
    }

    public void resize(int x, int y) {
        for(Widget widget : widgets) {
            widget.onScreenResize(x,y);
        }
    }

    public void addWidget(Widget widget) {
        widgets.add(widget);
        widget.screenBase = this;
        widget.onScreenResize(window.getWindowWidth(),window.getWindowHeight());
    }

    @Override
    public void setWindow(RenderWindow renderWindow) {
        this.window = renderWindow;
        this.defaultShader = renderWindow.getGraphicsEngine().getGameInstance().SHADERS.get("ourcraft:position_texture_color");
        for(Widget widget : widgets) {
            widget.addSource(renderWindow);
        }
        buildContentForWindow(renderWindow);
    }

    public RenderWindow getWindow() {
        return window;
    }

    public void buildContentForWindow(RenderWindow window) {}
}
