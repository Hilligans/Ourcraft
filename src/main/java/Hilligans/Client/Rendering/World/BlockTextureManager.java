package Hilligans.Client.Rendering.World;

import java.awt.image.BufferedImage;

public class BlockTextureManager {

    public int[] textures = new int[6];

    public int texture;

    String[] textureNames;
    String location;

    public void addString(String location) {
        this.location = location;
    }

    public void addString(String location, int side) {
        if(this.location == null) {
            this.location = location;
        }
        if(textureNames == null) {
            textureNames = new String[6];
        }
        textureNames[side] = location;
    }

    public void generate() {
        if(location != null) {
            int id = TextureManager.instance.loadTextureId("/Blocks/" + location);
            for (int x = 0; x < 6; x++) {
                textures[x] = id;

                if (textureNames != null && textureNames[x] != null) {
                    textures[x] = TextureManager.instance.loadTextureId("Blocks/" + textureNames[x]);
                }
            }

            texture = TextureManager.loadAndRegisterUnflippedTexture("Blocks/" + location);
        }
    }



}
