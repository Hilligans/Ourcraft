package Hilligans.Client.Rendering.Widgets;

import Hilligans.Client.MatrixStack;
import Hilligans.Data.Primitives.IntegerWrapper;

public class Widget {

    public int x;
    public int y;
    public int width;
    public int height;

    public int minY;

    public boolean isFocused = false;
    public boolean shouldRender = true;

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
        return x > this.x && x < this.x + this.width && y > this.y && y < this.y + this.height;
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

}
