package Hilligans.Client.Rendering.Widgets;

import Hilligans.Client.MatrixStack;

public class Widget {

    public int x;
    public int y;
    public int width;
    public int height;

    public boolean isFocused = false;

    public Widget(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }



    public void render(MatrixStack matrixStack) {}

    public void screenClose() {}

    public boolean isInBounds(int x, int y) {
        return x > this.x && x < this.x + this.width && y > this.y && y < this.y + this.height;
    }

    public void activate() {
        isFocused = true;
    }

}
