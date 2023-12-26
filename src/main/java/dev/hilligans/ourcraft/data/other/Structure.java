package dev.hilligans.ourcraft.data.other;

import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.block.Blocks;
import dev.hilligans.ourcraft.data.other.blockstates.BlockState;
import dev.hilligans.ourcraft.data.other.blockstates.DataBlockState;
import dev.hilligans.ourcraft.tag.CompoundNBTTag;
import dev.hilligans.ourcraft.tag.IntegerNBTTag;
import dev.hilligans.ourcraft.tag.ListNBTTag;
import dev.hilligans.ourcraft.tag.ShortNBTTag;
import dev.hilligans.ourcraft.save.WorldLoader;

public class Structure {

    public int width;
    public int height;
    public int length;


    public BlockState[][][] blocks;

    public Structure(int width, int height, int length) {
        blocks = new BlockState[width][height][length];
        this.width = width;
        this.height = height;
        this.length = length;
    }


    public CompoundNBTTag toTag() {
        CompoundNBTTag compoundTag = new CompoundNBTTag();
        compoundTag.putTag("width", new IntegerNBTTag(width));
        compoundTag.putTag("height", new IntegerNBTTag(height));
        compoundTag.putTag("length", new IntegerNBTTag(length));
        ListNBTTag<ShortNBTTag> blocks = new ListNBTTag<>();
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                for(int z = 0; z < length; z++) {
                    BlockState blockState = this.blocks[x][y][z];
                    if(blockState.getBlock().hasBlockState()) {
                        blocks.tags.add(new ShortNBTTag(blockState.getBlock().id));
                        blocks.tags.add(new ShortNBTTag(((DataBlockState)blockState).readData()));
                    } else {
                        blocks.tags.add(new ShortNBTTag(blockState.getBlock().id));
                    }
                }
            }
        }
        return compoundTag;
    }

    public static Structure fromPath(String path) {
        CompoundNBTTag compoundTag = WorldLoader.loadTag(path);
        Structure structure = new Structure(compoundTag.getInt("width"),compoundTag.getInt("height"),compoundTag.getInt("length"));
        ListNBTTag<ShortNBTTag> blocks = (ListNBTTag<ShortNBTTag>) compoundTag.getTag("blocks");
        int listSpot = 0;
        for(int x = 0; x < structure.width; x++) {
            for(int y = 0; y < structure.height; y++) {
                for(int z = 0; z < structure.length; z++) {
                    Block block = Blocks.getBlockWithID(blocks.tags.get(listSpot).val);
                    BlockState blockState;
                    if (block.hasBlockState()) {
                        listSpot++;
                        blockState = block.getStateWithData(blocks.tags.get(listSpot).val);
                    } else {
                        blockState = null;
                       // blockState = block.getDefaultState();
                    }
                    structure.blocks[x][y][z] = blockState;
                    listSpot++;
                }
            }
        }

        return structure;
    }




}
