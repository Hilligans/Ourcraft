package Hilligans.World.Builders.Foliage;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Util.Ray;
import Hilligans.Data.Other.BlockPos;
import Hilligans.World.Builders.SurfaceBuilder;

public class TreeBuilder extends SurfaceBuilder {

    @Override
    public void build(BlockPos startPos) {
        if(world.getBlockState(startPos.copy().add(0,-1,0)).getBlock() == Blocks.GRASS) {
            int height = random.nextInt(8) + 8;
            for (int y = 0; y < height; y++) {
                BlockPos pos = startPos.copy().add(0, y, 0);
                world.setBlockState(pos, Blocks.LOG.getDefaultState());
                if (y > 5) {
                    if (random.nextInt(100) > 40 && y < height - 1) {
                        startBranch(pos);
                    }
                    placeLeaves(pos);
                }
            }
            world.setBlockState(startPos.copy().add(0,-1,0),Blocks.DIRT.getDefaultState());
        }
    }

    private void startBranch(BlockPos startPos) {
        double pitch = random.nextInt(90) - 45;
        double yaw = random.nextInt(180);

        int rotCount = 90;
        placeBranch(startPos.copy().add(0,random.nextInt(4) - 2, 0),pitch,yaw + rotCount);
        placeBranch(startPos.copy().add(0,random.nextInt(4) - 2, 0),pitch,yaw + rotCount * 2);
        placeBranch(startPos.copy().add(0,random.nextInt(4) - 2, 0),pitch,yaw + rotCount * 3);
        placeBranch(startPos.copy().add(0,random.nextInt(4) - 2, 0),pitch,yaw + rotCount * 4);
    }

    private void placeBranch(BlockPos pos, double pitch, double yaw) {
        int length = (int) (Math.random() * 6) + 4;
        Ray ray = new Ray(pitch,yaw);
        for(int i = 0; i < length; i++) {
            BlockPos newPos = pos.copy().add(ray.getNextBlock(i));
            world.setBlockState(newPos,Blocks.LOG.getDefaultState());
            placeLeaves(newPos);
        }
    }

    private void placeLeaves(BlockPos pos) {
        for(int x = 0; x < 6; x++) {
            BlockPos newPos = pos.copy().add(Block.getBlockPos(x));
            if (world.getBlockState(newPos).getBlock() == Blocks.AIR) {
                world.setBlockState(newPos, Blocks.LEAVES.getDefaultState());
            }
            //if (random.nextInt(3) == 0) {
                for (int y = 0; y < 5; y++) {
                    BlockPos newPos1 = newPos.copy().add(0, -1 * y, 0);
                    if (world.getBlockState(newPos1).getBlock() == Blocks.AIR) {
                        world.setBlockState(newPos1, Blocks.WEEPING_VINE.getDefaultState());
                    }
                }
           // }
        }
    }
}
