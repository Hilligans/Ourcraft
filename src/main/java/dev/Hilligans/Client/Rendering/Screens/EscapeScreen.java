package dev.Hilligans.Client.Rendering.Screens;

import dev.Hilligans.Client.Client;
import dev.Hilligans.Client.Rendering.ScreenBase;
import dev.Hilligans.Client.Rendering.Widgets.Button;

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
