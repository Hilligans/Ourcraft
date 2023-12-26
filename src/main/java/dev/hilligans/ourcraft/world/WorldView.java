package dev.hilligans.ourcraft.world;

import dev.hilligans.ourcraft.block.blockstate.IBlockState;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;
import org.joml.Vector3f;

/**
 Represents a class to look at a world view the gravity direction normalized to always being -y
 */
public record WorldView(IWorld world, long offsetX, long offsetY, long offsetZ, short rot) {

    public IBlockState getBlockState(int x, int y, int z) {
        return switch (rot) {
            case 0 -> world.getBlockState(offsetX + y, offsetY + z, offsetZ + x);
            case 1 -> world.getBlockState(offsetX - y, offsetY + z, offsetZ + x);
            case 2 -> world.getBlockState(offsetX + x, offsetY + y, offsetZ + z);
            case 3 -> world.getBlockState(offsetX + x, offsetY - y, offsetZ + z);
            case 4 -> world.getBlockState(offsetX + z, offsetY + x, offsetZ + y);
            default ->world.getBlockState(offsetX + z, offsetY + x, offsetZ - y);
        };
    }

    public void setBlockState(IBlockState blockState, int x, int y, int z) {
        switch (rot) {
            case 0:
                world.setBlockState(offsetX + y, offsetY + z, offsetZ + x, blockState);
                break;
            case 1:
                world.setBlockState(offsetX - y, offsetY + z, offsetZ + x, blockState);
                break;
            case 2:
                world.setBlockState(offsetX + x, offsetY + y, offsetZ + z, blockState);
                break;
            case 3:
                world.setBlockState(offsetX + x, offsetY - y, offsetZ + z, blockState);
                break;
            case 4:
                world.setBlockState(offsetX + z, offsetY + x, offsetZ + y, blockState);
                break;
            default:
                world.setBlockState(offsetX + z, offsetY + x, offsetZ - y, blockState);
                break;
        }
    }

    public static WorldView getWorldView(IWorld world, long offsetX, long offsetY, long offsetZ, Vector3f gravVector) {
        float val = gravVector.x;
        if(val > 0) {
            return new WorldView(world, offsetX, offsetY, offsetZ, (short) 0);
        } else if(val < 0) {
            return new WorldView(world, offsetX, offsetY, offsetZ, (short) 1);
        }

        val = gravVector.y;
        if(val > 0) {
            return new WorldView(world, offsetX, offsetY, offsetZ, (short) 2);
        } else if(val < 0) {
            return new WorldView(world, offsetX, offsetY, offsetZ, (short) 3);
        }

        val = gravVector.z;
        if(val > 0) {
            return new WorldView(world, offsetX, offsetY, offsetZ, (short) 4);
        } else if(val < 0) {
            return new WorldView(world, offsetX, offsetY, offsetZ, (short) 5);
        }
        throw new RuntimeException("No Gravity Vector");
    }
}
