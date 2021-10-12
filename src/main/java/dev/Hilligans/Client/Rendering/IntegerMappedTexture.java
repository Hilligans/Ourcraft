package dev.Hilligans.Client.Rendering;

import dev.Hilligans.Client.MatrixStack;
import dev.Hilligans.Data.Primitives.IntegerWrapper;

public class IntegerMappedTexture {

    public static final int HORIZONTAL_LINEAR = 0;
    public static final int VERTICAL_LINEAR = 1;

    public Texture texture;
    public int mode;
    public int x;
    public int y;
    public int startX;
    public int startY;
    public int endX;
    public int endY;
    public IntegerWrapper integerWrapper;

    public IntegerMappedTexture(Texture texture, int mode, IntegerWrapper integerWrapper, int x, int y, int startX, int startY, int endX, int endY) {
        this.texture = texture;
        this.mode = mode;
        this.x = x;
        this.y = y;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.integerWrapper = integerWrapper;
    }

    public void render(MatrixStack matrixStack) {
        if(mode == 0) {
            texture.drawTexture(matrixStack, x, y, startX, startY, endX, endY);
        }
    }
}
