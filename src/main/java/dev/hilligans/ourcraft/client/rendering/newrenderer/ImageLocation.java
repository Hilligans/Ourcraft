package dev.hilligans.ourcraft.client.rendering.newrenderer;

public class ImageLocation {

    public String path;
    public String modId;

    public int index;
    public TextAtlas textAtlas;

    public ImageLocation(String name, String modId) {
        if(modId.equals("")) {
            modId = "ourcraft";
        }
        this.path = name;
        this.modId = modId;
    }

    @Override
    public String toString() {
        return "ImageLocation{" +
                "name='" + path + '\'' +
                ", modId='" + modId + '\'' +
                ", index=" + index +
                ", textAtlas=" + textAtlas +
                '}';
    }
}
