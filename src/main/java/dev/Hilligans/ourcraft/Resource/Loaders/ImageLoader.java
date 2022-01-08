package dev.Hilligans.ourcraft.Resource.Loaders;

import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import dev.Hilligans.ourcraft.Resource.ResourceLoader;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;

public class ImageLoader extends ResourceLoader<Image> {

    public ImageLoader() {
        super("default_image_loader", "image");
        withFileTypes("png","jpeg","tga","gmp","psd","gif","hdr","pic","pnm");
    }

    @Override
    public Image getResource(ByteBuffer buffer) {
        int[] width = new int[1];
        int[] height = new int[1];
        int[] components = new int[1];
        ByteBuffer temp = STBImage.stbi_load_from_memory(buffer, width, height, components, 4);
        ByteBuffer outBuffer = ByteBuffer.allocateDirect(temp.capacity());
        int length = temp.capacity();
        for(int x = 0; x < length / 4; x++) {
            outBuffer.put(x * 4 + 3,temp.get(length - x * 4 - 1));
            outBuffer.put(x * 4 + 2,temp.get(length - x * 4 - 2));
            outBuffer.put(x * 4 + 1,temp.get(length - x * 4 - 3));
            outBuffer.put(x * 4 + 0,temp.get(length - x * 4 - 4));
        }
        return new Image(width[0],height[0],4,outBuffer);
    }

    @Override
    public ByteBuffer write(Image image) {
        return null;
    }
}
