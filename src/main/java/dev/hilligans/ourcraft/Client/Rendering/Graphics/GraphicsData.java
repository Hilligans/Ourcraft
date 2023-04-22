package dev.hilligans.ourcraft.Client.Rendering.Graphics;

import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.hilligans.ourcraft.Client.Rendering.NewRenderer.TextAtlas;

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
        graphicsEngine.getDefaultImpl().destroyTexture(null,null,worldTexture);
    }

    public int getWorldTexture() {
        return worldTexture;
    }
}