package Hilligans.Block.BlockTypes;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.BlockProperties;
import Hilligans.Data.Other.BlockTemplate;
import Hilligans.World.Builders.Foliage.CustomTreeBuilder;
import Hilligans.World.World;
import org.joml.Vector2d;

public class OakSapling extends Block {
    public OakSapling(String name, BlockProperties blockProperties) {
        super(name, blockProperties);
    }

    CustomTreeBuilder customTreeBuilder = new CustomTreeBuilder(Blocks.WILLOW_LOG) {
        @Override
        public void build (BlockPos startPos){
            if (!isPlacedOn(startPos, Blocks.DIRT) && !isPlacedOn(startPos, Blocks.GRASS)) {
                return;
            }
            this.height = 15;
            baseSize = 2;
            buildStem(startPos, new Vector2d(), height, Blocks.WILLOW_LOG);
            buildSphereLeaves(startPos.copy().add(0, height, 0), 5, 3, Blocks.LEAVES);
            buildRoots(startPos, Blocks.WILLOW_LOG);

            for (int x = 0; x < 8; x++) {
                buildSphereLeaves(buildBranch(startPos.copy().add(0, (int) (height / 2 * random.nextFloat() + height / 2), 0), 8, new Vector2d(random.nextFloat() * 75, random.nextFloat() * 360), Blocks.WILLOW_LOG), 5, 3, Blocks.LEAVES);
            }
        }
    };

    @Override
    public void onPlace(World world, BlockPos blockPos) {
        customTreeBuilder.setWorld(world).build(blockPos);
    }
}
