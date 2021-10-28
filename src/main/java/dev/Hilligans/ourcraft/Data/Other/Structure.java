package dev.Hilligans.ourcraft.Data.Other;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.DataBlockState;
import dev.Hilligans.ourcraft.Tag.CompoundNBTTag;
import dev.Hilligans.ourcraft.Tag.IntegerNBTTag;
import dev.Hilligans.ourcraft.Tag.ListNBTTag;
import dev.Hilligans.ourcraft.Tag.ShortNBTTag;
import dev.Hilligans.ourcraft.World.World;
import dev.Hilligans.ourcraft.WorldSave.WorldLoader;

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

    public void placeInWorld(World world, BlockPos pos, int rotation) {
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
        Structure structure = new Structure(compoundTag.getInt("width").val,compoundTag.getInt("height").val,compoundTag.getInt("length").val);
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
                        blockState = block.getDefaultState();
                    }
                    structure.blocks[x][y][z] = blockState;
                    listSpot++;
                }
            }
        }

        return structure;
    }




}
