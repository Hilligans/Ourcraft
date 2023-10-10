package dev.hilligans.ourcraft.World.NewWorldSystem;

import dev.hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.hilligans.ourcraft.Block.Blocks;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.Ourcraft;

public class GlobalPaletteImpl implements ISubChunk {

    public short[] blockStates;

    public byte width;
    public byte height;

    public GlobalPaletteImpl(int width, int height) {
        this.width = (byte) width;
        this.height = (byte) height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public IBlockState getBlockState(int x, int y, int z) {
        if(blockStates == null) {
            return Blocks.AIR.getDefaultState1();
        }
        try {
            short b = blockStates[((x * width + y) * height + z)];
            return b == 0 ? Blocks.AIR.getDefaultState1() : Ourcraft.GAME_INSTANCE.BLOCK_STATES.get(b);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return Blocks.AIR.getDefaultState1();
    }

    @Override
    public IBlockState setBlockState(int x, int y, int z, IBlockState blockState) {
        if(blockStates == null) {
            if(blockState.getBlock() != Blocks.AIR) {
                blockStates = new short[width * height * width];
            } else {
                return blockState;
            }
        }
        try {
            return Ourcraft.GAME_INSTANCE.BLOCK_STATES.get(blockStates[(int) ((x * width + y) * height + z)] = (short) blockState.getBlockStateID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return blockStates == null;
    }
}
