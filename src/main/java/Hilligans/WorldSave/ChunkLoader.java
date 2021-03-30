package Hilligans.WorldSave;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Data.Other.BlockState;
import Hilligans.Data.Other.DataBlockState;
import Hilligans.Data.Primitives.DoubleTypeWrapper;
import Hilligans.Tag.*;
import Hilligans.Util.Settings;
import Hilligans.World.Chunk;
import Hilligans.World.DataProvider;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.HashMap;

public class ChunkLoader {

    public static String pathToWorld = "world/" + Settings.worldName + "/dim-0/";
    public static String getPathToChunk(int x, int z) {
        return pathToWorld + "x" + x + "_z" + z + ".dat";
    }

    public static HashMap<String, CompoundTag> loadedGroups = new HashMap<>();

    public static final Short2ObjectOpenHashMap<String> blockMap = new Short2ObjectOpenHashMap<>();


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

    public static CompoundTag createTag3(Chunk chunk) {
        CompoundTag compoundTag = new CompoundTag();

        ListTag<ShortTag> blocks = new ListTag<>();

        ArrayList<DoubleTypeWrapper<BlockState,Integer>> blockList = chunk.getBlockChainedList();

        for(DoubleTypeWrapper<BlockState,Integer> block : blockList) {
            blocks.tags.add(new ShortTag(block.getTypeA().blockId));
            if(block.getTypeA().getBlock().hasBlockState()) {
                blocks.tags.add(new ShortTag(((DataBlockState)block.getTypeA()).readData()));
            }
            int val = block.getTypeB();
            blocks.tags.add(new ShortTag((short)val));
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

    public static Chunk createChunk3(int X, int Z, CompoundTag compoundTag) {
        Chunk chunk = new Chunk(X, Z, null);
        try {
            ListTag<ShortTag> blocks = (ListTag<ShortTag>) compoundTag.getTag("blocks");
            ArrayList<DoubleTypeWrapper<BlockState,Integer>> blockList = new ArrayList<>();

            int offset = 0;

            while(offset < blocks.tags.size()) {
                Block block = Blocks.getBlockWithID(blocks.tags.get(offset).val);
                offset++;
                BlockState blockState;
                if(block.hasBlockState()) {
                    blockState = block.getStateWithData(blocks.tags.get(offset).val);
                    offset++;
                } else {
                    blockState = block.getDefaultState();
                }
                blockList.add(new DoubleTypeWrapper<>(blockState,blocks.tags.get(offset).val & 0xFFFF));
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

    public static void finishSave() {
        for(String string : loadedGroups.keySet()) {
            CompoundTag compoundTag = loadedGroups.get(string);
            WorldLoader.save(compoundTag, pathToWorld + string + ".dat");
        }
        loadedGroups.clear();
    }

    public static CompoundTag fetchChunk(int x, int z) {
        int X = x >> 3;
        int Z = z >> 3;
        CompoundTag compoundTag = loadedGroups.get("x" + X + "_z" + Z);
        if(compoundTag != null) {
            return compoundTag.getCompoundTag("x" + x + "_z" + z);
        } else {
            compoundTag = loadTag(X,Z);
            if(compoundTag == null) {
                compoundTag = new CompoundTag();
                loadedGroups.put("x" + X + "_z" + Z,compoundTag);
                return null;
            } else {
                loadedGroups.put("x" + X + "_z" + Z,compoundTag);
                return compoundTag.getCompoundTag("x" + x + "_z" + z);
            }
        }
    }

    public static void putTag(int x, int z, CompoundTag compoundTag) {
        int X = x >> 3;
        int Z = z >> 3;
        CompoundTag groupTag = loadedGroups.get("x" + X + "_z" + Z);
        if(groupTag == null) {
            groupTag = loadTag(X,Z);
            if(groupTag == null) {
                groupTag = new CompoundTag();
            }
            loadedGroups.put("x" + X + "_z" + Z, groupTag);
        }
        groupTag.putTag("x" + x + "_z" + z,compoundTag);
    }

    public static CompoundTag loadTag(int x, int z) {
        return WorldLoader.loadTag(getPathToChunk(x,z));
    }

    public static Chunk readChunk(int x, int z) {
        CompoundTag compoundTag = fetchChunk(x,z);
        if(compoundTag != null) {
            return ChunkLoader.createChunk3(x, z, compoundTag);
        }
        return null;
    }

    public static void writeChunk(Chunk chunk) {
        CompoundTag compoundTag = ChunkLoader.createTag3(chunk);
        putTag(chunk.x,chunk.z,compoundTag);
    }


}
