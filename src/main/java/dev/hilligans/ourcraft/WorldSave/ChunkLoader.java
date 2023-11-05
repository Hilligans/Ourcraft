package dev.hilligans.ourcraft.WorldSave;

import dev.hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.hilligans.ourcraft.Data.Other.BlockStates.DataBlockState;
import dev.hilligans.ourcraft.Data.Primitives.Tuple;
import dev.hilligans.ourcraft.Tag.*;
import dev.hilligans.ourcraft.Util.Settings;
import dev.hilligans.ourcraft.World.Chunk;
import dev.hilligans.ourcraft.World.DataProvider;
import dev.hilligans.ourcraft.World.NewWorldSystem.IChunk;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ChunkLoader {

    public static String pathToWorld = "world/" + Settings.worldName + "/dim-0/";
    public static String getPathToChunk(int x, int z) {
        return pathToWorld + "chunks/" + "x" + x + "_z" + z + ".dat";
    }

    public static HashMap<String, CompoundNBTTag> loadedGroups = new HashMap<>();
    public static HashMap<String,Level> levels = new HashMap<>();
   // public static HashMap<String, CompoundNBTTag> loadedData = new HashMap<>();

   // public static Level level = new Level();


    public static CompoundNBTTag createTag(Chunk chunk) {
        CompoundNBTTag compoundTag = new CompoundNBTTag();
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
        IntegerArrayNBTTag intArrayTag = new IntegerArrayNBTTag(blocks);
        compoundTag.putTag("blocks", intArrayTag);
        compoundTag.putTag("data",getDataProviderTag(chunk));

        return compoundTag;
    }

    public static CompoundNBTTag createTag2(Chunk chunk) {
        CompoundNBTTag compoundTag = new CompoundNBTTag();
        ListNBTTag<ShortNBTTag> blocks = new ListNBTTag<>();

        for(int i = 0; i < 65536; i++) {
            int x = i & 15;
            int y = i >> 4 & 255;
            int z = i >> 12 & 15;
            BlockState blockState = chunk.getBlockState(x,y,z);
            if(blockState.getBlock().hasBlockState()) {
                blocks.tags.add(new ShortNBTTag(blockState.getBlock().id));
                blocks.tags.add(new ShortNBTTag(((DataBlockState)blockState).readData()));
            } else {
                blocks.tags.add(new ShortNBTTag(blockState.getBlock().id));
            }
        }

        compoundTag.putTag("blocks", blocks);
        compoundTag.putTag("data",getDataProviderTag(chunk));

        return compoundTag;
    }

    public static CompoundNBTTag createTag3(Chunk chunk, Level level) {
        CompoundNBTTag compoundTag = new CompoundNBTTag();

        ListNBTTag<ShortNBTTag> blocks = new ListNBTTag<>();

        ArrayList<Tuple<BlockState,Integer>> blockList = chunk.getBlockChainedList();

        for(Tuple<BlockState,Integer> block : blockList) {
            int id = level.ensureHasBlock(block.typeA.getBlock());
            blocks.tags.add(new ShortNBTTag((short) id));
            if(block.getTypeA().getBlock().hasBlockState()) {
                if(block.getTypeA() instanceof DataBlockState) {
                    blocks.tags.add(new ShortNBTTag(((DataBlockState) block.getTypeA()).readData()));
                } else {
                    System.out.println(block.typeA.getBlock().name);
                }
            }
            int val = block.getTypeB();
            blocks.tags.add(new ShortNBTTag((short)val));
        }
        compoundTag.putTag("blocks", blocks);
        compoundTag.putTag("data",getDataProviderTag(chunk));


        return compoundTag;
    }

    public static CompoundNBTTag getDataProviderTag(Chunk chunk) {
        CompoundNBTTag compoundTag = new CompoundNBTTag();
        int x = 0;
        for(Short2ObjectMap.Entry<DataProvider> set : chunk.dataProviders.short2ObjectEntrySet()) {
            CompoundNBTTag compoundTag2 = new CompoundNBTTag();
            compoundTag2.putShort("pos",set.getShortKey());
            //TODO: sometimes returns null and causes an exception
            if(set.getValue() != null) {
                set.getValue().write(compoundTag2);
                compoundTag.putTag(x + "", compoundTag2);
                x++;
            }
        }
        compoundTag.putInt("count",x);
        return compoundTag;
    }


    /*
    public static Chunk createChunk2(int X, int Z, CompoundNBTTag compoundTag) {
        Chunk chunk = new Chunk(X, Z, null);
        try {
            ListNBTTag<ShortNBTTag> blocks = (ListNBTTag<ShortNBTTag>) compoundTag.getTag("blocks");

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

     */

    /*
    public static Chunk createChunk3(int X, int Z, CompoundNBTTag compoundTag, Level level) {
        Chunk chunk = new Chunk(X, Z, null);
        try {
            ListNBTTag<ShortNBTTag> blocks = (ListNBTTag<ShortNBTTag>) compoundTag.getTag("blocks");
            ArrayList<Tuple<BlockState,Integer>> blockList = new ArrayList<>();

            int offset = 0;

            while(offset < blocks.tags.size()) {
                short val = blocks.tags.get(offset).val;
                Block block = level.getBlock(val);
                offset++;
                BlockState blockState;
                if(level.getBlockStateSize(val) != 0) {
                    blockState = block.getStateWithData(val);
                    offset++;
                } else {
                    blockState = block.getDefaultState();
                }
                blockList.add(new Tuple<>(blockState,blocks.tags.get(offset).val & 0xFFFF));
                offset++;
            }

            chunk.setFromChainedList(blockList);

            setDataProviders(compoundTag.getCompoundTag("data"), chunk);
        } catch (Exception e) {
            System.err.println("Failed to load chunk x:" + X + " z:" + Z);
            e.printStackTrace();
            return null;
        }
        return chunk;
    }

     */

    public static IChunk loadChunk(IChunk chunk, CompoundNBTTag tag, Level level) {


        return chunk;
    }


    public static void setDataProviders(CompoundNBTTag compoundTag, Chunk chunk) {
        //CompoundTag compoundTag1 = (CompoundTag) compoundTag.getTag("data");
        int count = ((IntegerNBTTag)compoundTag.getTag("count")).val;

        for(int i = 0; i < count; i++) {
            CompoundNBTTag compoundTag2 = (CompoundNBTTag) compoundTag.getTag(i + "");
            short key = ((ShortNBTTag)compoundTag2.getTag("pos")).val;
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

    public static void finishSave() {
        new File(pathToWorld + "chunks/").mkdir();
        for(String string : loadedGroups.keySet()) {
            CompoundNBTTag compoundTag = loadedGroups.get(string);
            CompoundNBTTag levelTag = new CompoundNBTTag();
            Level level = levels.get(string);
            level.write(levelTag);
            compoundTag.putTag("level",levelTag);
            WorldLoader.save(compoundTag, pathToWorld + "chunks/" + string + ".dat");
        }
        loadedGroups.clear();
        levels.clear();
    }

    public static void finishSave1() {
        new File(pathToWorld + "chunks/").mkdir();
        for(String string : loadedGroups.keySet()) {
            CompoundNBTTag compoundTag = loadedGroups.get(string);
            CompoundNBTTag levelTag = new CompoundNBTTag();
            Level level = levels.get(string);
            level.write(levelTag);
            compoundTag.putTag("level",levelTag);
            WorldLoader.save(compoundTag, pathToWorld + "chunks/" + string + ".dat");
        }
        loadedGroups.clear();
    }



    public static CompoundNBTTag fetchChunk(int x, int z) {
        int X = x >> 3;
        int Z = z >> 3;
        CompoundNBTTag compoundTag = loadedGroups.get("x" + X + "_z" + Z);
        if(compoundTag != null) {
            return compoundTag.getCompoundTag("x" + x + "_z" + z);
        } else {
            compoundTag = loadTag(X,Z);
            if(compoundTag == null) {
                compoundTag = new CompoundNBTTag();
                loadedGroups.put("x" + X + "_z" + Z,compoundTag);
                return null;
            } else {
                loadedGroups.put("x" + X + "_z" + Z,compoundTag);
                return compoundTag.getCompoundTag("x" + x + "_z" + z);
            }
        }
    }

    public static Level fetchLevel(int x, int z) {
        int X = x >> 3;
        int Z = z >> 3;
        Level level = levels.get("x" + X + "_z" + Z);
        if (level == null) {
            CompoundNBTTag compoundTag = loadTag(X, Z);
            if (compoundTag == null) {
                level = new Level();
            } else {
                level = new Level(compoundTag.getCompoundTag("level"));
            }
            levels.put("x" + X + "_z" + Z, level);
        }
        return level;
    }

    public static void putTag(int x, int z, CompoundNBTTag compoundTag) {
        int X = x >> 3;
        int Z = z >> 3;
        CompoundNBTTag groupTag = loadedGroups.get("x" + X + "_z" + Z);
        if(groupTag == null) {
            groupTag = loadTag(X,Z);
            if(groupTag == null) {
                groupTag = new CompoundNBTTag();
            }
            loadedGroups.put("x" + X + "_z" + Z, groupTag);
        }
        groupTag.putTag("x" + x + "_z" + z,compoundTag);
    }

    public static CompoundNBTTag loadTag(int x, int z) {
        return WorldLoader.loadTag(getPathToChunk(x,z));
    }

    /*
    public static synchronized Chunk readChunk(int x, int z) {
        CompoundNBTTag compoundTag = fetchChunk(x,z);
        if(compoundTag != null) {
            return ChunkLoader.createChunk3(x, z, compoundTag,fetchLevel(x,z));
        }
        return null;
    }

     */

    /*
    public static synchronized void readWriteChunk(Chunk chunk) {
        readChunk(chunk.x,chunk.z);
        CompoundNBTTag compoundTag = ChunkLoader.createTag3(chunk,fetchLevel(chunk.x,chunk.z));
        putTag(chunk.x,chunk.z,compoundTag);
        finishSave1();
    }

     */

    public static void writeChunk(Chunk chunk) {
        CompoundNBTTag compoundTag = ChunkLoader.createTag3(chunk,fetchLevel(chunk.x,chunk.z));
        putTag(chunk.x,chunk.z,compoundTag);
    }


}
