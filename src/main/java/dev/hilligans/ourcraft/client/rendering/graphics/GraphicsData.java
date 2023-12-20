package dev.hilligans.ourcraft.client.rendering.graphics;

import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.client.rendering.graphics.api.IGraphicsEngine;
import dev.hilligans.ourcraft.client.rendering.newrenderer.TextAtlas;

public class GraphicsData {

    public IGraphicsEngine<?,?,?> graphicsEngine;

    public int worldTexture;
    public TextAtlas worldTextureAtlas;

    public GraphicsData(IGraphicsEngine<?,?,?> graphicsEngine) {
        this.graphicsEngine = graphicsEngine;
    }

    public void build() {
        buildTextures();
    }

    public void buildTextures() {
        worldTextureAtlas = new TextAtlas();

        TextAtlas.instance = worldTextureAtlas;
        for(Block block : graphicsEngine.getGameInstance().BLOCKS.ELEMENTS) {
            block.generateTextures(worldTextureAtlas);
        }

        worldTexture = worldTextureAtlas.upload(graphicsEngine);
    }

    public void clear() {
        clearTextures();
    }

    ///TODO fix
    public void clearTextures() {
        graphicsEngine.getDefaultImpl().destroyTexture(null,worldTexture);
    }

    public int getWorldTexture() {
        return worldTexture;
    }
}
