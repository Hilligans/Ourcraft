package dev.hilligans.engine.resource.loaders;

import dev.hilligans.engine.client.graphics.resource.Image;
import dev.hilligans.engine.resource.BufferAllocator;
import dev.hilligans.engine.resource.IAllocator;
import dev.hilligans.engine.resource.ResourceLocation;
import dev.hilligans.engine.resource.UniversalResourceLoader;
import dev.hilligans.engine.save.FileLoader;
import org.lwjgl.stb.STBIWriteCallbackI;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

import static org.lwjgl.system.MemoryUtil.memByteBuffer;

public class ImageLoader extends ResourceLoader<Image> implements IAllocator<Image> {

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

        if(width[0] == 0) {
            throw new RuntimeException("Failed to load image: " + STBImage.stbi_failure_reason());
        }
        return new Image(width[0],height[0],4,temp).setAllocator(this);
    }

    @Override
    public Image read(ResourceLocation path) {
        ByteBuffer buffer = gameInstance.getResource(path, new BufferAllocator());
        if (buffer == null) {
            return null;
        }
        Image img = read(buffer);
        MemoryUtil.memFree(buffer);
        return img;
    }

    @Override
    public void write(String path, Image image) {
        String extension = UniversalResourceLoader.getExtension(path);
        final ByteBuffer[] buf = new ByteBuffer[1];
        STBIWriteCallbackI callbackI = (context, data, size) -> buf[0] = memByteBuffer(data, size);
        write(extension, image, callbackI);
        FileLoader.write(path, buf[0]);
    }

    @Override
    public ByteBuffer write(Image image) {
        return null;
    }

    public static void write(String type, Image image, STBIWriteCallbackI writeCallbackI) {
        switch (type) {
            case "png" -> STBImageWrite.stbi_write_png_to_func(writeCallbackI,0,image.width,image.height,image.format,image.buffer,image.format * image.width);
            case "jpg" -> STBImageWrite.stbi_write_jpg_to_func(writeCallbackI,1,image.width,image.height,image.format,image.buffer,image.format * image.width);
            case "bmp" -> STBImageWrite.stbi_write_bmp_to_func(writeCallbackI,2,image.width,image.height,image.format,image.buffer);
            case "tga" -> STBImageWrite.stbi_write_tga_to_func(writeCallbackI,3,image.width,image.height,image.format,image.buffer);
        }
    }

    @Override
    public void free(Image resource) {
        STBImage.stbi_image_free(resource.getBuffer());
    }
}
