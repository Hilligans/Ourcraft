package dev.Hilligans.Ourcraft.Client.Rendering.Screens;

import dev.Hilligans.Ourcraft.Client.Client;
import dev.Hilligans.Ourcraft.Client.MatrixStack;
import dev.Hilligans.Ourcraft.Client.Mouse.MouseHandler;
import dev.Hilligans.Ourcraft.Client.Rendering.MiniMap.MiniMap;
import dev.Hilligans.Ourcraft.Client.Rendering.ScreenBase;
import dev.Hilligans.Ourcraft.ClientMain;

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
    public void drawScreen(MatrixStack matrixStack) {
        super.drawScreen(matrixStack);
        if(MouseHandler.instance.mousePressed) {
            DoubleBuffer mousePos = ClientMain.getClient().getMousePos();
            int x = (int) (mousePos.get(0) - mouseLastX);
            int y = (int) (mousePos.get(1) - mouseLastY);
            miniMap.addX(Math.round(x / getRatio(miniMap.zoom)));
            miniMap.addY(Math.round(y / getRatio(miniMap.zoom)));

            mouseLastX = (int) mousePos.get(0);
            mouseLastY = (int) mousePos.get(1);
        }
        miniMap.draw(matrixStack,miniMap.lastX, miniMap.lastY,0,0,ClientMain.getWindowX(),ClientMain.getWindowY());
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
