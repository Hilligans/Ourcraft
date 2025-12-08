package dev.hilligans.engine.client.graphics.layout;

public non-sealed class LBox extends LWidget {

    public LWidget widget;

    public LBox() {}

    public LBox(int width, int height) {
        this.widgetSize = new WidgetSize(width, height);
    }

    public LBox(LWidget widget) {
        this.widget = widget;
    }

    @Override
    public void recalculate() {

    }
}
