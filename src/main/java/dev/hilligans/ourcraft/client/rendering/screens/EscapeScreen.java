package dev.hilligans.ourcraft.client.rendering.screens;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.rendering.ScreenBase;
import dev.hilligans.ourcraft.client.rendering.widgets.Button;

public class EscapeScreen extends ScreenBase {

    public EscapeScreen(Client client) {
        super(client);
        addWidget(new Button(50,50,200,40, "menu.disconnect", () -> {
            client.closeScreen();
            client.network.disconnect();
        }));
        addWidget(new Button(50, 100, 200, 40, "menu.settings", () -> {
            client.openScreen(new SettingsScreen(client));
        }));
    }
}
