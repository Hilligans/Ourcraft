package dev.Hilligans.ourcraft.Client.Rendering.Graphics;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.IGraphicsEngine;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.TextAtlas;

public class GraphicsData {

    public IGraphicsEngine<?,?> graphicsEngine;

    public int worldTexture;
    public TextAtlas worldTextureAtlas;

    public GraphicsData(IGraphicsEngine<?,?> graphicsEngine) {
        this.graphicsEngine = graphicsEngine;
    }

    public void build() {
        buildTextures();
    }

    public void buildTextures() {
        worldTextureAtlas = new TextAtlas();

        for(Block block : graphicsEngine.getGameInstance().BLOCKS.ELEMENTS) {
            block.generateTextures(worldTextureAtlas);
        }

        worldTexture = graphicsEngine.getDefaultImpl().createTexture(null,worldTextureAtlas.image);
    }

    public void clear() {
        clearTextures();
    }

    public void clearTextures() {
        graphicsEngine.getDefaultImpl().destroyTexture(null,worldTexture);
    }

    public int getWorldTexture() {
        return worldTexture;
    }
}
