package Hilligans.Client.Rendering.World.Managers;

import java.awt.image.BufferedImage;

public class FlatTextureManager implements TextureManager {

    public int texture;
    public int textureLocation;
    public String texturePath;
    public String textureSource;

    public FlatTextureManager(String texturePath, String textureSource) {
        this.texturePath = texturePath;
        this.textureSource = textureSource;
    }

    public FlatTextureManager(String texturePath) {
        this(texturePath,"");
    }

    public void generate() {
        BufferedImage image = WorldTextureManager.loadImage(texturePath,textureSource);
    }

    @Override
    public int getTextureId() {
        return 0;
    }

    @Override
    public int getTextureMap() {
        return 0;
    }
}
