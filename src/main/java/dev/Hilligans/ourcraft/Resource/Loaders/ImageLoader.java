package dev.Hilligans.ourcraft.Resource.Loaders;

import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import dev.Hilligans.ourcraft.Resource.ResourceLoader;
import dev.Hilligans.ourcraft.WorldSave.WorldLoader;
import org.lwjgl.stb.STBImage;

import java.io.FileInputStream;
import java.nio.ByteBuffer;

public class ImageLoader extends ResourceLoader<Image> {

    public ImageLoader() {
        super("default_image_loader", "image");
        withFileTypes("png","jpeg","tga","gmp","psd","gif","hdr","pic","pnm");
    }

    @Override
    public Image read(ByteBuffer buffer) {
        int[] width = new int[1];
        int[] height = new int[1];
        int[] components = new int[1];
        ByteBuffer temp = STBImage.stbi_load_from_memory(buffer, width, height, components, 4);
        return new Image(width[0],height[0],4,temp);
    }

    @Override
    public Image read(String path) {
        return read(WorldLoader.readResource(path, true));
    }

    @Override
    public ByteBuffer write(Image image) {
        return null;
    }
}
