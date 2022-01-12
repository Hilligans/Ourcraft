package dev.Hilligans.ourcraft.Client.Rendering.Misc;

import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.Image;
import dev.Hilligans.ourcraft.Util.BitArray;
import dev.Hilligans.ourcraft.Util.BitStream;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class WebPHelper {

    public static ByteBuffer read(ByteBuffer buffer) {
        int firstCode = buffer.getInt();
        WebP webP = new WebP(buffer.getInt());
        int secondCode = buffer.getInt();
        if(firstCode != 1380533830 && secondCode != 1464156752) {
            return null;
        }


        return null;
    }

    public static ByteBuffer write(Image image) {
        ByteArrayList list = new ByteArrayList();
        BitStream stream = new BitStream();
        add(list, "RIFF");
        add(list, 0); //block length
        add(list, "WEBP");
        add(list, "VP8L");
        add(list, 0); //bytes in stream
        add(list, (byte)0x2F);

        stream.put(14, image.getWidth() - 1);
        stream.put(14, image.getHeight() - 1);
        stream.put(1, image.format == 4 ? 1 : 0);
        stream.put(3, 0); //version number

        stream.put(3, 0); //block size
        stream.put(8, 0); //color table size

        return null;
    }

    public static void add(ByteArrayList list, byte... values) {
        for(byte b : values) {
            list.add(b);
        }
    }

    public static void add(ByteArrayList list, int... values) {
        for(int b : values) {
            list.add((byte) ((b >> 0) & 0xFF));
            list.add((byte) ((b >> 8) & 0xFF));
            list.add((byte) ((b >> 16) & 0xFF));
            list.add((byte) ((b >> 24) & 0xFF));
        }
    }

    public static void add(ByteArrayList list, String s) {
        add(list, s.getBytes(StandardCharsets.US_ASCII));
    }


    static class WebP {

        public int size;

        public WebP(int size) {
            this.size = size;
        }


    }

}
