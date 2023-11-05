package dev.hilligans.ourcraft.Data.Other.Server;

import dev.hilligans.ourcraft.Container.Container;
import dev.hilligans.ourcraft.Container.Containers.InventoryContainer;
import dev.hilligans.ourcraft.Container.Slot;
import dev.hilligans.ourcraft.Data.Other.Inventory;
import dev.hilligans.ourcraft.Data.UUID;
import dev.hilligans.ourcraft.Entity.Entities.ItemEntity;
import dev.hilligans.ourcraft.Entity.Entity;
import dev.hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.hilligans.ourcraft.Item.ItemStack;
import dev.hilligans.ourcraft.Network.IServerPacketHandler;
import dev.hilligans.ourcraft.Network.ServerNetworkHandler;
import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.Server.IServer;
import dev.hilligans.ourcraft.ServerMain;
import dev.hilligans.ourcraft.Tag.CompoundNBTTag;
import dev.hilligans.ourcraft.Util.EntityPosition;
import dev.hilligans.ourcraft.Util.Settings;
import dev.hilligans.ourcraft.World.NewWorldSystem.IServerWorld;
import dev.hilligans.ourcraft.World.NewWorldSystem.IWorld;
import dev.hilligans.ourcraft.WorldSave.WorldLoader;
import io.netty.channel.ChannelId;

public class ServerPlayerData implements IServerPacketHandler {

    IServer server;

    public PlayerEntity playerEntity;
    public ItemStack heldStack = ItemStack.emptyStack();
    public Container openContainer;
    public Inventory playerInventory;
    public String id;
    public boolean isCreative = true;
    public int opLevel = 1;

    public int lastX = Integer.MIN_VALUE;
    public int lastY = Integer.MIN_VALUE;
    public int lastZ = Integer.MIN_VALUE;

    public int renderDistance = 6;
    public int renderYDistance = 4;
    public ServerNetworkHandler serverNetworkHandler;
    public String playerName;
    public UUID playerID;
    public ChannelId channelId;

    public ServerPlayerData(PlayerEntity playerEntity, String id) {
        this.playerEntity = playerEntity;
        this.id = id;
        playerInventory = playerEntity.inventory;
        openContainer = new InventoryContainer(playerInventory).setPlayerId(playerEntity.id);
        playerInventory.setItem(0,new ItemStack(Ourcraft.GAME_INSTANCE.getItem("chest"), (byte)2));
        playerInventory.setItem(1,new ItemStack(Ourcraft.GAME_INSTANCE.getItem("slab"), (byte)10));
        playerInventory.setItem(2,new ItemStack(Ourcraft.GAME_INSTANCE.getItem("weeping_vine"), (byte)64));
        playerInventory.setItem(3,new ItemStack(Ourcraft.GAME_INSTANCE.getItem("stair"), (byte)63));
        playerInventory.setItem(4,new ItemStack(Ourcraft.GAME_INSTANCE.getItem("grass_plant"), (byte)63));
        playerInventory.setItem(5,new ItemStack(Ourcraft.GAME_INSTANCE.getItem("blue"),(byte)63));
    }

    public ServerPlayerData(PlayerEntity playerEntity, String id, CompoundNBTTag tag) {
        this.playerEntity = playerEntity;
        this.id = id;
        playerInventory = playerEntity.inventory;
        read(tag);
        openContainer = new InventoryContainer(playerInventory).setPlayerId(playerEntity.id);
    }

    public ServerPlayerData setServer(IServer server) {
        this.server = server;
        return this;
    }

    public ServerPlayerData setNetworkHandler(ServerNetworkHandler serverNetworkHandler) {
        this.serverNetworkHandler = serverNetworkHandler;
        return this;
    }

    public ServerPlayerData setName(String name) {
        this.playerName = name;
        return this;
    }

