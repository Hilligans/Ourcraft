package Hilligans.Block.BlockTypes;

import Hilligans.Block.Block;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Data.Other.BlockProperties;
import Hilligans.Data.Other.BlockState;
import Hilligans.Data.Other.BlockShapes.SlabBlockShape;
import Hilligans.Data.Other.DataBlockState;
import Hilligans.Data.Other.RayResult;
import Hilligans.Item.ItemStack;
import Hilligans.World.DataProviders.ShortBlockState;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class SlabBlock extends Block {
    public SlabBlock(String name, BlockProperties blockProperties) {
        super(name, blockProperties);
        blockProperties.transparent();
        blockProperties.blockShape = new SlabBlockShape();
    }

    @Override
    public BlockState getDefaultState() {
        return new DataBlockState(this, new ShortBlockState((short) 5));
    }

    @Override
    public BlockState getStateForPlacement(Vector3d playerPos, RayResult rayResult) {
        return new DataBlockState(this, new ShortBlockState((short) rayResult.side));
    }

    @Override
    public boolean hasBlockState() {
        return true;
    }

    @Override
    public int blockStateByteCount() {
        return 2;
    }

    @Override
    public BlockState getStateWithData(short data) {
        return new DataBlockState(this, new ShortBlockState(data));
    }

}
