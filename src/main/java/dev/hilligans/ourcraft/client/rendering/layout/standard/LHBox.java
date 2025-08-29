package dev.hilligans.ourcraft.client.rendering.layout.standard;

import dev.hilligans.ourcraft.client.rendering.layout.LList;
import dev.hilligans.ourcraft.client.rendering.layout.LWidget;

import java.util.Arrays;

public class LHBox extends LList {

    public LHBox() {}

    public LHBox(LWidget... widgets) {
        this.children.addAll(Arrays.asList(widgets));
    }

    @Override
    public void recalculate() {

    }
}
