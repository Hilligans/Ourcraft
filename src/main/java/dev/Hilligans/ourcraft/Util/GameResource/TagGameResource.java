package dev.Hilligans.ourcraft.Util.GameResource;

public class TagGameResource extends GameResource {

    public String tag;

    public TagGameResource(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "TagGameResource{" +
                "tag='" + tag + '\'' +
                '}';
    }
}
