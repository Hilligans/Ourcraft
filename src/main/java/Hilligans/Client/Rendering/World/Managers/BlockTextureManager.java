package Hilligans.Client.Rendering.World.Managers;


import Hilligans.ClientMain;
import Hilligans.Util.Settings;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

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

    public void addStrings(String[] strings) {
        for(int x = 0; x < strings.length; x++) {
            if(x == 0) {
                addString(strings[x]);
            } else {
                addString(strings[x],x - 1);
            }
        }
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

    public HashMap<String, BufferedImage> getAllTextures() {
        HashMap<String, BufferedImage> map = new HashMap<>();
        if(textureNames != null) {
            for (String string : textureNames) {
                if (!map.containsKey(string)) {
                    map.put(string, WorldTextureManager.loadImage("Blocks/" + string));
                }
            }
        }
        if(location != null) {
            if(!map.containsKey(location)) {
                map.put(location, WorldTextureManager.loadImage("Blocks/" + location));
            }
        }

        return map;
    }

    public String[] getTextures() {
        if(textureNames != null) {
            String[] string = new String[textureNames.length + 1];
            System.arraycopy(textureNames,0,string,1,textureNames.length);
            string[0] = location;
            return string;
        } else {
            return new String[]{location};
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
