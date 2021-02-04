package Hilligans.World;

import Hilligans.Blocks.Block;
import Hilligans.Blocks.Blocks;
import Hilligans.Entity.Entities.ItemEntity;
import Hilligans.Entity.Entity;
import Hilligans.Network.Packet.Server.SCreateEntityPacket;
import Hilligans.Network.Packet.Server.SRemoveEntityPacket;
import Hilligans.Network.ServerNetworkHandler;

public class ServerWorld extends World {

    @Override
    public void addEntity(Entity entity) {
        entities.put(entity.id,entity);
        ServerNetworkHandler.sendPacket(new SCreateEntityPacket(entity));
    }

    @Override
    public void removeEntity(int id) {
        entities.remove(id);
        ServerNetworkHandler.sendPacket(new SRemoveEntityPacket(id));
    }

    @Override
    public void tick() {
        int x = 0;
        //System.out.println("TICKING");
        for(Entity entity : entities.values()) {
            try {
                entity.tick();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //System.out.println(x);
            x++;
        }
    }

    public void createItemEntity(BlockPos blockPos) {
        Block block = getBlockState(blockPos).block;
        if(block != Blocks.AIR) {
            ItemEntity itemEntity = new ItemEntity(blockPos.x,blockPos.y,blockPos.z,Entity.getNewId(),block);
            addEntity(itemEntity);
        }
    }
}
