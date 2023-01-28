package dev.hilligans.ourcraft.World.Builders.Foliage;

import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.Block.Blocks;
import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Util.Ray;
import dev.hilligans.ourcraft.World.Builders.SurfaceBuilder;

public class LargeTreeBuilder extends SurfaceBuilder {


    public LargeTreeBuilder(String featureName) {
        super(featureName);
    }

    @Override
    public void build(BlockPos startPos) {

        if(isPlacedOn(startPos, Blocks.GRASS)) {
            float height = random.nextInt(30) + 80;
            int girth = random.nextInt(4) + 3;
            for (int y = 0; y < height; y++) {
                int width = Math.round((height - y) / height * girth) + 1;
                for(int yaw = 0; yaw < 360; yaw += 8) {
                    Ray ray = new Ray(0,yaw,1.0f);
                    for(int x = 0; x < width; x++) {
                        world.setBlockState(startPos.copy().add(ray.getNextBlock(x)).add(0,y,0),Blocks.LOG.getDefaultState());
                    }

                    if(girth > width) {
                        for(int x = 0; x < width * 2; x++) {
                            placeLeaves(startPos.copy().add(ray.getNextBlock(x)).add(0,y,0));
                        }
                    }
                }

               // if(random.nextInt(10) == 0) {
                    //startBranch(startPos.copy().add(0,y,0),width);
             //   }

            }


           // int length = random.nextInt(30) + 80;
           // int girth = random.nextInt(4) + 3;
           // drawLargeTrunk(startPos,length,girth,90,0);
            world.setBlockState(startPos.copy().add(0,-1,0),Blocks.DIRT.getDefaultState());
        }
    }

    private void drawLargeTrunk(BlockPos startPos, int length, int girth, double startPitch, double startYaw) {
        Ray startRay = new Ray(startPitch,startYaw,1.0f);
        for (int y = 0; y < length; y++) {
            int width = Math.round(((float)length - y) / length * girth) + 1;
            for(int yaw = 0; yaw < 360; yaw += 360f / girth / 4) {
                Ray ray = new Ray(yaw,0,1.0f);
                for(int x = 0; x < width; x++) {
                    world.setBlockState(startPos.copy().add(ray.getNextBlock(x)).add(startRay.getNextBlock(y)),Blocks.LOG.getDefaultState());
                }
            }
        }
    }

    private final int rot = 4;

    private void startBranch(BlockPos startPos, int girth) {
        double pitch = random.nextInt(90) - 45;
        double yaw = random.nextInt(360);
        int length = (int) (Math.random() * 12) + 8;

        //int rotCount = 360 / rot;

       // for(int x = 0; x < rot; x++) {
      //      drawLargeTrunk(startPos.add(0,random.nextInt(8) - 4,0),length,4,pitch,yaw + rotCount * x);
      //  }

        drawLargeTrunk(startPos.add(0,random.nextInt(8) - 4,0),length,girth,pitch,yaw);

       // placeBranch(startPos.copy().add(0,(int)(Math.random() * 4 - 2), 0),pitch,yaw + rotCount);
        //placeBranch(startPos.copy().add(0,(int)(Math.random() * 4 - 2), 0),pitch,yaw + rotCount * 2);
        //placeBranch(startPos.copy().add(0,(int)(Math.random() * 4 - 2), 0),pitch,yaw + rotCount * 3);
        //placeBranch(startPos.copy().add(0,(int)(Math.random() * 4 - 2), 0),pitch,yaw + rotCount * 4);
    }

    private void placeBranch(BlockPos pos, double pitch, double yaw) {
        int length = (int) (Math.random() * 12) + 8;
        Ray ray = new Ray(pitch,yaw,1.0f);
        for(int i = 0; i < length; i++) {
            BlockPos newPos = pos.copy().add(ray.getNextBlock(i));
            world.setBlockState(newPos,Blocks.LOG.getDefaultState());
            placeLeaves(newPos);
        }
    }

    private void placeLeaves(BlockPos pos) {
        for(int x = 0; x < 6; x++) {
            BlockPos newPos = pos.copy().add(Block.getBlockPos(x));
            if(world.getBlockState(newPos).getBlock() == Blocks.AIR) {
                world.setBlockState(newPos,Blocks.LEAVES.getDefaultState());
            }
        }
    }
}
