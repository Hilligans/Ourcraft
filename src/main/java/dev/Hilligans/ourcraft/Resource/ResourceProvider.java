package dev.Hilligans.ourcraft.Resource;

import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import dev.Hilligans.ourcraft.Client.Rendering.Texture;

public interface ResourceProvider {

    Image getTexture(String path, String source, ResourceManager resourceManager);

}
