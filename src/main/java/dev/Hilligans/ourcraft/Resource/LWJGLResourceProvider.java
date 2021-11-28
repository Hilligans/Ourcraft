package dev.Hilligans.ourcraft.Resource;

import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import dev.Hilligans.ourcraft.Client.Rendering.Texture;

public class LWJGLResourceProvider implements ResourceProvider {

    //TODO properly handle exception
    @Override
    public Image getTexture(String path, String modId, ResourceManager resourceManager) {
        try {
            return new Image(resourceManager.getResource(path, modId).readAllBytes());
        } catch (Exception e) {
            return null;
        }
    }
}
