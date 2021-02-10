package Hilligans.Block.BlockTypes;

import Hilligans.Block.Block;
import Hilligans.Container.Container;
import Hilligans.Container.Containers.ChestContainer;
import Hilligans.Data.Other.Inventory;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.Packet.Server.SOpenContainer;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.World.World;

public class ChestBlock extends Block {

    Inventory inventory = new Inventory(27);

    public ChestBlock(String name) {
        super(name);
    }

    @Override
    public boolean activateBlock(World world, PlayerEntity playerEntity) {
        if(world.isServer()) {
            ChestContainer container = new ChestContainer(inventory,playerEntity.getPlayerData().playerInventory);
            playerEntity.getPlayerData().openContainer = container;
            ServerNetworkHandler.sendPacket(new SOpenContainer(container), playerEntity);
        }
        return true;
    }
}
