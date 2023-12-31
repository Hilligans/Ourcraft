package dev.hilligans.ourcraft.block.bl;

import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.block.blockstate.BlockStateBuilder;
import dev.hilligans.ourcraft.block.blockstate.BlockStateTypes;
import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.block.blockstate.IntegerStateType;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.other.BlockProperties;
import dev.hilligans.ourcraft.world.newworldsystem.IMethodResult;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;

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
