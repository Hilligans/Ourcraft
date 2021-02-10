package Hilligans.Item;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Client.Camera;
import Hilligans.Client.MatrixStack;
import Hilligans.Client.Rendering.Renderer;
import Hilligans.Data.Other.BlockPos;
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
        super(name);
        this.block = block;
    }

    @Override
    public boolean onActivate(World world, PlayerEntity playerEntity) {
        BlockPos pos;
        if (world.isServer()) {
            pos = world.traceBlock(playerEntity.x, playerEntity.y, playerEntity.z, playerEntity.pitch, playerEntity.yaw);
        } else {
            pos = world.traceBlock(Camera.pos.x, Camera.pos.y, Camera.pos.z, Camera.pitch, Camera.yaw);
        }
        if (pos == null) {
            return false;
        }
        for (Entity entity : world.entities.values()) {
            if (entity instanceof LivingEntity) {
                if(world.isServer()) {
                    if (!block.getAllowedMovement1(new Vector3f(), new Vector3f(playerEntity.x, playerEntity.y, playerEntity.z), pos, entity.boundingBox)) {
                        return false;
                    }
                } else {
                    if (!block.getAllowedMovement1(new Vector3f(), Camera.pos, pos, entity.boundingBox)) {
                        return false;
                    }
                }
            }
        }
        world.setBlockState(pos, block.getDefaultState());
        if (world.isServer()) {
            ServerNetworkHandler.sendPacket(new SSendBlockChanges(pos,block.id));
        }
        return true;
    }

    @Override
    public void render(MatrixStack matrixStack, int x, int y, int size, ItemStack itemStack) {
        Renderer.renderBlockItem(matrixStack,x,y,size,block);
        drawString(matrixStack,x - size / 2,y,size,itemStack.count);
    }
}
