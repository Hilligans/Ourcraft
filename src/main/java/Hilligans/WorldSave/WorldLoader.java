package Hilligans.WorldSave;

import Hilligans.Block.Block;
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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
            File file = new File(path);
            if(file.exists()) {
                RandomAccessFile aFile = new RandomAccessFile(path, "rw");
                int length = (int) aFile.length();
                ByteBuffer buf = ByteBuffer.allocate(length);
                buf.mark();
                int bytesRead = aFile.getChannel().read(buf);
                buf.reset();
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.read(buf);
                return compoundTag;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
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
