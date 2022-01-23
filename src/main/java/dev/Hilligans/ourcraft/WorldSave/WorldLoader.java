package dev.Hilligans.ourcraft.WorldSave;

import dev.Hilligans.ourcraft.Ourcraft;
import dev.Hilligans.ourcraft.Resource.ResourceLocation;
import dev.Hilligans.ourcraft.Tag.CompoundNBTTag;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Random;

public class WorldLoader {

    public static int maxSize = 10000000;

    public static void write(String fileName, ByteBuffer byteBuffer) {
        try {
            File file = new File(fileName);
            if(file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            if(byteBuffer == null) {
                throw new RuntimeException("Writing nothing");
            }
            RandomAccessFile aFile = new RandomAccessFile(fileName, "rw");
            FileChannel inChannel = aFile.getChannel();
            inChannel.write(byteBuffer);
            inChannel.close();
        } catch (IOException ingored) {
            ingored.printStackTrace();
        }
    }

    public static CompoundNBTTag loadTag(String path) {
        try {
            ByteBuffer byteBuffer = readBuffer(path);
            if(byteBuffer != null) {
                CompoundNBTTag compoundTag = new CompoundNBTTag();
                compoundTag.readFrom(byteBuffer);
                return compoundTag;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ByteBuffer readBuffer(String path) {
        try {
            File file = new File(path);
            if(file.exists()) {
                RandomAccessFile aFile = new RandomAccessFile(path, "rw");
                int length = (int) aFile.length();
                ByteBuffer buf = ByteBuffer.allocate(length);
                buf.mark();
                aFile.getChannel().read(buf);
                buf.reset();
                return buf;
            }
            return null;
        } catch (Exception ignored) {
            return null;
        }
    }

    public static ByteBuffer readBufferDirect(String path) {
        try {
            File file = new File(path);
            if(file.exists()) {
                RandomAccessFile aFile = new RandomAccessFile(path, "rw");
                int length = (int) aFile.length();
                ByteBuffer buf = ByteBuffer.allocateDirect(length);
                buf.mark();
                aFile.getChannel().read(buf);
                buf.reset();
                return buf;
            }
            return null;
        } catch (Exception ignored) {
            return null;
        }
    }

    public static ByteBuffer readResource(String path) {
        try {
            InputStream inputStream = Ourcraft.getResourceManager().getResource(path);
            byte[] vals = inputStream.readAllBytes();
            ByteBuffer byteBuffer = ByteBuffer.allocate(vals.length);
            byteBuffer.put(vals);
            byteBuffer.flip();
            return byteBuffer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO handle this properly
    public static ByteBuffer readResource(ResourceLocation resourceLocation) {
        return readResource(resourceLocation.path);
    }

    public static String readString(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream stream = WorldLoader.class.getResourceAsStream(path);
        if(stream == null) {
            System.out.println("Cant read file: " + path);
            return "";
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        reader.lines().forEach(string -> stringBuilder.append(string).append("\n"));
        return stringBuilder.toString() + "\n\0";
    }


    public static String readString(InputStream stream) {
        if(stream == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        reader.lines().forEach(string -> stringBuilder.append(string).append("\n"));
        return stringBuilder.toString();
    }

    public static void save(CompoundNBTTag compoundTag, String path) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(maxSize);
        byteBuffer.mark();
        compoundTag.writeTo(byteBuffer);
        byteBuffer.limit(byteBuffer.position());
        byteBuffer.reset();
        write(path,byteBuffer);
    }



}
