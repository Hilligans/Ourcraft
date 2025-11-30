package dev.hilligans.ourcraft.client.rendering.widgets;

import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.rendering.Textures;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.screens.JoinScreen;
import dev.hilligans.ourcraft.client.rendering.screens.LoadingScreen;
import dev.hilligans.engine.network.Protocol;
import dev.hilligans.engine.network.engine.INetworkEngine;
import dev.hilligans.engine.network.engine.NetworkSocket;
import dev.hilligans.ourcraft.network.packet.CGetServerInfo;
import dev.hilligans.ourcraft.util.Settings;

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
    public void render(RenderWindow window, GraphicsContext graphicsContext, MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(window, graphicsContext, matrixStack, xOffset, yOffset);
        Textures.BUTTON.drawTexture(window, graphicsContext, matrixStack,x,y,width,height);
        float sizeX = width / (float)Textures.BUTTON.getWidth(window.getGameInstance());
        float sizeY = height / (float)Textures.BUTTON.getHeight(window.getGameInstance());
        window.getStringRenderer().drawStringInternal(window, graphicsContext, matrixStack,ip + ":" + port,x + (int)(sizeX * Settings.guiSize),y + (int)(sizeY * Settings.guiSize),0.5f);
    }

    public void joinServer() {
        try {
            Client client = (Client) screenBase.getClient();
            NetworkSocket<?> socket = screenBase.getClient().getGameInstance().getExcept("engine:nettyEngine", INetworkEngine.class)
                    .openClient(screenBase.getClient().getGameInstance().getExcept("ourcraft:login", Protocol.class), client, ip, port);
            client.socket = socket;
            socket.onConnected(CGetServerInfo::send);
            socket.connectSocket();
            //screenBase.getClient().network.joinServer(ip, port, screenBase.getClient());
            Thread.sleep(1000);
            client.closeScreen();
            client.serverIP = ip + ":" + port;
            screenBase.window.queueRenderPipeline("ourcraft:new_world_pipeline");
            screenBase.getClient().openScreen(new LoadingScreen());
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
