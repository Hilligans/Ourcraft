package dev.hilligans.ourcraft.client.rendering.screens;

import dev.hilligans.ourcraft.client.rendering.ScreenBase;
import dev.hilligans.ourcraft.client.rendering.widgets.FrameTimeWidget;

public class FrameTimeScreen extends ScreenBase {

    public FrameTimeScreen() {
        addWidget(new FrameTimeWidget(100,100,400,400,4));
    }
}
