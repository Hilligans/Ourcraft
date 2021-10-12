package dev.Hilligans.ourcraft.Data.Other;

import dev.Hilligans.ourcraft.Block.Block;
import org.joml.Vector3f;

public class RayResult {

    public Vector3f pos;
    BlockPos blockPos;
    public int side;

    public RayResult(Vector3f pos, BlockPos blockPos, int side) {
        this.pos = pos;
        this.blockPos = blockPos;
        this.side = side;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public BlockPos getBlockPosWidthSide() {
        return blockPos.copy().add(Block.getBlockPos(side));
    }


}
