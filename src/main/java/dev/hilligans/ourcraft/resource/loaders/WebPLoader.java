package dev.hilligans.ourcraft.resource.loaders;

import dev.hilligans.ourcraft.client.rendering.misc.WebPHelper;
import dev.hilligans.ourcraft.client.rendering.newrenderer.Image;

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
