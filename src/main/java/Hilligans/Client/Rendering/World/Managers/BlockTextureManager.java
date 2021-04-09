package Hilligans.Client.Rendering.World.Managers;


import Hilligans.ClientMain;
import Hilligans.Util.Settings;

public class BlockTextureManager implements TextureManager {

    public int[] textures = new int[6];

    public int texture;

    public String[] textureNames;
    public String location;

    public String textureSource = "";

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
            int id = WorldTextureManager.instance.loadTextureId("Blocks/" + location, location.substring(0,location.length() - 4),textureSource);
            for (int x = 0; x < 6; x++) {
                textures[x] = id;

                if (textureNames != null && textureNames[x] != null) {
                    textures[x] = WorldTextureManager.instance.loadTextureId("Blocks/" + textureNames[x], textureNames[x].substring(0,textureNames[x].length() - 4),textureSource);
                }
            }
           // if(!Settings.isServer) {
                //texture = WorldTextureManager.loadAndRegisterUnflippedTexture("Blocks/" + location, textureSource);

          //  }
        }
    }


    @Override
    public int getTextureId() {
        return textures[0];
    }

    @Override
    public int getTextureMap() {
        return ClientMain.getClient().texture;
    }
}
