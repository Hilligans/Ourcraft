package Hilligans.Block.BlockTypes;

import Hilligans.Container.Containers.ChestContainer;
import Hilligans.Container.Containers.SlabBlockContainer;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.BlockProperties;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.Packet.Server.SOpenContainer;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.ServerMain;
import Hilligans.World.DataProviders.ChestDataProvider;
import Hilligans.World.DataProviders.SlabChestDataProvider;
import Hilligans.World.World;

public class SlabChest extends SlabBlock {
    public SlabChest(String name, BlockProperties blockProperties) {
        super(name, blockProperties);
    }

    @Override
    public void onPlace(World world, BlockPos blockPos) {
        super.onPlace(world, blockPos);
        world.setDataProvider(blockPos, new SlabChestDataProvider());
    }

    @Override
    public boolean activateBlock(World world, PlayerEntity playerEntity, BlockPos pos) {
        if(world.isServer()) {
            SlabChestDataProvider chestDataProvider = (SlabChestDataProvider) world.getDataProvider(pos);
            SlabBlockContainer container = (SlabBlockContainer) new SlabBlockContainer(chestDataProvider.inventory,playerEntity.getPlayerData().playerInventory).setPlayerId(playerEntity.id);
            playerEntity.getPlayerData().openContainer(container);
            ServerMain.getServer().sendPacket(new SOpenContainer(container), playerEntity);
        }
        return true;
    }


}
