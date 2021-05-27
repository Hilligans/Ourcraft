package Hilligans.Client.Rendering.Widgets;

import Hilligans.Client.MatrixStack;
import Hilligans.Data.Primitives.IntegerWrapper;

import java.util.ArrayList;

public class Widget {

    public int x;
    public int y;
    public int width;
    public int height;
    public short widgetId = -1;
    public String name;

    public int minY;

    public boolean isFocused = false;
    public boolean shouldRender = true;
    public boolean enabled = true;

    public IntegerWrapper yOffset = new IntegerWrapper(0);

    public Widget(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Widget addOffset(IntegerWrapper integerWrapper) {
        this.yOffset = integerWrapper;
        return this;
    }

    public void render(MatrixStack matrixStack, int xOffset, int yOffset) {}

    public void screenClose() {}

    public boolean isInBounds(int x, int y) {
        return x > this.getX() && x < this.getX() + this.width && y > this.getY() && y < this.getY() + this.height;
    }

    public boolean isInBoundsX(int x) {
        return x > this.getX() && x < this.getX() + this.width;
    }

    public void activate(int x, int y) {
        isFocused = true;
    }

    public void mouseScroll(int x, int y, float amount) {}

    public int getX() {
        return x;
    }

    public int getY() {
        return y + yOffset.value;
    }

    public boolean isActive() {
        return this.getY() >= minY;
    }

    public static ArrayList<WidgetFetcher> widgets = new ArrayList<>();

    public static void register() {
        widgets.add(Button::new);
    }



}
