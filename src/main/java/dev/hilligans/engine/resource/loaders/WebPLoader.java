package dev.hilligans.engine.resource.loaders;

import dev.hilligans.ourcraft.client.rendering.misc.WebPHelper;
import dev.hilligans.engine.client.graphics.resource.Image;

import java.nio.ByteBuffer;

public class WebPLoader extends ResourceLoader<Image> {

    public WebPLoader() {
        super("webp_loader", "image");
    }

    @Override
    public Image read(ByteBuffer buffer) {
        return null;
    }

    @Override
    public ByteBuffer write(Image image) {
        return WebPHelper.write(image);
    }
}
