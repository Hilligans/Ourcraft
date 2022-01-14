package dev.Hilligans.ourcraft.Resource.Loaders;

import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import dev.Hilligans.ourcraft.Resource.ResourceLocation;
import dev.Hilligans.ourcraft.Resource.UniversalResourceLoader;
import dev.Hilligans.ourcraft.WorldSave.WorldLoader;
import org.lwjgl.stb.STBIWriteCallbackI;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;

import java.nio.ByteBuffer;

import static org.lwjgl.system.MemoryUtil.memByteBuffer;

public class ImageLoader extends ResourceLoader<Image> {

    public ImageLoader() {
        super("default_image_loader", "image");
        withFileTypes("png","jpeg","tga","bmp","psd","gif","hdr","pic","pnm");
    }

    @Override
    public Image read(ByteBuffer buffer) {
        int[] width = new int[1];
        int[] height = new int[1];
        int[] components = new int[1];
        ByteBuffer temp = STBImage.stbi_load_from_memory(buffer, width, height, components, 4);
        return new Image(width[0],height[0],components[0],temp);

    }

    @Override
    public Image read(ResourceLocation path) {
        return read(WorldLoader.readResourceDirect(path));
    }

    @Override
    public void write(String path, Image image) {
        super.write(path, image);
        String extension = UniversalResourceLoader.getExtension(path);
        final ByteBuffer[] buf = new ByteBuffer[1];
        STBIWriteCallbackI callbackI = (context, data, size) -> buf[0] = memByteBuffer(data, size);
        write(extension, image, callbackI);
        WorldLoader.write(path, buf[0]);
    }

    @Override
    public ByteBuffer write(Image image) {
        return null;
    }

    public static void write(String type, Image image, STBIWriteCallbackI writeCallbackI) {
        switch (type) {
            case "png" -> STBImageWrite.stbi_write_png_to_func(writeCallbackI,0,image.width,image.height,image.format,image.buffer,image.format * image.width);
            case "jpg" -> STBImageWrite.stbi_write_jpg_to_func(writeCallbackI,0,image.width,image.height,image.format,image.buffer,image.format * image.width);
            case "bmp" -> STBImageWrite.stbi_write_bmp_to_func(writeCallbackI,0,image.width,image.height,image.format,image.buffer);
            case "tga" -> STBImageWrite.stbi_write_tga_to_func(writeCallbackI,0,image.width,image.height,image.format,image.buffer);
        }
    }
}