    public ServerPlayerData setPlayerID(UUID id) {
        this.playerID = id;
        return this;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public ServerPlayerData setChannelID(ChannelId channelId) {
        this.channelId = channelId;
        return this;
    }

    public ChannelId getChannelId() {
        return channelId;
    }


    public String getPlayerName() {
        return playerName;
    }

    public static ServerPlayerData loadOrCreatePlayer(PlayerEntity playerEntity, String id) {
        CompoundNBTTag tag = WorldLoader.loadTag(path + id + ".dat");
            if(tag == null) {
            return new ServerPlayerData(playerEntity,id);
        } else {
            return new ServerPlayerData(playerEntity,id,tag);
        }
    }

    public static String path = "world/" + Settings.worldName + "/player-data/";

    public void read(CompoundNBTTag tag) {
        try {
            CompoundNBTTag inventory = tag.getCompoundTag("inventory");
            if (inventory != null) {
                for (int x = 0; x < 27; x++) {
                    playerInventory.setItem(x, inventory.readStack(x));
                }
                heldStack = inventory.readStack(-1);
            }
            if (playerEntity != null) {
                playerEntity.position = new EntityPosition(tag);
                playerEntity.pitch = tag.getFloat("pitch").val;
                playerEntity.yaw = tag.getFloat("yaw").val;
            }
        } catch (Exception ignored) {}
    }

    public void write(CompoundNBTTag tag) {
        CompoundNBTTag inventory = new CompoundNBTTag();
        for(int x = 0; x < 27; x++) {
            inventory.writeStack(x,playerInventory.getItem(x));
        }
        inventory.writeStack(-1,heldStack);
        tag.putTag("inventory",inventory);

        playerEntity.position.write(tag);
        tag.putFloat("pitch",playerEntity.pitch);
        tag.putFloat("yaw",playerEntity.yaw);
    }

    public void save() {
        CompoundNBTTag compoundTag = new CompoundNBTTag();
        write(compoundTag);
        WorldLoader.save(compoundTag,ServerPlayerData.path + id + ".dat");
    }

    public int getDimension() {
        return playerEntity.dimension;
    }

    public void openContainer(Container container) {
        if(!(container instanceof InventoryContainer)) {
            container.uniqueId = Container.getId();
        }
        openContainer.closeContainer();
        openContainer = container;
    }

    public void swapStack(short slot) {
        heldStack = openContainer.swapStack(slot,heldStack);
    }

    public void splitStack(short slot) {
        heldStack = openContainer.splitStack(slot,heldStack);
    }

    public void putOne(short slot) {
        openContainer.putOne(slot,heldStack);
    }

    public void copyStack(short slot) {
        heldStack = openContainer.copyStack(slot,heldStack);
    }

    public void dropItem(short slot, byte count) {
        if(slot == -1) {
            if(!heldStack.isEmpty()) {
                if(count == -1 || count >= heldStack.count) {
                    ServerMain.getWorld(getDimension()).addEntity(new ItemEntity(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), Entity.getNewId(), heldStack).setVel(playerEntity.getForeWard().mul(-0.5f).add(0, 0.25f, 0)));
                    heldStack = ItemStack.emptyStack();
                } else {
                    ServerMain.getWorld(getDimension()).addEntity(new ItemEntity(playerEntity.getX(),playerEntity.getY(),playerEntity.getZ(),Entity.getNewId(),new ItemStack(heldStack.item,count)).setVel(playerEntity.getForeWard().mul(-0.5f).add(0, 0.25f, 0)));
                    heldStack.count -= count;
                }
            }
        } else {
            Slot itemSlot = openContainer.getSlot(slot);
            if(itemSlot != null) {
                if(!itemSlot.getContents().isEmpty()) {
                    if(count == -1 || count >= itemSlot.getContents().count) {
                        ServerMain.getWorld(getDimension()).addEntity(new ItemEntity(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), Entity.getNewId(), itemSlot.getContents()).setVel(playerEntity.getForeWard().mul(-0.5f).add(0, 0.25f, 0)));
                        itemSlot.setContents(ItemStack.emptyStack());
                    } else {
                        ServerMain.getWorld(getDimension()).addEntity(new ItemEntity(playerEntity.getX(),playerEntity.getY(),playerEntity.getZ(),Entity.getNewId(),new ItemStack(itemSlot.getContents().item,count)).setVel(playerEntity.getForeWard().mul(-0.5f).add(0, 0.25f, 0)));
                        itemSlot.getContents().count -= count;
                    }
                }
            }
        }
    }

    public void close() {
        save();
        openContainer.closeContainer();
    }

    @Override
    public IServer getServer() {
        return server;
    }

    @Override
    public IServerWorld getWorld() {
        return server.getWorld(this);
    }

    @Override
    public ServerPlayerData getServerPlayerData() {
        return this;
    }

    @Override
    public PlayerEntity getPlayerEntity() {
        return playerEntity;
    }

    @Override
    public ServerNetworkHandler getServerNetworkHandler() {
        return null;
    }
}
