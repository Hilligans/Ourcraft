package dev.Hilligans.Client.Rendering.World.Managers;

import dev.Hilligans.Client.Rendering.NewRenderer.PrimitiveBuilder;
import dev.Hilligans.Client.Rendering.NewRenderer.TextAtlas;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class ItemTextureManager implements TextureManager {

    public int id;
    public String path;
    public int model;

    public ItemTextureManager(String path) {
        this.path = path;
    }

    public void generate() {
        id = TextAtlas.instance.loadTextureId("Items/" + path,"Items/" + path.substring(0,path.length() - 4),"");
        PrimitiveBuilder primitiveBuilder = new PrimitiveBuilder(GL_TRIANGLES, ShaderManager.worldShader);
    }

    @Override
    public int getTextureId() {
        return id;
    }

    @Override
    public int getTextureMap() {
        return 0;
    }
}
