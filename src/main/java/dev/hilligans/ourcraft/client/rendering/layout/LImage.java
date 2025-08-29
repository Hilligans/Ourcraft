package dev.hilligans.ourcraft.client.rendering.layout;

import dev.hilligans.ourcraft.client.rendering.newrenderer.ImageLocation;

public non-sealed class LImage extends LWidget {

    public ImageLocation path;

    public void setImage(ImageLocation path) {
        this.path = path;
    }

    public ImageLocation getImage() {
        return path;
    }

    @Override
    public void recalculate() {
    }
}
