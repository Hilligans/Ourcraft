package dev.Hilligans.ourcraft.Client.Rendering.Screens;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.ScreenBase;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.NewWidgets.FrameTimeWidget;

public class FrameTimeScreen extends ScreenBase {

    public FrameTimeScreen(Client client) {
        super(client);
        addWidget(new FrameTimeWidget(100,100,400,400,4));
    }
}
