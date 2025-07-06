package dev.hilligans.ourcraft.client.rendering.screens;

import dev.hilligans.ourcraft.client.rendering.ScreenBase;
import dev.hilligans.ourcraft.client.rendering.widgets.Button;

public class EscapeScreen extends ScreenBase {

    public EscapeScreen() {
        addWidget(new Button(50,50,200,40, "menu.disconnect", () -> {
            getClient().closeScreen();
            getClient().socket.closeSocket();
            //getClient().network.disconnect();
        }));
        addWidget(new Button(50, 100, 200, 40, "menu.settings", () -> {
            getClient().openScreen(new SettingsScreen());
        }));
    }
}
