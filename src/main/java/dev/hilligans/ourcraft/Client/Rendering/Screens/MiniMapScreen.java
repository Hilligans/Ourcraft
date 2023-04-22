package dev.hilligans.ourcraft.Client.Rendering.Screens;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Input.Key.MouseHandler;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Client.Rendering.MiniMap.MiniMap;
import dev.hilligans.ourcraft.Client.Rendering.ScreenBase;
import dev.hilligans.ourcraft.ClientMain;

import java.nio.DoubleBuffer;

public class MiniMapScreen extends ScreenBase {

    MiniMap miniMap;

    public MiniMapScreen(Client client, MiniMap miniMap) {
        super(client);
        this.miniMap = miniMap;
    }

    int mouseLastX = 0;
    int mouseLastY = 0;

    @Override
    public void drawScreen(RenderWindow window, MatrixStack matrixStack) {
        super.drawScreen(window, matrixStack);
        if(MouseHandler.instance.mousePressed) {
            DoubleBuffer mousePos = ClientMain.getClient().getMousePos();
            int x = (int) (mousePos.get(0) - mouseLastX);
            int y = (int) (mousePos.get(1) - mouseLastY);
            miniMap.addX(Math.round(x / getRatio(miniMap.zoom)));
            miniMap.addY(Math.round(y / getRatio(miniMap.zoom)));

            mouseLastX = (int) mousePos.get(0);
            mouseLastY = (int) mousePos.get(1);
        }
        miniMap.draw(matrixStack,miniMap.lastX, miniMap.lastY,0,0, (int) window.getWindowWidth(), (int) window.getWindowHeight());
    }

    @Override
    public void mouseClick(int x, int y, int mouseButton) {
        super.mouseClick(x, y, mouseButton);
        mouseLastX = x;
        mouseLastY = y;
    }

    @Override
    public void mouseScroll(int x, int y, float amount) {
        super.mouseScroll(x, y, amount);
        miniMap.zoom = (int) Math.max(Math.min(miniMap.zoom + amount * 10, 1000),10);
    }

    public float getRatio(int size) {
        return size / 256f;
    }

    @Override
    public boolean renderWorld() {
        return false;
    }
}