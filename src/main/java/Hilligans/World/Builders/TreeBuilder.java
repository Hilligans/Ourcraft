package Hilligans.World.Builders;

import Hilligans.Blocks.Block;
import Hilligans.Blocks.Blocks;
import Hilligans.Client.Camera;
import Hilligans.World.BlockPos;
import Hilligans.World.World;

public class TreeBuilder extends WorldBuilder {


    public TreeBuilder(World world) {
        super(world);
    }

    @Override
    public void build(BlockPos startPos) {
        if(world.getBlockState(startPos.copy().add(0,-1,0)).block == Blocks.GRASS) {
            int height = (int) (Math.random() * 10) + 5;
            for (int y = 0; y < height; y++) {
                BlockPos pos = startPos.copy().add(0, y, 0);
                world.setBlockState(pos, Blocks.LOG.getDefaultState());
                if (y > 5) {
                    if (Math.random() > 0.6 && y < height - 1) {
                        placeBranch(pos);
                    }
                    placeLeaves(pos);
                }

            }
            world.setBlockState(startPos.copy().add(0,-1,0),Blocks.DIRT.getDefaultState());
        }
    }

    private void placeBranch(BlockPos pos) {
        double pitch = Math.random() * 180;
        double yaw = Math.random() * 180;

        int length = (int) (Math.random() * 6) + 4;

        for(int i = 0; i < length; i++) {
            int x = (int)(Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * i);
            int y = (int)(Math.sin(Math.toRadians(pitch)) * i);
            int z = (int)(Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * i);
            world.setBlockState(pos.copy().add(x,y,z),Blocks.LOG.getDefaultState());
            placeLeaves(pos.copy().add(x,y,z));
        }
    }

    private void placeLeaves(BlockPos pos) {
        for(int x = 0; x < 6; x++) {
            BlockPos newPos = pos.copy().add(Block.getBlockPos(x));
            if(world.getBlockState(newPos).block == Blocks.AIR) {
                world.setBlockState(newPos,Blocks.LEAVES.getDefaultState());
            }
        }
    }
}
