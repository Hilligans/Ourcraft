package Hilligans.Client.Rendering.NewRenderer;

import Hilligans.Ourcraft;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

public class Image {

    public ByteBuffer buffer;
    public int width;
    public int height;
    public int format;

    public Image(int width, int height) {
        this(width,height,4);
    }

    public Image(int width, int height, int format) {
        this(width,height,format,ByteBuffer.allocateDirect(width * height * format));
    }

    public Image(int width, int height, int format, ByteBuffer byteBuffer) {
        this.width = width;
        this.height = height;
        this.format = format;
        this.buffer = byteBuffer;
    }

    public Image(String path, String modId) {
        try {
            byte[] rawImageData = Ourcraft.getResourceManager().getResource(path, modId).readAllBytes();
            ByteBuffer rawData = ByteBuffer.allocateDirect(rawImageData.length);
            rawData.put(rawImageData).flip();

            int[] width = new int[1];
            int[] height = new int[1];
            int[] components = new int[1];
            ByteBuffer temp = STBImage.stbi_load_from_memory(rawData, width, height, components, 4);
            this.buffer = ByteBuffer.allocateDirect(temp.capacity());
            int length = temp.capacity();
            for(int x = 0; x < length / 4; x++) {
                buffer.put(x * 4 + 3,temp.get(length - x * 4 - 1));
                buffer.put(x * 4 + 2,temp.get(length - x * 4 - 2));
                buffer.put(x * 4 + 1,temp.get(length - x * 4 - 3));
                buffer.put(x * 4 + 0,temp.get(length - x * 4 - 4));
            }


            this.width = width[0];
            this.height = height[0];
        } catch (Exception ignored) {
            ignored.printStackTrace();
            System.err.println("Failed to load image " + path + " from mod " + modId);
        }
    }

    public static synchronized Image createImage(String path, String modID) {
        return new Image(path,modID);
    }

    public int getPixel(int x, int y) {
        int pos = x * y * 4;
        return buffer.get(pos) | (buffer.get(pos + 1) << 8) | (buffer.get(pos + 2) << 16) | (buffer.get(pos + 3) << 24);
    }

    public void putPixel(int x, int y, int val) {
        int pos = x * y * 4;
        buffer.put(pos, (byte) val);
        buffer.put(pos + 1, (byte) (val >> 8));
        buffer.put(pos + 2, (byte) (val >> 16));
        buffer.put(pos + 3, (byte) (val >> 24));
    }

    public void putImage(Image image, int x, int y) {
         for(int yy = 0; yy < image.height; yy++) {
          buffer.put((yy + y) * format * width + x * format,image.buffer,yy * format * image.width,image.width * format);
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



}
