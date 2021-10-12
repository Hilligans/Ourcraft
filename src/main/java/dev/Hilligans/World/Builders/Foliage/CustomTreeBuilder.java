package dev.Hilligans.World.Builders.Foliage;

import dev.Hilligans.Block.Block;
import dev.Hilligans.Block.Blocks;
import dev.Hilligans.Data.Other.BlockPos;
import dev.Hilligans.Data.Other.BlockTemplate;
import dev.Hilligans.Data.Other.JoinedBlockTemplate;
import dev.Hilligans.Util.Ray;
import dev.Hilligans.World.Builders.SurfaceBuilder;
import org.joml.Vector2d;

public class CustomTreeBuilder extends SurfaceBuilder {

    public int baseSize;
    public int height;
    Block wood;
    Block leaves;

    public CustomTreeBuilder(Block block) {
        this.wood = block;
        this.leaves = Blocks.LEAVES;
    }

    @Override
    public void build(BlockPos startPos) {
        if(!isPlacedOn(startPos,Blocks.DIRT) && !isPlacedOn(startPos, Blocks.GRASS)) {
            return;
        }
        height = 30;
        baseSize = 2;
        buildStem(startPos,new Vector2d(),height, wood);
        //buildCircularLeaves(startPos,new Vector2d(),height);
        //buildSphereLeavesVariance(startPos.copy().add(0,height,0),3,4);
        buildDiscLeaves(startPos.copy().add(0,height,0),3,8,leaves);
        buildRoots(startPos,wood);

        for(int x = 0; x < 8; x++) {
            buildDiscLeaves(buildBranch(startPos.copy().add(0, (int) (height / 2 * random.nextFloat() + height / 2),0),15,new Vector2d(random.nextFloat() * 15 + 45,random.nextFloat() * 360),wood),3,8,leaves);
        }
    }

    public void buildStem(BlockPos startPos, Vector2d rotation, int height, Block wood) {
        int heightRatio = height / baseSize;
        for(int x = 0; x < baseSize; x++) {
            BlockTemplate blockTemplate = getTemplate(baseSize - x);
            for(int y = heightRatio * x; y < heightRatio * x + heightRatio; y++) {
                blockTemplate.placeTemplate(world,new BlockPos(startPos.x,y + startPos.y, startPos.z),wood);
            }
        }
    }

    public void buildCircularLeaves(BlockPos startPos, Vector2d rotation, int height, Block leaves) {
        float increase = 5f / height;
        for(int y = 0; y < height; y++) {
            int template = (int) (6 - increase * y);
            if(y % 2 == 1) {
                template--;
                template = Math.max(1,template);
            }
            template += 1;
            BlockTemplate blockTemplate = getTemplate(template);
            blockTemplate.placeTemplateOnAir(world,startPos.copy().add(0,y,0),leaves);
        }
    }

    public void buildDiscLeaves(BlockPos startPos, int size, int discSize, Block leaves) {
        if(size % 2 == 0) {
            for(int x = 0; x < size/2; x++) {
                BlockTemplate blockTemplate = getTemplate(discSize - x * 2);
                blockTemplate.placeTemplateOnAir(world,startPos.copy().add(0,x,0),leaves);
                blockTemplate.placeTemplateOnAir(world,startPos.copy().add(0,-x - 1,0),leaves);
            }
        } else {
            getTemplate(discSize).placeTemplateOnAir(world,startPos,leaves);
            for(int x = 1; x < (int)((size + 2)/2f); x++) {
                BlockTemplate blockTemplate = getTemplate(discSize - x * 2);
                blockTemplate.placeTemplateOnAir(world,startPos.copy().add(0,x,0),leaves);
                blockTemplate.placeTemplateOnAir(world,startPos.copy().add(0,-x,0),leaves);
            }
        }
    }

    public void buildRoots(BlockPos startPos, Block roots) {
        float startOffset = random.nextFloat() * 360;
        for(int x = 0; x < 5; x++) {
            Ray ray = new Ray(-35,startOffset + 75 * x,1.0f);
            for(int y = 0; y < 6; y++) {
                world.setBlockState(startPos.copy().add(ray.getNextBlock(y)).add(0,1,0),roots.getDefaultState());
            }
        }
    }

