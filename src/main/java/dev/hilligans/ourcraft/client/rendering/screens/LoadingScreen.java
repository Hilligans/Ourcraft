package dev.hilligans.ourcraft.client.rendering.screens;

import dev.hilligans.engine.client.graphics.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.ScreenBase;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;

public class LoadingScreen extends ScreenBase {

    public LoadingScreen() {
    }

    @Override
    public void drawScreen(RenderWindow window, MatrixStack matrixStack, GraphicsContext graphicsContext) {
        super.drawScreen(window, matrixStack, graphicsContext);
        //if(ClientNetworkInit.networkInit != null && ClientNetworkInit.networkInit.packetDecoder != null) {
      //      StringRenderer.drawString(matrixStack, "Content downloading:" + ClientNetworkInit.networkInit.packetDecoder.getPercentage(), 100, 100, 0.5f);
     //   }
    }
}
