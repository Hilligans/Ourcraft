package dev.Hilligans.ourcraft.Resource.Loaders;

import dev.Hilligans.ourcraft.Client.Rendering.Misc.WebPHelper;
import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.Image;

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
