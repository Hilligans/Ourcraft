package Hilligans.Client.Rendering.Screens;

import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.Widgets.Button;
import Hilligans.Client.Rendering.Widgets.ButtonAction;
import Hilligans.ClientMain;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.ClientNetworkInit;

public class EscapeScreen extends ScreenBase {

    public EscapeScreen() {
        widgets.add(new Button(50,50,200,40, "Disconnect", () -> {
            if(ClientNetworkInit.channel != null) {
                ClientNetworkInit.channel.close();
            }
            ClientMain.closeScreen();

        }));
    }

    @Override
    public void close() {
        super.close();
    }
}
