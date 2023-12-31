package dev.hilligans.ourcraft.client.rendering.world.managers;

public class ItemTextureManager {

    public int id;
    public String path;
    public int model;

    public ItemTextureManager(String path) {
        this.path = path;
    }

    public void generate() {
       // id = TextAtlas.instance.loadTextureId("Items/" + path,"Items/" + path.substring(0,path.length() - 4),"");
       // PrimitiveBuilder primitiveBuilder = new PrimitiveBuilder(GL_TRIANGLES, ShaderManager.worldShader);
    }
}
