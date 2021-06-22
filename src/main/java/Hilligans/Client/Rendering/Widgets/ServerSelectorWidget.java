package Hilligans.Client.Rendering.Widgets;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Client.Rendering.Screens.JoinScreen;
import Hilligans.Client.Rendering.Screens.LoadingScreen;
import Hilligans.Client.Rendering.Texture;
import Hilligans.Client.Rendering.Textures;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.ClientMain;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.ClientNetworkInit;
import Hilligans.Util.Settings;

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
    public void render(MatrixStack matrixStack, int xOffset, int yOffset) {
        super.render(matrixStack, xOffset, yOffset);
        Textures.BUTTON.drawTexture(matrixStack,x,y,width,height);
        float sizeX = width / (float)Textures.BUTTON.width;
        float sizeY = height / (float)Textures.BUTTON.height;
        StringRenderer.drawString(matrixStack,ip + ":" + port,x + (int)(sizeX * Settings.guiSize),y + (int)(sizeY * Settings.guiSize),0.5f);
    }

    public void joinServer() {
        try {
            ClientNetworkHandler.clientNetworkHandler = new ClientNetworkHandler();
            ClientNetworkInit.joinServer(ip, port, ClientNetworkHandler.clientNetworkHandler);
            ClientMain.getClient().closeScreen();
         //   ClientMain.getClient().openScreen(new LoadingScreen());
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
