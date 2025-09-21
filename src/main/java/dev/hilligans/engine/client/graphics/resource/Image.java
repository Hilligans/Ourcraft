package dev.hilligans.engine.client.graphics.resource;

import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.engine.client.graphics.api.IDefaultEngineImpl;
import dev.hilligans.engine.resource.IAllocator;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Image {

    public ByteBuffer buffer;
    public int width;
    public int height;
    public int format;

    public IAllocator<Image> allocator;

    public Image(int width, int height) {
        this(width,height,4);
    }

    public Image(int width, int height, int format) {
        this(width,height,format, ByteBuffer.allocateDirect(width * height * format));
    }

    public Image(int width, int height, int format, ByteBuffer byteBuffer) {
        this.width = width;
        this.height = height;
        this.format = format;
        this.buffer = byteBuffer;
    }

    public Image(byte[] rawImageData) {
        try {
            ByteBuffer rawData = ByteBuffer.allocateDirect(rawImageData.length);
            rawData.put(rawImageData).flip();

            int[] width = new int[1];
            int[] height = new int[1];
            int[] components = new int[1];
            this.buffer = STBImage.stbi_load_from_memory(rawData, width, height, components, 4);


            this.width = width[0];
            this.height = height[0];
        } catch (Exception ignored) {
            ignored.printStackTrace();
            System.err.println("Failed to create image");
        }
    }

    public Image(BufferedImage image) {
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.format = 4;
        buffer = ByteBuffer.allocateDirect(width * height * 4);
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                putPixel(x,y,image.getRGB(x,y));
            }
        }
    }

    public Image setAllocator(IAllocator<Image> allocator) {
        this.allocator = allocator;
        return this;
    }

    public void free() {
        if(allocator == null) {
            throw new RuntimeException();
        }
        allocator.free(this);
        buffer = null;
    }

    public int getPixel(int x, int y) {
        int pos = x * format + y * format * width;
        return buffer.getInt(pos);
    }

    public int getSmallPixel(int x, int y) {
        int pos = x * format + y * format * width;
        return buffer.get(pos) | (buffer.get(pos + 1) << 8) | (buffer.get(pos + 2) << 16);
    }

    public void putPixel(int x, int y, int val) {
        int pos = x * format + y * format * width;
        buffer.put(pos, (byte) val);
        buffer.put(pos + 1, (byte) (val >> 8));
        buffer.put(pos + 2, (byte) (val >> 16));
        buffer.put(pos + 3, (byte) (val >> 24));
    }

    public void putSmallPixel(int x, int y, int val) {
        int pos = getPos(x, y);
        buffer.put(pos, (byte) val);
        buffer.put(pos + 1, (byte) (val >> 8));
        buffer.put(pos + 2, (byte) (val >> 16));
    }

    public int getPos(int x, int y) {
        return x * format + y * format * width;
    }

    public void putImage(Image image, int x, int y) {
        if(format != image.format) {
            for(int xx = 0; xx < image.getWidth(); xx++) {
                for(int yy = 0; yy < image.getHeight(); yy++) {
                    putPixel(x + xx, y + yy, image.getSmallPixel(xx, yy));
                }
            }
            return;
        }
         for(int yy = 0; yy < image.height; yy++) {
          buffer.put((yy + y) * format * width + x * format,image.buffer,yy * format * image.width,image.width * format);
        }
    }

    public void fill(int rgba) {
        for(int x = 0; x < width * height; x++) {
            buffer.putInt(x,rgba);
        }
    }

    public void print() {
        byte[] vals = new byte[width * height * 4];
        buffer.get(vals);
        System.out.println(Arrays.toString(vals));
    }

    public void print1() {
        byte[] vals = new byte[width * height * 4];
        buffer.get(vals);
        for(byte val : vals) {
            if(val != 0) {
                System.out.println(val);
            }
        }
    }

    public void setAlpha() {
        for(int x = 0; x < width * height; x++) {
            buffer.put(x * 4 + 3, (byte) 255);
        }

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFormat() {
        return format;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public int getSize() {
        return width * height * 4;
    }

    public BufferedImage toBufferedImage() {
        BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                bufferedImage.setRGB(x,y,getPixel(x,y));
            }
        }
        return bufferedImage;
    }

    public Image ensureSquare() {
        this.height = this.width;
        return this;
    }

    public ByteBuffer mallocSizedBuffer() {
        return MemoryUtil.memAlloc(getWidth() * getHeight() * format);
    }

    public Image flip(boolean horizontal, ByteBuffer buffer) {
         Image tempImage = new Image(getWidth(), getHeight(), format, buffer);

        int width = getWidth();
        int height = getHeight();

        if(format == 3) {
        } else {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (horizontal) {
                        tempImage.putPixel(x, height - y - 1, getPixel(x, y));
                    } else {
                        tempImage.putPixel(width - x - 1, height - y - 1, getPixel(x, y));
                    }
                }
            }
        }
        free();
        allocator = resource -> MemoryUtil.memFree(resource.buffer);
        this.buffer = tempImage.buffer;
        return this;
    }

    public ImageInfo upload(IDefaultEngineImpl<?, ?, ?> imp, GraphicsContext graphicsContext) {
        long id = imp.createTexture(graphicsContext, this);
        return new ImageInfo(width, height, format, id);
    }

    @Override
    public String toString() {
        return "Image{" +
                "width=" + width +
                ", height=" + height +
                ", format=" + format +
                '}';
    }
}
