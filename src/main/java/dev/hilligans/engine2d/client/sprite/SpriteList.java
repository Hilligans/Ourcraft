package dev.hilligans.engine2d.client.sprite;

import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.api.IMeshBuilder;
import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;

import java.util.ArrayList;

public class SpriteList {

    public Object2LongOpenHashMap<Sprite> sprites = new Object2LongOpenHashMap<>();

    public void build(ArrayList<Sprite> sprites, VertexFormat vertexFormat, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {
        IMeshBuilder builder = graphicsEngine.getDefaultImpl().getMeshBuilder(vertexFormat);

        for(Sprite sprite : sprites) {
            this.sprites.put(sprite, builder.getVertexCount());


        }
    }
}
