package Hilligans.WorldSave;

import Hilligans.Block.Block;
import Hilligans.Client.Rendering.World.Managers.ShaderManager;
import Hilligans.Data.Other.BlockState;
import Hilligans.Block.Blocks;
import Hilligans.Data.Other.DataBlockState;
import Hilligans.Data.Primitives.DoubleTypeWrapper;
import Hilligans.Tag.*;
import Hilligans.Util.Settings;
import Hilligans.World.Chunk;
import Hilligans.World.DataProvider;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

public class WorldLoader {

    public static int maxSize = 10000000;

    public static void write(String fileName, ByteBuffer byteBuffer) {
        try {
            RandomAccessFile aFile = new RandomAccessFile(fileName, "rw");
            FileChannel inChannel = aFile.getChannel();
            inChannel.write(byteBuffer);
            inChannel.close();
        } catch (IOException ingored) {
            ingored.printStackTrace();
        }
    }

    public static CompoundTag loadTag(String path) {
        try {
            ByteBuffer byteBuffer = readBuffer(path);
            if(byteBuffer != null) {
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.read(byteBuffer);
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

    public static ByteBuffer readResource(String path) {
        try {

            //WorldLoader.class.getResourceAsStream(path).
            //File file = new File(WorldLoader.class.getResource(path).toURI());
            //System.out.println("file exist?" + file.exists());
           // File file = new File(path);
            //if(file.exists()) {
           // ScheduledExecutorService
            InputStream inputStream = WorldLoader.class.getResourceAsStream(path);
            byte[] vals = inputStream.readAllBytes();
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vals.length);
            byteBuffer.put(vals);
            byteBuffer.flip();


            //stbi_load_from_memory()

           // ImageIO.read()
              /*  RandomAccessFile aFile = new RandomAccessFile(path, "rw");
                length = (int) aFile.length();
                ByteBuffer buf = ByteBuffer.allocate(length);
                buf.mark();
                aFile.getChannel().read(buf);
                buf.reset();]

               */
                return byteBuffer;
            //}
            //System.err.println("Failed to find file " + path);
           // return null;
        } catch (Exception e) {
            System.err.println("Failed to find file " + path);
            e.printStackTrace();
            return null;
        }
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
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        reader.lines().forEach(string -> stringBuilder.append(string).append("\n"));
        return stringBuilder.toString();
    }

    public static void save(CompoundTag compoundTag, String path) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(maxSize);
        byteBuffer.mark();
        compoundTag.write(byteBuffer);
        byteBuffer.limit(byteBuffer.position());
        byteBuffer.reset();
        write(path,byteBuffer);
    }



}
