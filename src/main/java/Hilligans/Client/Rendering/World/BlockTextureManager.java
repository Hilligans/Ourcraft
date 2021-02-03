package Hilligans.Client.Rendering.World;

public class BlockTextureManager {

    public int[] textures = new int[6];

    String[] textureNames;
    String location;

    public void addString(String location) {
        this.location = location;
    }

    public void addString(String location, int side) {
        if(textureNames == null) {
            textureNames = new String[6];
        }
        textureNames[side] = location;
    }

    public void generate() {
        int id = TextureManager.instance.loadTextureId(location);
        for(int x = 0; x < 6; x++) {
            textures[x] = id;

            if(textureNames != null && textureNames[x] != null) {
                textures[x] = TextureManager.instance.loadTextureId(textureNames[x]);
            }
        }
    }



}
