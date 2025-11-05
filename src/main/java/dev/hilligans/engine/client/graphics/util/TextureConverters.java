package dev.hilligans.engine.client.graphics.util;

import dev.hilligans.engine.client.graphics.api.TextureFormat;
import dev.hilligans.engine.client.graphics.resource.Image;
import dev.hilligans.engine.resource.IAllocator;
import org.lwjgl.stb.STBDXT;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBDXT.STB_DXT_NORMAL;

public class TextureConverters {


    public static ITextureConverter dxt1Compressor = new ITextureConverter() {
        @Override
        public TextureFormat[] getSourceFormats() {
            return new TextureFormat[] {TextureFormat.RGB};
        }

        @Override
        public TextureFormat[] getTargetFormats() {
            return new TextureFormat[] {TextureFormat.DXT1};
        }

        @Override
        public Image convert(Image source, TextureFormat target) {
            int width = (source.width + 3) & ~0x03;
            int height = (source.height + 3) & ~0x03;

            ByteBuffer newImage = MemoryUtil.memAlloc(width * height/2);
            ByteBuffer tempBuffer = MemoryUtil.memAlloc(64);


            int x = 0;
            int y = 0;
            int position = 0;

            while(y != height) {
                if(x + 3 >= source.width || y + 3 >= source.height) {
                    throw new RuntimeException("");
                } else {
                    /* faster ish code */
                    for(int yy = y; yy < y + 4; yy++) {
                        for(int xx = x; xx < x + 4; xx++) {
                            tempBuffer.putInt(source.buffer.getInt((yy * source.width + xx) * 4));
                        }
                    }
                }

                tempBuffer.flip();
                STBDXT.stb_compress_dxt_block(newImage, tempBuffer, false, STB_DXT_NORMAL);
                position += 8;
                newImage.position(position);


                x += 4;
                if(x == width) {
                    x = 0;
                    y += 4;
                }
            }

            MemoryUtil.memFree(tempBuffer);
            return new Image(width, height, TextureFormat.DXT1, newImage.flip(), resource -> MemoryUtil.memFree(resource.buffer));
        }

        @Override
        public String getResourceName() {
            return "dxt1_converter";
        }

        @Override
        public String getResourceOwner() {
            return "ourcraft";
        }
    };

    public static ITextureConverter dxt5Compressor = new ITextureConverter() {
        @Override
        public TextureFormat[] getSourceFormats() {
            return new TextureFormat[] {TextureFormat.RGBA};
        }

        @Override
        public TextureFormat[] getTargetFormats() {
            return new TextureFormat[] {TextureFormat.DXT5};
        }

        @Override
        public Image convert(Image source, TextureFormat target) {
            int width = (source.width + 3) & ~0x03;
            int height = (source.height + 3) & ~0x03;

            ByteBuffer newImage = MemoryUtil.memAlloc(width * height);
            ByteBuffer tempBuffer = MemoryUtil.memAlloc(64);

            int x = 0;
            int y = 0;
            int position = 0;

            while(y != height) {
                if(x + 3 >= source.width || y + 3 >= source.height) {
                    throw new RuntimeException("");
                } else {
                    /* faster ish code */
                    for(int yy = y; yy < y + 4; yy++) {
                        for(int xx = x; xx < x + 4; xx++) {
                            tempBuffer.putInt(source.buffer.getInt((yy * source.width + xx) * 4));
                        }
                    }
                }

                tempBuffer.flip();
                STBDXT.stb_compress_dxt_block(newImage, tempBuffer, true, STB_DXT_NORMAL);
                position += 16;
                newImage.position(position);


                x += 4;
                if(x == width) {
                    x = 0;
                    y += 4;
                }
            }

            MemoryUtil.memFree(tempBuffer);
            return new Image(width, height, TextureFormat.DXT5, newImage.flip(), resource -> MemoryUtil.memFree(resource.buffer));
        }

        @Override
        public String getResourceName() {
            return "dxt5_converter";
        }

        @Override
        public String getResourceOwner() {
            return "ourcraft";
        }
    };

}
