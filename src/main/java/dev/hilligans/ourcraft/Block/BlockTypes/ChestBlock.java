package dev.hilligans.ourcraft.Block.BlockTypes;

import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.Container.Containers.ChestContainer;
import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Data.Other.BlockProperties;
import dev.hilligans.ourcraft.Data.Other.Inventory;
import dev.hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.hilligans.ourcraft.Network.Packet.Server.SOpenContainer;
import dev.hilligans.ourcraft.ServerMain;
import dev.hilligans.ourcraft.World.DataProvider;
import dev.hilligans.ourcraft.World.DataProviders.ChestDataProvider;
import dev.hilligans.ourcraft.World.World;

public class ChestBlock extends Block {


    public ChestBlock(String name, BlockProperties blockProperties) {
        super(name, blockProperties);
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
            ChestContainer container = (ChestContainer) new ChestContainer(chestDataProvider.inventory,playerEntity.getPlayerData().playerInventory).setPlayerId(playerEntity.id);
            playerEntity.getPlayerData().openContainer(container);
            ServerMain.getServer().sendPacket(new SOpenContainer(container), playerEntity);
        }
        return true;
    }

    @Override
    public void onBreak(World world, BlockPos blockPos) {
        super.onBreak(world, blockPos);
        if(world.isServer()) {
            Inventory inventory = ((ChestDataProvider) world.getDataProvider(blockPos)).inventory;
            world.setDataProvider(blockPos, null);
            for (int x = 0; x < inventory.getSize(); x++) {
                world.spawnItemEntity(blockPos.x + 0.5f, blockPos.y + 0.5f, blockPos.z + 0.5f, inventory.getItem(x));
            }
        }
    }

    @Override
    public DataProvider getDataProvider() {
        return new ChestDataProvider();
    }
}
