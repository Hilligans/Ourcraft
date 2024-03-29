package dev.hilligans.ourcraft.client.rendering.world.managers;


import dev.hilligans.ourcraft.client.rendering.newrenderer.TextAtlas;

public class BlockTextureManager {

    public int[] textures = new int[6];


    public String[] textureNames;
    public String location;
    public String source;

    public String textureSource = "";

    public int[] colors = new int[6];

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

    public void generate(TextAtlas textAtlas) {
        if(location != null) {
            int id = textAtlas.loadTextureId("Blocks/" + location, location.substring(0,location.length() - 4),textureSource);
            for (int x = 0; x < 6; x++) {
                textures[x] = id;

                if (textureNames != null && textureNames[x] != null) {
                    textures[x] = textAtlas.loadTextureId("Blocks/" + textureNames[x], textureNames[x].substring(0,textureNames[x].length() - 4),textureSource);
                }
            }
        }
        if(location != null) {
            for(int x = 0; x < 6; x++) {
                if(textureNames != null) {
                    colors[x] = textAtlas.gameInstance.RESOURCE_MANAGER.getColor("Blocks/" + (textureNames[x] == null ? location : textureNames[x]));
                } else {
                    colors[x] = textAtlas.gameInstance.RESOURCE_MANAGER.getColor("Blocks/" + location);
                }
            }
        }
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
}
