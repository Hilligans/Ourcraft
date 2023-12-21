package dev.hilligans.ourcraft.client.rendering.graphics.api;

import dev.hilligans.ourcraft.GameInstance;

public interface IGraphicsElement {

    void load(GameInstance gameInstance, IGraphicsEngine<?, ?, ?> graphicsEngine, GraphicsContext graphicsContext);

    void cleanup(GameInstance gameInstance, IGraphicsEngine<?,?,?> graphicsEngine, GraphicsContext graphicsContext);
}
