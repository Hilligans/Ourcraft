package Hilligans.Item;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Data.Other.BlockState;
import Hilligans.Client.Camera;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.ItemProperties;
import Hilligans.Data.Other.RayResult;
import Hilligans.Entity.Entity;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Entity.LivingEntity;
import Hilligans.Network.Packet.Server.SSendBlockChanges;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.World.World;
import org.joml.Vector3f;

public class BlockItem extends Item {

    public Block block;

    public BlockItem(String name, Block block) {
        super(name, new ItemProperties().serverSide(block.blockProperties.serverSide));
        this.block = block;
    }

    @Override
    public boolean onActivate(World world, PlayerEntity playerEntity) {
        RayResult rayResult;
        if (world.isServer()) {
            rayResult = world.traceBlock(playerEntity.x, playerEntity.y + playerEntity.boundingBox.eyeHeight, playerEntity.z, playerEntity.pitch, playerEntity.yaw);
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
            blockState = block.getStateForPlacement(new Vector3f(playerEntity.x, playerEntity.y, playerEntity.z),rayResult);
        } else {
            blockState = block.getStateForPlacement(Camera.pos,rayResult);
        }
        world.setBlockState(pos, blockState);

        for (Entity entity : world.entities.values()) {
            if (entity instanceof LivingEntity) {
                if(world.isServer()) {
                    if (!block.getAllowedMovement(new Vector3f(), new Vector3f(playerEntity.x, playerEntity.y, playerEntity.z), pos, entity.boundingBox, world)) {
                        world.setBlockState(pos,oldState);
                        return false;
                    }
                } else {
                    if (!block.getAllowedMovement(new Vector3f(), Camera.pos, pos, entity.boundingBox, world)) {
                        world.setBlockState(pos,oldState);
                        return false;
                    }
                }
            }
        }

        block.onPlace(world,pos);
        if (world.isServer()) {
           // ServerNetworkHandler.sendPacket(new SSendBlockChanges(pos,blockState));
        }
        return true;
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, int size, ItemStack itemStack) {
        block.renderItem(matrixStack,x,y,size,itemStack);
        drawString(matrixStack,x - size / 2,y,size,itemStack.count);
    }
}
