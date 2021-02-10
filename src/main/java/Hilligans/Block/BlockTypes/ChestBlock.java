package Hilligans.Block.BlockTypes;

import Hilligans.Block.Block;
import Hilligans.Container.Container;
import Hilligans.Container.Containers.ChestContainer;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.Inventory;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.Packet.Server.SOpenContainer;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.World.DataProviders.ChestDataProvider;
import Hilligans.World.World;

public class ChestBlock extends Block {


    public ChestBlock(String name) {
        super(name);
    }

    @Override
    public void onPlace(World world, BlockPos blockPos) {
        super.onPlace(world, blockPos);
        world.setDataProvider(blockPos, new ChestDataProvider());
    }

    @Override
    public boolean activateBlock(World world, PlayerEntity playerEntity, BlockPos pos) {
        if(world.isServer()) {
            ChestDataProvider chestDataProvider = (ChestDataProvider) world.getDataProvider(pos);
            ChestContainer container = new ChestContainer(chestDataProvider.inventory,playerEntity.getPlayerData().playerInventory);
            playerEntity.getPlayerData().openContainer = container;
            ServerNetworkHandler.sendPacket(new SOpenContainer(container), playerEntity);
        }
        return true;
    }
}
