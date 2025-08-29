package dev.hilligans.ourcraft.client.rendering.layout;

public non-sealed class LBox extends LWidget {

    public LWidget widget;

    public LBox() {}

    public LBox(LWidget widget) {
        this.widget = widget;
    }

    @Override
    public void recalculate() {

    }
}
