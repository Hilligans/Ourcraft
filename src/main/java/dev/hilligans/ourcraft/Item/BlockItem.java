package dev.hilligans.ourcraft.Item;

import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.Block.Blocks;
import dev.hilligans.ourcraft.Client.Rendering.NewRenderer.PrimitiveBuilder;
import dev.hilligans.ourcraft.Data.Other.BlockStates.BlockState;
import dev.hilligans.ourcraft.Client.Camera;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Data.Other.ItemProperties;
import dev.hilligans.ourcraft.Data.Other.RayResult;
import dev.hilligans.ourcraft.Entity.Entity;
import dev.hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.hilligans.ourcraft.Entity.LivingEntity;
import dev.hilligans.ourcraft.World.World;
import org.joml.Vector3d;
import org.joml.Vector3f;

public class BlockItem extends Item {

    public Block block;

    public BlockItem(String name, Block block, String modID) {
        super(name, new ItemProperties().serverSide(block.blockProperties.serverSide).block(block.getName()),modID);
        this.block = block;
    }

    @Override
    public boolean onActivate(World world, PlayerEntity playerEntity) {
        RayResult rayResult;
        if (world.isServer()) {
            rayResult = world.traceBlock(playerEntity.getX(), playerEntity.getY() + playerEntity.boundingBox.eyeHeight, playerEntity.getZ(), playerEntity.pitch, playerEntity.yaw);
        } else {
            rayResult = world.traceBlock(Camera.pos.x, Camera.pos.y + Camera.playerBoundingBox.eyeHeight, Camera.pos.z, Camera.pitch, Camera.yaw);
        }
        if (rayResult == null || rayResult.side == -1) {
            return false;
        }
        BlockPos pos = rayResult.getBlockPos();
        if(world.getBlockState(pos).getBlock() != Blocks.AIR) {
            pos = rayResult.getBlockPosWidthSide();
            if(world.getBlockState(pos).getBlock() != Blocks.AIR) {
                return false;
            }
        }
        BlockState oldState = world.getBlockState(pos);

        BlockState blockState;
        if(world.isServer()) {
            blockState = block.getStateForPlacement(new Vector3d(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ()),rayResult);
        } else {
            blockState = block.getStateForPlacement(Camera.pos,rayResult);
        }
        world.setBlockState(pos, blockState);

        for (Entity entity : world.entities.values()) {
            if (entity instanceof LivingEntity) {
                if(world.isServer()) {
                    if (!block.getAllowedMovement(new Vector3d(), new Vector3d(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ()), pos, entity.boundingBox, world)) {
                        world.setBlockState(pos,oldState);
                        return false;
                    }
                } else {
                    if (!block.getAllowedMovement(new Vector3d(), Camera.pos, pos, entity.boundingBox, world)) {
                        world.setBlockState(pos,oldState);
                        return false;
                    }
                }
            }
        }

        block.onPlace(world,pos);
        return true;
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, int size, ItemStack itemStack) {
        block.renderItem(matrixStack,x,y,size,itemStack);

        //TODO fix

       // drawString(matrixStack,x - size / 2,y,size,itemStack.count);
    }

    @Override
    public void addData(PrimitiveBuilder primitiveBuilder, float size) {
        for(int z = 0; z < 6; z++) {
            block.addVertices(primitiveBuilder,z,size,block.getDefaultState(),new BlockPos(0,0,0),0,0);
        }
        primitiveBuilder.translate(size / 3f,size / 1.3f,0);
        primitiveBuilder.rotate(0.785f,new Vector3f(0.5f,-1,0));
        primitiveBuilder.rotate(0.186f,new Vector3f(0,0,-1));
        primitiveBuilder.rotate(3.1415f,new Vector3f(0,0,1));
        primitiveBuilder.translate(0,0 ,-size * 2);
    }
}
