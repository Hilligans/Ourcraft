package dev.hilligans.ourcraft.world;

import dev.hilligans.ourcraft.data.other.blockstates.BlockState;
import dev.hilligans.ourcraft.block.Blocks;
import dev.hilligans.ourcraft.client.rendering.world.managers.VAOManager;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.other.blockstates.DataBlockState;
import dev.hilligans.ourcraft.world.data.providers.ShortBlockState;

import java.util.Arrays;

public class SubChunk {

    public int id = -2;
    World world;
    public long y;
    public long x;
    public long z;

    public boolean empty = true;

    public int[] vals;

    public SubChunk(World world, long X, long Y, long Z) {
        this.world = world;
        this.y = Y;
        this.x = X;
        this.z = Z;
    }

    public void fill() {
        vals = new int[16*16*16];
        for(int x = 0; x < 16; x++) {
            for(int i = 0; i < 16; i++) {
                for(int z = 0; z < 16; z++) {
                    setBlockState(x,i,z, Blocks.AIR.getDefaultState());
                }
            }
        }
    }

    public void destroy() {
        if(id != -1 && id != -2 && id != -3) {
            VAOManager.destroyBuffer(id);
            id = -1;
        }
        world.getChunk((int)x >> 4,(int)z >> 4).destroyMap(-1);
    }

    public BlockState getBlock(BlockPos pos) {
        if(pos.isSubChunkValid()) {
            return getBlock(pos.x,pos.y,pos.z);
        }
        pos.y += y;
        pos.x += x;
        pos.z += z;

        return world.getBlockState(pos);
    }

    public BlockState getBlock(int pos) {
        return getBlock(pos & 0xF,pos & 0x0F,pos & 0x00F);
    }

    public BlockState getBlock(int x, int y, int z) {
        if(empty) {
            return Blocks.AIR.getDefaultState();
        }
        if((vals[x | y << 4 | z << 8] & 65535) == 65535) {
            return new BlockState((short) (vals[x | y << 4 | z << 8] >> 16));
        } else {
            return new DataBlockState((short) (vals[x | y << 4 | z << 8] >> 16),new ShortBlockState((short) (vals[x | y << 4 | z << 8] & 65535)));
        }

       // return blocks[x][y][z];
    }


    public void setBlockState(int x, int y, int z, BlockState blockState) {
       if(!blockState.getBlock().blockProperties.airBlock) {
           getBlock(x & 15, y & 15, z & 15).getBlock().onBreak(world,new BlockPos(x,y,z));
           if (empty) {
               empty = false;
               fill();
           }
       }
       if(!empty) {
           int i = (x & 15) | (y & 15) << 4 | (z & 15) << 8;
           if (blockState instanceof DataBlockState) {
               vals[i] = blockState.blockId << 16 | ((DataBlockState) blockState).blockData.write();
           } else {
               vals[i] = blockState.blockId << 16 | 65535;
           }
       }
    }

    public void set(int block) {
        for(int x = 0; x < 4096; x++) {
            vals[x] = block;
        }
    }

    @Override
    public String toString() {
        return "SubChunk{" +
                "y=" + y +
                ", x=" + x +
                ", z=" + z +
                ", vals=" + Arrays.toString(vals) +
                '}';
    }
}
