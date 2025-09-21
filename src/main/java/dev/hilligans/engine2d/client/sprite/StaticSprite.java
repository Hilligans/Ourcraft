package dev.hilligans.engine2d.client.sprite;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IGraphicsEngine;
import dev.hilligans.engine.client.graphics.api.IIndirectBuilder;
import dev.hilligans.engine.client.graphics.api.IMeshBuilder;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;

public class StaticSprite implements Sprite {


    @Override
    public String getResourceName() {
        return "";
    }

    @Override
    public String getResourceOwner() {
        return "";
    }

    @Override
    public void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {

    }

    @Override
    public void cleanup(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext) {

    }

    @Override
    public void build(IMeshBuilder builder) {

    }

    @Override
    public void draw(ISpriteEntity entity, IIndirectBuilder builder) {

    }

    @Override
    public void draw(ISpriteEntity entity, IGraphicsEngine<?, ?, ?> engine, GraphicsContext graphicsContext, MatrixStack matrixStack) {

    }
}
