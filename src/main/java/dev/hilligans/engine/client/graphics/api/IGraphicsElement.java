package dev.hilligans.engine.client.graphics.api;

import dev.hilligans.engine.GameInstance;

public interface IGraphicsElement {

    void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext);

    void cleanup(GameInstance gameInstance, IGraphicsEngine<?,?,?> graphicsEngine, GraphicsContext graphicsContext);
}
