package Hilligans.Client.Rendering.Screens;

import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.Network.ClientNetworkInit;

public class LoadingScreen extends ScreenBase {

    @Override
    public void drawScreen(MatrixStack matrixStack) {
        super.drawScreen(matrixStack);
        if(ClientNetworkInit.networkInit != null && ClientNetworkInit.networkInit.packetDecoder != null) {
            StringRenderer.drawString(matrixStack, "Content downloading:" + ClientNetworkInit.networkInit.packetDecoder.getPercentage(), 100, 100, 0.5f);
        }
    }
}
