package Hilligans.WorldSave;

import Hilligans.Block.Block;
import Hilligans.Block.BlockState;
import Hilligans.Block.Blocks;
import Hilligans.Tag.*;
import Hilligans.Util.Settings;
import Hilligans.World.Chunk;
import Hilligans.World.DataProvider;
import it.unimi.dsi.fastutil.ints.AbstractInt2IntMap;
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

                return createChunk(x, z, compoundTag);
            }
            return null;
            //System.out.println((System.currentTimeMillis() - start));




        } catch (IOException ingored) {
            return null;
        }
    }

    public static void writeChunk(Chunk chunk) {
        String fileName = getPathToChunk(chunk.x,chunk.z);
        CompoundTag compoundTag = createTag(chunk);
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
            blocks[i] = blockState.block.id << 16 | blockState.readData();
        }
        IntegerArrayTag intArrayTag = new IntegerArrayTag(blocks);
        compoundTag.putTag("blocks", intArrayTag);
        CompoundTag compoundTag1 = new CompoundTag();
        compoundTag1.putInt("count",chunk.dataProviders.size());
        int x = 0;
        for(Short2ObjectMap.Entry<DataProvider> set : chunk.dataProviders.short2ObjectEntrySet()) {
            CompoundTag compoundTag2 = new CompoundTag();
            compoundTag2.putShort("pos",set.getShortKey());
            set.getValue().write(compoundTag2);
            compoundTag1.putTag(x + "", compoundTag2);
            x++;

        }
        compoundTag.putTag("data",compoundTag1);

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
            BlockState blockState = block.getDefaultState();
            blockState.write((short) (integerTag.val[i] & 65535));
            chunk.setBlockState(x,y,z,blockState);
        }

        CompoundTag compoundTag1 = (CompoundTag) compoundTag.getTag("data");
        int count = ((IntegerTag)compoundTag1.getTag("count")).val;

        for(int i = 0; i < count; i++) {
            CompoundTag compoundTag2 = (CompoundTag) compoundTag1.getTag(i + "");
            short key = ((ShortTag)compoundTag2.getTag("pos")).val;
            //(short)(pos.x & 15 | (pos.y & 255) << 4 | (pos.z & 15) << 12)
            int x = key & 15;
            int y = (key & 4080) >> 4;
            int z = (key & 61440) >> 12;

            BlockState blockState = chunk.getBlockState(x,y,z);
            DataProvider dataProvider = blockState.block.getDataProvider();
            if(dataProvider != null) {
                dataProvider.read(compoundTag2);
                chunk.dataProviders.put(key,dataProvider);
            } else {
                System.out.println("EMPTY DATA PROVIDER");
            }
            //System.out.println(x + ":" + y + ":" + z);


        }
        //System.out.println(integerTag.val[0]);

        //chunk.populate();
        return chunk;
    }

}
