package Hilligans.Client.Rendering;

import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;

import java.awt.image.BufferedImage;

public class Texture {

    String path;

    public int width;
    public int height;

    public int textureId;

    public String name;

    BufferedImage texture;

    public Texture(String path) {
        this.path = path;
        Textures.textures.add(this);
    }

    public Texture(String name, BufferedImage texture) {
        width = texture.getWidth();
        height = texture.getHeight();
        this.texture = texture;
        this.name = name;
    }

    public void register() {
        BufferedImage bufferedImage = WorldTextureManager.loadImage(path);
        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();
        textureId = WorldTextureManager.registerTexture(bufferedImage);
    }

    public void register1() {
        textureId = WorldTextureManager.registerTexture(texture);
    }



}
