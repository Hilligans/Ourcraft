package dev.hilligans.ourcraft.Client.Rendering.Screens;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Client.Rendering.ScreenBase;

public class LoadingScreen extends ScreenBase {

    public LoadingScreen(Client client) {
        super(client);
    }

    @Override
    public void drawScreen(RenderWindow window, MatrixStack matrixStack) {
        super.drawScreen(window, matrixStack);
        //if(ClientNetworkInit.networkInit != null && ClientNetworkInit.networkInit.packetDecoder != null) {
      //      StringRenderer.drawString(matrixStack, "Content downloading:" + ClientNetworkInit.networkInit.packetDecoder.getPercentage(), 100, 100, 0.5f);
     //   }
    }
}
