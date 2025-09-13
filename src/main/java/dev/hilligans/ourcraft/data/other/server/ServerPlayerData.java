package dev.hilligans.ourcraft.data.other.server;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.container.Container;
import dev.hilligans.ourcraft.container.Slot;
import dev.hilligans.ourcraft.container.containers.InventoryContainer;
import dev.hilligans.engine.data.UUID;
import dev.hilligans.ourcraft.data.other.Inventory;
import dev.hilligans.ourcraft.entity.IPlayerEntity;
import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;
import dev.hilligans.ourcraft.item.ItemStack;
import dev.hilligans.engine.network.engine.NetworkEntity;
import dev.hilligans.engine.save.FileLoader;
import dev.hilligans.ourcraft.server.IServer;
import dev.hilligans.engine.tag.CompoundNBTTag;
import dev.hilligans.ourcraft.util.EntityPosition;
import dev.hilligans.engine.util.IByteArray;
import dev.hilligans.ourcraft.util.Settings;
import dev.hilligans.ourcraft.world.newworldsystem.IServerWorld;
import io.netty.channel.Channel;

import java.util.HashMap;

public class ServerPlayerData {

    IServer server;

    public HashMap<String, Object> arbDataMap = new HashMap<>();

    public PlayerEntity playerEntity;
    public IPlayerEntity entity;

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
    public String playerName;
    public UUID playerID;

    GameInstance gameInstance;

    public Channel channel;

    public NetworkEntity networkEntity;

    public ServerPlayerData(GameInstance gameInstance, PlayerEntity playerEntity, String id) {
        this.playerEntity = playerEntity;
        this.gameInstance = gameInstance;
        this.id = id;
        playerInventory = playerEntity.inventory;
        openContainer = new InventoryContainer(playerInventory).setPlayerId(playerEntity.id);
        playerInventory.setItem(0,new ItemStack(gameInstance.getItem("chest"), (byte)2));
        playerInventory.setItem(1,new ItemStack(gameInstance.getItem("slab"), (byte)10));
        playerInventory.setItem(2,new ItemStack(gameInstance.getItem("weeping_vine"), (byte)64));
        playerInventory.setItem(3,new ItemStack(gameInstance.getItem("stair"), (byte)63));
        playerInventory.setItem(4,new ItemStack(gameInstance.getItem("grass_plant"), (byte)63));
        playerInventory.setItem(5,new ItemStack(gameInstance.getItem("blue"),(byte)63));
    }

    public ServerPlayerData(GameInstance gameInstance, String name) {
        this.playerName = name;
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

    public ServerPlayerData setName(String name) {
        this.playerName = name;
        return this;
    }

    public ServerPlayerData setPlayerID(UUID id) {
        this.playerID = id;
        return this;
    }

    public void sendPacket(IByteArray array) {
        networkEntity.sendPacket(array);
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public static ServerPlayerData loadOrCreatePlayer(GameInstance gameInstance, PlayerEntity playerEntity, String id) {
        CompoundNBTTag tag = FileLoader.loadTag(path + id + ".dat");
            if(tag == null) {
            return new ServerPlayerData(gameInstance, playerEntity,id);
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
                playerEntity.pitch = tag.getFloat("pitch");
                playerEntity.yaw = tag.getFloat("yaw");
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
        FileLoader.save(compoundTag,ServerPlayerData.path + id + ".dat");
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
                   // ServerMain.getWorld(getDimension()).addEntity(new ItemEntity(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), Entity.getNewId(), heldStack).setVel(playerEntity.getForeWard().mul(-0.5f).add(0, 0.25f, 0)));
                    heldStack = ItemStack.emptyStack();
                } else {
                   // ServerMain.getWorld(getDimension()).addEntity(new ItemEntity(playerEntity.getX(),playerEntity.getY(),playerEntity.getZ(),Entity.getNewId(),new ItemStack(heldStack.item,count)).setVel(playerEntity.getForeWard().mul(-0.5f).add(0, 0.25f, 0)));
                    heldStack.count -= count;
                }
            }
        } else {
            Slot itemSlot = openContainer.getSlot(slot);
            if(itemSlot != null) {
                if(!itemSlot.getContents().isEmpty()) {
                    if(count == -1 || count >= itemSlot.getContents().count) {
                      //  ServerMain.getWorld(getDimension()).addEntity(new ItemEntity(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), Entity.getNewId(), itemSlot.getContents()).setVel(playerEntity.getForeWard().mul(-0.5f).add(0, 0.25f, 0)));
                        itemSlot.setContents(ItemStack.emptyStack());
                    } else {
                     //   ServerMain.getWorld(getDimension()).addEntity(new ItemEntity(playerEntity.getX(),playerEntity.getY(),playerEntity.getZ(),Entity.getNewId(),new ItemStack(itemSlot.getContents().item,count)).setVel(playerEntity.getForeWard().mul(-0.5f).add(0, 0.25f, 0)));
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

    public IServer getServer() {
        return server;
    }

    public IServerWorld getWorld() {
        return server.getWorld(this);
    }

    public PlayerEntity getPlayerEntity() {
        return playerEntity;
    }
}
