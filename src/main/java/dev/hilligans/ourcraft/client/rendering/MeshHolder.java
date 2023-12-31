package dev.hilligans.ourcraft.client.rendering;

public class MeshHolder {

    public long id = -1;
    public long oldID = 0;

    public int index = 0;
    public int length = 0;

    public int meshTexture = 0;

    public MeshHolder set(long id, int length) {
        oldID = id;
        this.id = id;
        this.length = length;
        return this;
    }

    public long getId() {
        return id;
    }
}
