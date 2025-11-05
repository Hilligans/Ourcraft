package dev.hilligans.engine.client.graphics.util;

import dev.hilligans.engine.client.graphics.api.TextureFormat;
import dev.hilligans.engine.client.graphics.resource.Image;
import dev.hilligans.engine.util.registry.IRegistryElement;

public interface ITextureConverter extends IRegistryElement {

    TextureFormat[] getSourceFormats();
    TextureFormat[] getTargetFormats();

    Image convert(Image source, TextureFormat target);

    default String getResourceType() {
        return "texture_converter";
    }
}
