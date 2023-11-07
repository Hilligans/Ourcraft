package dev.hilligans.ourcraft.data.other;

import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.block.Blocks;
import dev.hilligans.ourcraft.world.World;

import java.util.Random;

public class BlockTemplate {

    public BlockPos[] positions;

    public BlockTemplate(BlockPos... positions) {
        this.positions = positions;
    }

    public void placeTemplate(World world, BlockPos startPos, Block block) {
        for(BlockPos blockPos : positions) {
            world.setBlockState(startPos.x + blockPos.x, startPos.y + blockPos.y, startPos.z + blockPos.z,block.getDefaultState());
        }
    }

    public void placeTemplateOnAir(World world, BlockPos startPos, Block block) {
        for(BlockPos blockPos : positions) {
            BlockPos pos = new BlockPos(startPos.x + blockPos.x, startPos.y + blockPos.y, startPos.z + blockPos.z);
            if(world.getBlockState(pos).getBlock() == Blocks.AIR) {
                world.setBlockState(pos, block.getDefaultState());
            }
        }
    }

    public void placeTemplateOnAirChanced(World world, BlockPos startPos, Block block, Random random, float chance, int min) {
        for(BlockPos blockPos : positions) {
            BlockPos pos = new BlockPos(startPos.x + blockPos.x, startPos.y + blockPos.y, startPos.z + blockPos.z);
            if(random.nextFloat() * chance >= min) {
                if (world.getBlockState(pos).getBlock() == Blocks.AIR) {
                    world.setBlockState(pos, block.getDefaultState());
                }
            }
        }
    }

    public void placeTemplateOnAir(World world, BlockPos startPos, BlockPos worldPos, Block block, double ang, double cos, double sin) {
        for(BlockPos blockPos : positions) {
            BlockPos pos = blockPos.copy().rotateZ(Math.cos(ang),Math.sin(ang)).add(startPos.rotateY(cos,sin)).add(worldPos);
            if(world.getBlockState(pos).getBlock() == Blocks.AIR) {
                world.setBlockState(pos, block.getDefaultState());
            }
        }
    }
}
