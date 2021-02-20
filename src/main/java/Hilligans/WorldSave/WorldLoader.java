package Hilligans.WorldSave;

import Hilligans.Block.Block;
import Hilligans.Data.Other.BlockState;
import Hilligans.Block.Blocks;
import Hilligans.Data.Other.DataBlockState;
import Hilligans.Tag.*;
import Hilligans.Util.Settings;
import Hilligans.World.Chunk;
import Hilligans.World.DataProvider;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class WorldLoader {

    public static String pathToWorld = "world/" + Settings.worldName + "/";
    public static int maxSize = 10000000;

    public static String getPathToChunk(int x, int z) {
        return pathToWorld + "chunk_x" + x + "z" + z + ".dat";
    }

    public static Chunk readChunk(int x, int z) {
        try {
            //long start = System.currentTimeMillis();
            File file = new File(getPathToChunk(x,z));
            if(file.exists()) {
                RandomAccessFile aFile = new RandomAccessFile(getPathToChunk(x, z), "rw");
                FileChannel inChannel = aFile.getChannel();
                int length = (int) aFile.length();
                ByteBuffer buf = ByteBuffer.allocate(length);
                buf.mark();
                int bytesRead = inChannel.read(buf); //read into buffer.
                //System.out.println(bytesRead);
                buf.reset();
                CompoundTag compoundTag = new CompoundTag();
                compoundTag.read(buf);

                return createChunk2(x, z, compoundTag);
            }
            return null;
            //System.out.println((System.currentTimeMillis() - start));

        } catch (IOException ingored) {
            return null;
        }
    }

    public static void writeChunk(Chunk chunk) {
        String fileName = getPathToChunk(chunk.x,chunk.z);
        CompoundTag compoundTag = createTag2(chunk);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(maxSize);
        byteBuffer.mark();
        compoundTag.write(byteBuffer);
        byteBuffer.limit(byteBuffer.position());
        byteBuffer.reset();
        write(fileName,byteBuffer);
    }



    public static void write(String fileName, ByteBuffer byteBuffer) {
        try {
            RandomAccessFile aFile = new RandomAccessFile(fileName, "rw");
            FileChannel inChannel = aFile.getChannel();
            inChannel.write(byteBuffer);
            inChannel.close();
        } catch (IOException ingored) {}
    }


    public static CompoundTag createTag(Chunk chunk) {
        CompoundTag compoundTag = new CompoundTag();
        int[] blocks = new int[16 * 16 * 256];

        for(int i = 0; i < blocks.length; i++) {
            int x = i & 15;
            int y = i >> 4 & 255;
            int z = i >> 12 & 15;
            BlockState blockState = chunk.getBlockState(x,y,z);
            if(blockState.getBlock().hasBlockState()) {
                blocks[i] = blockState.getBlock().id << 16 | ((DataBlockState)blockState).readData();
            } else {
                blocks[i] = blockState.getBlock().id << 16;
            }
        }
        IntegerArrayTag intArrayTag = new IntegerArrayTag(blocks);
        compoundTag.putTag("blocks", intArrayTag);
        compoundTag.putTag("data",getDataProviderTag(chunk));

        return compoundTag;
    }

    public static CompoundTag createTag2(Chunk chunk) {
        CompoundTag compoundTag = new CompoundTag();
        ListTag<ShortTag> blocks = new ListTag<>();

        for(int i = 0; i < 65536; i++) {
            int x = i & 15;
            int y = i >> 4 & 255;
            int z = i >> 12 & 15;
            BlockState blockState = chunk.getBlockState(x,y,z);
            if(blockState.getBlock().hasBlockState()) {
                blocks.tags.add(new ShortTag(blockState.getBlock().id));
                blocks.tags.add(new ShortTag(((DataBlockState)blockState).readData()));
            } else {
                blocks.tags.add(new ShortTag(blockState.getBlock().id));
            }
        }

        compoundTag.putTag("blocks", blocks);
        compoundTag.putTag("data",getDataProviderTag(chunk));

        return compoundTag;
    }

    public static CompoundTag getDataProviderTag(Chunk chunk) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt("count",chunk.dataProviders.size());
        int x = 0;
        for(Short2ObjectMap.Entry<DataProvider> set : chunk.dataProviders.short2ObjectEntrySet()) {
            CompoundTag compoundTag2 = new CompoundTag();
            compoundTag2.putShort("pos",set.getShortKey());
            set.getValue().write(compoundTag2);
            compoundTag.putTag(x + "", compoundTag2);
            x++;

        }
        return compoundTag;
    }

    public static Chunk createChunk(int X, int Z, CompoundTag compoundTag) {
        Chunk chunk = new Chunk(X,Z,null);

        IntegerArrayTag integerTag = (IntegerArrayTag) compoundTag.getTag("blocks");

        for(int i = 0; i < integerTag.val.length; i++) {
            int x = i & 15;
            int y = i >> 4 & 255;
            int z = i >> 12 & 15;

            //System.out.println(x);
            Block block = Blocks.getBlockWithID((integerTag.val[i]) >> 16 & 65535);
            BlockState blockState = block.getStateWithData((short) (integerTag.val[i] & 65535));
            // blockState.write((short) (integerTag.val[i] & 65535));
            chunk.setBlockState(x,y,z,blockState);
        }

        setDataProviders(compoundTag.getCompoundTag("data"),chunk);

        //chunk.populate();
        return chunk;
    }

    public static Chunk createChunk2(int X, int Z, CompoundTag compoundTag) {
            Chunk chunk = new Chunk(X, Z, null);
        try {
            ListTag<ShortTag> blocks = (ListTag<ShortTag>) compoundTag.getTag("blocks");

            int listSpot = 0;

            for (int i = 0; i < 65536; i++) {
                int x = i & 15;
                int y = i >> 4 & 255;
                int z = i >> 12 & 15;

                Block block = Blocks.getBlockWithID(blocks.tags.get(listSpot).val);
                BlockState blockState;
                if (block.hasBlockState()) {
                    listSpot++;
                    blockState = block.getStateWithData(blocks.tags.get(listSpot).val);
                } else {
                    blockState = block.getDefaultState();
                }
                chunk.setBlockState(x, y, z, blockState);
                listSpot++;
            }

            setDataProviders(compoundTag.getCompoundTag("data"), chunk);
        } catch (Exception e) {
            System.err.println("Failed to load chunk x:" + X + " z:" + Z);
            e.printStackTrace();
            return null;
        }
        return chunk;
    }

    public static void setDataProviders(CompoundTag compoundTag, Chunk chunk) {
        //CompoundTag compoundTag1 = (CompoundTag) compoundTag.getTag("data");
        int count = ((IntegerTag)compoundTag.getTag("count")).val;

        for(int i = 0; i < count; i++) {
            CompoundTag compoundTag2 = (CompoundTag) compoundTag.getTag(i + "");
            short key = ((ShortTag)compoundTag2.getTag("pos")).val;
            int x = key & 15;
            int y = key >> 4 & 255;
            int z = key >> 12 & 15;

            BlockState blockState = chunk.getBlockState(x,y,z);
            DataProvider dataProvider = blockState.getBlock().getDataProvider();
            if(dataProvider != null) {
                dataProvider.read(compoundTag2);
                chunk.dataProviders.put(key,dataProvider);
            } else {
                System.out.println("EMPTY DATA PROVIDER");
            }
        }
    }

}
