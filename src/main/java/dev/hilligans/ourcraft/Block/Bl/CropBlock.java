package dev.hilligans.ourcraft.Block.Bl;

import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.Block.BlockState.BlockStateBuilder;
import dev.hilligans.ourcraft.Block.BlockState.IBlockState;
import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Data.Other.BlockProperties;
import dev.hilligans.ourcraft.World.NewWorldSystem.IMethodResult;
import dev.hilligans.ourcraft.World.NewWorldSystem.IWorld;
import dev.hilligans.ourcraft.Block.BlockState.BlockStateTypes;
import dev.hilligans.ourcraft.Block.BlockState.IntegerStateType;

import java.util.ArrayList;
import java.util.Random;

public class CropBlock extends Block {

    public static IntegerStateType stages = BlockStateTypes.growthStages;

    public CropBlock(String name, BlockProperties blockProperties) {
        super(name, blockProperties);
    }

    @Override
    public void registerBlockStates(BlockStateBuilder builder) {
        builder.register(stages);
    }

    @Override
    public void addMethodKeys(ArrayList<String> keys) {
        super.addMethodKeys(keys);
        keys.add("randomTick:run");
        keys.add("randomTick:async");
        keys.add("randomTick:update");
    }

    @Override
    public IBlockState randomTick(IMethodResult result, IBlockState state, IWorld world, BlockPos pos, Random random) {
        int value = (int) state.getValue(stages);
        if(value == stages.count) {
            return null;
        }
        //TODO add configurable crop growth speed
        if(random.nextInt(10) == 0) {
            return state.getNewState(stages, value + 1);
        }
        return null;
    }
}