    public void buildSphereLeaves(BlockPos startPos, int size, Block leaves) {
        for(int x = -size; x < size; x++) {
            for(int y = -size; y < size; y++) {
                for(int z = -size; z < size; z++) {
                    if(x*x+y*y+z*z < size*size) {
                        BlockPos pos = startPos.copy().add(x,y,z);
                        if(world.getBlockState(pos).getBlock() == Blocks.AIR) {
                            world.setBlockState(pos, leaves.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    public void buildSphereLeaves(BlockPos startPos, int size, int randomChance, Block leaves) {
        for(int x = -size; x < size; x++) {
            for(int y = -size; y < size; y++) {
                for(int z = -size; z < size; z++) {
                    if(x*x+y*y+z*z < size*size && random.nextInt(randomChance) == 0) {
                        BlockPos pos = startPos.copy().add(x,y,z);
                        if(world.getBlockState(pos).getBlock() == Blocks.AIR) {
                            world.setBlockState(pos, leaves.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    public void buildSphereLeavesVariance(BlockPos startPos, int variance, int size, Block leaves) {
        for(int x = -size; x < size; x++) {
            for(int y = -size; y < size; y++) {
                for(int z = -size; z < size; z++) {
                    if(x*x+y*y+z*z - random.nextFloat() * variance < size*size) {
                        BlockPos pos = startPos.copy().add(x,y,z);
                        if(world.getBlockState(pos).getBlock() == Blocks.AIR) {
                            world.setBlockState(pos, leaves.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    public void buildSphereLeavesVariance(BlockPos startPos, int size, int variance, int randomChance, Block leaves) {
        for(int x = -size; x < size; x++) {
            for(int y = -size; y < size; y++) {
                for(int z = -size; z < size; z++) {
                    if(x*x+y*y+z*z - random.nextFloat() * variance < size*size && random.nextInt(randomChance) == 0) {
                        BlockPos pos = startPos.copy().add(x,y,z);
                        if(world.getBlockState(pos).getBlock() == Blocks.AIR) {
                            world.setBlockState(pos, leaves.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    public BlockPos buildBranch(BlockPos startPos, int length, Vector2d rotation, Block wood) {
        Ray ray = new Ray(rotation.x,rotation.y,1f);
        for(int x = 0; x < length; x++) {
            world.setBlockMatches(ray.getNextBlock(x).add(startPos),wood.getDefaultState(),Blocks.AIR);
        }
        return ray.getNextBlock(length).add(startPos);
    }

    public static BlockTemplate getTemplate(int val) {
        switch (val) {
            case 1:
                return STEM_0;
            case 2:
                return STEM_1;
            case 3:
                return STEM_2;
            case 4:
                return STEM_3;
            case 5:
                return STEM_4;
            case 6:
                return STEM_5;
            case 7:
                return STEM_6;
            default:
                return STEM_7;
        }
    }


    public static final BlockTemplate STEM_0 = new BlockTemplate(new BlockPos(0,0,0));
    public static final BlockTemplate STEM_1 = new JoinedBlockTemplate(STEM_0,new BlockTemplate(new BlockPos(1,0,0),new BlockPos(-1,0,0),new BlockPos(0,0,1),new BlockPos(0,0,-1)));
    public static final BlockTemplate STEM_2 = new JoinedBlockTemplate(STEM_1,new BlockTemplate(new BlockPos(2,0,0),new BlockPos(-2,0,0),new BlockPos(0,0,2),new BlockPos(0,0,-2),new BlockPos(1,0,1),new BlockPos(-1,0,1),new BlockPos(1,0,-1),new BlockPos(-1,0,-1)));
    public static final BlockTemplate STEM_3 = new JoinedBlockTemplate(STEM_2,new BlockTemplate(new BlockPos(2,0,1),new BlockPos(-2,0,1),new BlockPos(2,0,-1),new BlockPos(-2,0,-1),new BlockPos(1,0,2),new BlockPos(-1,0,2),new BlockPos(1,0,-2),new BlockPos(-1,0,-2)));
    public static final BlockTemplate STEM_4 = new JoinedBlockTemplate(STEM_3,new BlockTemplate(new BlockPos(3,0,0),new BlockPos(3,0,0),new BlockPos(-3,0,0),new BlockPos(0,0,3),new BlockPos(0,0,-3),new BlockPos(2,0,2),new BlockPos(-2,0,2),new BlockPos(2,0,-2),new BlockPos(-2,0,-2)));
    public static final BlockTemplate STEM_5 = new JoinedBlockTemplate(STEM_4,new BlockTemplate(new BlockPos(3,0,1),new BlockPos(-3,0,1),new BlockPos(3,0,-1),new BlockPos(-3,0,-1),new BlockPos(1,0,3),new BlockPos(-1,0,3),new BlockPos(1,0,-3),new BlockPos(-1,0,-3)));
    public static final BlockTemplate STEM_6 = new JoinedBlockTemplate(STEM_5,new BlockTemplate(new BlockPos(3,0,2),new BlockPos(-3,0,2),new BlockPos(3,0,-2),new BlockPos(-3,0,-2),new BlockPos(2,0,3),new BlockPos(-2,0,3),new BlockPos(2,0,-3),new BlockPos(-2,0,-3),new BlockPos(4,0,0),new BlockPos(-4,0,0),new BlockPos(0,0,4),new BlockPos(0,0,-4)));
    public static final BlockTemplate STEM_7 = new JoinedBlockTemplate(STEM_6,new BlockTemplate(new BlockPos(4,0,1),new BlockPos(4,0,-1),new BlockPos(-4,0,1),new BlockPos(-4,0,-1),new BlockPos(1,0,4),new BlockPos(-1,0,4),new BlockPos(1,0,-4),new BlockPos(-1,0,-4)));
}
