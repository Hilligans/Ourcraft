package dev.hilligans.engine.client.graphics.layout.standard;

import dev.hilligans.engine.client.graphics.layout.LList;
import dev.hilligans.engine.client.graphics.layout.LWidget;

import java.util.Arrays;

public class LVBox extends LList {

    public LVBox() {}

    public LVBox(LWidget... widgets) {
        this.children.addAll(Arrays.asList(widgets));
    }

    public int getChildrenCount() {
        return this.children.size();
    }

    @Override
    public void recalculate() {

    }
}
