package Hilligans.Client.Rendering;

import Hilligans.Client.Rendering.World.Managers.TextureManager;

import java.awt.image.BufferedImage;

public class Texture {

    String path;

    public int width;
    public int height;

    public int textureId;

    public Texture(String path) {
        this.path = path;
        Textures.textures.add(this);
    }

    public void register() {
        BufferedImage bufferedImage = TextureManager.loadImage(path);
        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();
        textureId = TextureManager.registerTexture(bufferedImage);
    }



}
