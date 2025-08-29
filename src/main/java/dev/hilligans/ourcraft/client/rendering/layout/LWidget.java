package dev.hilligans.ourcraft.client.rendering.layout;

import dev.hilligans.ourcraft.client.rendering.layout.events.LClickEvent;
import dev.hilligans.ourcraft.client.rendering.layout.events.LKeyEvent;
import dev.hilligans.ourcraft.client.rendering.layout.events.LMouseEvent;
import dev.hilligans.ourcraft.client.rendering.layout.events.LWidgetEvent;
import org.json.JSONObject;

import java.util.function.Consumer;

public abstract sealed class LWidget permits LBox, LCustomWidget, LImage, LList, LRenderable, LText {

    public WidgetSize widgetSize;
    public WidgetPosition widgetPosition;



    public LWidget() {}

    public LWidget(JSONObject jsonObject) {
        this.widgetPosition = new WidgetPosition(jsonObject);
        this.widgetSize = new WidgetSize(jsonObject);
    }

    public WidgetSize getWidgetSize() {
        return widgetSize;
    }
    public void setWidgetSize(WidgetSize widgetSize) {
        this.widgetSize = widgetSize;
    }

    public WidgetPosition getWidgetPosition() {
        return widgetPosition;
    }
    public void setWidgetPosition(WidgetPosition widgetPosition) {
        this.widgetPosition = widgetPosition;
    };


    public int getX() {
        return getWidgetPosition().x();
    }
    public void setX(int x) {
        setWidgetPosition(new WidgetPosition(x, getY()));
    }

    public int getY() {
        return getWidgetPosition().y();
    }
    public void setY(int y) {
        setWidgetPosition(new WidgetPosition(getX(), y));
    }


    public int getMinWidth() {
        return getWidgetSize().minWidth();
    }
    public void setMinWidth(int minWidth) {
        setWidgetSize(new WidgetSize(minWidth, getMinHeight(), getMaxWidth(), getMaxHeight(), getWidth(), getHeight()));
    }

    public int getMinHeight() {
        return getWidgetSize().minHeight();
    }
    public void setMinHeight(int minHeight) {
        setWidgetSize(new WidgetSize(getMinWidth(), minHeight, getMaxWidth(), getMaxHeight(), getWidth(), getHeight()));
    }


    public int getWidth() {
        return getWidgetSize().maxWidth();
    }
    public void setWidth(int width) {
        setWidgetSize(new WidgetSize(getMinWidth(), getMinHeight(), getMaxWidth(), getMaxHeight(), width, getHeight()));
    }

    public int getHeight() {
        return getWidgetSize().maxHeight();
    }
    public void setHeight(int height) {
        setWidgetSize(new WidgetSize(getMinWidth(), getMinHeight(), getMaxWidth(), getMaxHeight(), getWidth(), height));
    }


    public int getMaxWidth() {
        return getWidgetSize().maxWidth();
    }
    public void setMaxWidth(int maxWidth) {
        setWidgetSize(new WidgetSize(getMinWidth(), getMinHeight(), maxWidth, getMaxHeight(), getWidth(), getHeight()));
    }

    public int getMaxHeight() {
        return getWidgetSize().maxHeight();
    }
    public void setMaxHeight(int maxHeight) {
        setWidgetSize(new WidgetSize(getMinWidth(), getMinHeight(), getMaxWidth(), maxHeight, getWidth(), getHeight()));
    }

    public abstract void recalculate();

    public void serialize(JSONObject json) {
        widgetSize.serialize(json);
        widgetPosition.serialize(json);
    }

    public Consumer<LKeyEvent> keyEvent;
    public Consumer<LMouseEvent> mouseEvent;
    public Consumer<LClickEvent> clickEvent;

    public LWidget withKeyEvent(Consumer<LKeyEvent> keyEvent) {
        this.keyEvent = keyEvent;
        return this;
    }
    public LWidget withMouseEvent(Consumer<LMouseEvent> mouseEvent) {
        this.mouseEvent = mouseEvent;
        return this;
    }
    public LWidget withClickEvent(Consumer<LClickEvent> clickEvent) {
        this.clickEvent = clickEvent;
        return this;
    }

    public void onKeyEvent(LKeyEvent keyEvent) {
        if(this.keyEvent != null) {
            this.keyEvent.accept(keyEvent);
        }
    }
    public void onMouseEvent(LMouseEvent mouseEvent) {
        if(this.mouseEvent != null) {
            this.mouseEvent.accept(mouseEvent);
        }
    }
    public void onMouseClick(LClickEvent mouseEvent) {
        if(this.clickEvent != null) {
            this.clickEvent.accept(mouseEvent);
        }
    }


    public void onEvent(LWidgetEvent event) {
        switch (event) {
            case LClickEvent e -> onMouseClick(e);
            case LKeyEvent e -> onKeyEvent(e);
            case LMouseEvent e -> onMouseEvent(e);
        }
    }
}
