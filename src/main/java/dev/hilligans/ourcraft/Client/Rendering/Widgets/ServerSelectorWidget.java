package dev.hilligans.ourcraft.Client.Rendering.Widgets;

import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Client.Rendering.Screens.JoinScreen;
import dev.hilligans.ourcraft.Client.Rendering.Screens.LoadingScreen;
import dev.hilligans.ourcraft.Client.Rendering.Textures;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Util.Settings;

public class ServerSelectorWidget extends Widget {

    long lastTime;
    String ip;
    String port;
    JoinScreen joinScreen;

    public ServerSelectorWidget(int x, int y, int width, int height, String ip, String port, JoinScreen joinScreen) {
        super(x, y, width, height);
        this.ip = ip;
        this.port = port;
        this.joinScreen = joinScreen;
    }

    @Override
    public void render(RenderWindow window, MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(window, matrixStack, xOffset, yOffset);
        Textures.BUTTON.drawTexture(window, matrixStack,x,y,width,height);
        float sizeX = width / (float)Textures.BUTTON.width;
        float sizeY = height / (float)Textures.BUTTON.height;
        window.getStringRenderer().drawStringInternal(window, matrixStack,ip + ":" + port,x + (int)(sizeX * Settings.guiSize),y + (int)(sizeY * Settings.guiSize),0.5f);
    }

    public void joinServer() {
        try {
            screenBase.client.network.joinServer(ip,port, ClientMain.getClient());
            screenBase.client.closeScreen();
            screenBase.client.serverIP = ip + ":" + port;
            screenBase.window.queueRenderPipeline("ourcraft:new_world_pipeline");
            screenBase.client.openScreen(new LoadingScreen(screenBase.client));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void activate(int x, int y) {
        long time = System.currentTimeMillis();
        joinScreen.setActive(this);
        if(time - lastTime < 250) {
            joinServer();
        }
        lastTime = time;
    }
}
