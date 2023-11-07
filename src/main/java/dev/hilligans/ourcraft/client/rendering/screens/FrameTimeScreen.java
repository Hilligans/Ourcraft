package dev.hilligans.ourcraft.client.rendering.screens;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.rendering.ScreenBase;
import dev.hilligans.ourcraft.client.rendering.widgets.FrameTimeWidget;

public class FrameTimeScreen extends ScreenBase {

    public FrameTimeScreen(Client client) {
        super(client);
        addWidget(new FrameTimeWidget(100,100,400,400,4));
    }
}
