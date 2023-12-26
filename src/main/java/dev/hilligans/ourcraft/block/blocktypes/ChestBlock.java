package dev.hilligans.ourcraft.block.blocktypes;

import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.container.containers.ChestContainer;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.other.BlockProperties;
import dev.hilligans.ourcraft.data.other.Inventory;
import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;
import dev.hilligans.ourcraft.network.packet.server.SOpenContainer;
import dev.hilligans.ourcraft.ServerMain;
import dev.hilligans.ourcraft.world.DataProvider;
import dev.hilligans.ourcraft.world.data.providers.ChestDataProvider;
import dev.hilligans.ourcraft.world.newworldsystem.IServerWorld;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;

public class ChestBlock extends Block {


    public ChestBlock(String name, BlockProperties blockProperties) {
        super(name, blockProperties);
    }

    @Override
    public void onPlace(IWorld world, BlockPos blockPos) {
        super.onPlace(world, blockPos);
        //world.setDataProvider(blockPos, new ChestDataProvider());
    }

    @Override
    public boolean activateBlock(IWorld world, PlayerEntity playerEntity, BlockPos pos) {
        if(world instanceof IServerWorld) {
            ChestDataProvider chestDataProvider = null;
            //ChestDataProvider chestDataProvider = (ChestDataProvider) world.getDataProvider(pos);
            ChestContainer container = (ChestContainer) new ChestContainer(chestDataProvider.inventory,playerEntity.getPlayerData().playerInventory).setPlayerId(playerEntity.id);
            playerEntity.getPlayerData().openContainer(container);
            ServerMain.getServer().sendPacket(new SOpenContainer(container), playerEntity);
        }
        return true;
    }

    @Override
    public void onBreak(IWorld world, BlockPos blockPos) {
        super.onBreak(world, blockPos);
        if(world instanceof IServerWorld) {
            /*
            Inventory inventory = ((ChestDataProvider) world.getDataProvider(blockPos)).inventory;
            world.setDataProvider(blockPos, null);
            for (int x = 0; x < inventory.getSize(); x++) {
                world.spawnItemEntity(blockPos.x + 0.5f, blockPos.y + 0.5f, blockPos.z + 0.5f, inventory.getItem(x));
            }

             */
        }
    }

    @Override
    public DataProvider getDataProvider() {
        return new ChestDataProvider();
    }
}
