package Hilligans.Client.Rendering.Screens;

import Hilligans.Client.Client;
import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.Widgets.Button;
import Hilligans.Client.Rendering.Widgets.ButtonAction;
import Hilligans.Client.Rendering.Widgets.SliderWidget;
import Hilligans.ClientMain;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.ClientNetworkInit;

public class EscapeScreen extends ScreenBase {

    public EscapeScreen(Client client) {
        super(client);
        widgets.add(new Button(50,50,200,40, "menu.disconnect", () -> {
            client.closeScreen();
            client.network.disconnect();
        }));
        widgets.add(new Button(50, 100, 200, 40, "menu.settings", () -> {
            client.openScreen(new SettingsScreen(client));
        }));
    }
}
