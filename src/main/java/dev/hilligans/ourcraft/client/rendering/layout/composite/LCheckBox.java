package dev.hilligans.ourcraft.client.rendering.layout.composite;

import dev.hilligans.ourcraft.client.rendering.layout.LBox;
import dev.hilligans.ourcraft.client.rendering.layout.LCustomWidget;
import dev.hilligans.ourcraft.client.rendering.layout.LText;
import dev.hilligans.ourcraft.client.rendering.layout.LWidget;
import dev.hilligans.ourcraft.client.rendering.layout.standard.LHBox;

public class LCheckBox extends LCustomWidget {

    public boolean checked;
    public String text;

    @Override
    public LWidget decompose() {
        return new LBox(
                new LHBox(
                        new LText(), // check box
                        new LText()
                ));
    }

    @Override
    public void recalculate() {

    }
}
