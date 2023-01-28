package dev.hilligans.ourcraft.Client.Rendering.Screens;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.Rendering.ScreenBase;
import dev.hilligans.ourcraft.Client.Rendering.Widgets.NewWidgets.FrameTimeWidget;

public class FrameTimeScreen extends ScreenBase {

    public FrameTimeScreen(Client client) {
        super(client);
        addWidget(new FrameTimeWidget(100,100,400,400,4));
    }
}
