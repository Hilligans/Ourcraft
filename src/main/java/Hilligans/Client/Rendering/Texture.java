package Hilligans.Client.Rendering;

import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;

import java.awt.image.BufferedImage;

public class Texture {

    String path;
    String source;

    public int width;
    public int height;

    public int textureId;

    BufferedImage texture;

    public Texture(String path) {
        this(path,"");
    }

    public Texture(String path, String source) {
        this.path = path;
        this.source = source;
        Textures.TEXTURES.add(this);
    }

    public Texture(BufferedImage texture) {
        width = texture.getWidth();
        height = texture.getHeight();
        this.texture = texture;
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
