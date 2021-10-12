package dev.Hilligans.Ourcraft.Data.Other;

import dev.Hilligans.Ourcraft.Block.Block;
import dev.Hilligans.Ourcraft.Block.Blocks;
import dev.Hilligans.Ourcraft.Data.Other.BlockStates.BlockState;
import dev.Hilligans.Ourcraft.Data.Other.BlockStates.DataBlockState;
import dev.Hilligans.Ourcraft.Tag.CompoundTag;
import dev.Hilligans.Ourcraft.Tag.IntegerTag;
import dev.Hilligans.Ourcraft.Tag.ListTag;
import dev.Hilligans.Ourcraft.Tag.ShortTag;
import dev.Hilligans.Ourcraft.World.World;
import dev.Hilligans.Ourcraft.WorldSave.WorldLoader;

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


    public CompoundTag toTag() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putTag("width", new IntegerTag(width));
        compoundTag.putTag("height", new IntegerTag(height));
        compoundTag.putTag("length", new IntegerTag(length));
        ListTag<ShortTag> blocks = new ListTag<>();
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                for(int z = 0; z < length; z++) {
                    BlockState blockState = this.blocks[x][y][z];
                    if(blockState.getBlock().hasBlockState()) {
                        blocks.tags.add(new ShortTag(blockState.getBlock().id));
                        blocks.tags.add(new ShortTag(((DataBlockState)blockState).readData()));
                    } else {
                        blocks.tags.add(new ShortTag(blockState.getBlock().id));
                    }
                }
            }
        }
        return compoundTag;
    }

    public static Structure fromPath(String path) {
        CompoundTag compoundTag = WorldLoader.loadTag(path);
        Structure structure = new Structure(compoundTag.getInt("width").val,compoundTag.getInt("height").val,compoundTag.getInt("length").val);
        ListTag<ShortTag> blocks = (ListTag<ShortTag>) compoundTag.getTag("blocks");
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
