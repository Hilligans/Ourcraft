package dev.hilligans.engine.client.graphics.resource;

import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IDefaultEngineImpl;

public record ImageInfo(int width, int height, int format, long imageID) {

    public void cleanup(IDefaultEngineImpl<?, ?, ?> impl, GraphicsContext graphicsContext) {
        impl.destroyTexture(graphicsContext, imageID);
    }
}
