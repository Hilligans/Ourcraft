package dev.Hilligans.Ourcraft.Data.Other.Server;

import dev.Hilligans.Ourcraft.Container.Container;
import dev.Hilligans.Ourcraft.Container.Containers.InventoryContainer;
import dev.Hilligans.Ourcraft.Container.Slot;
import dev.Hilligans.Ourcraft.Data.Other.Inventory;
import dev.Hilligans.Ourcraft.Entity.Entities.ItemEntity;
import dev.Hilligans.Ourcraft.Entity.Entity;
import dev.Hilligans.Ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.Hilligans.Ourcraft.Item.ItemStack;
import dev.Hilligans.Ourcraft.Item.Items;
import dev.Hilligans.Ourcraft.ServerMain;
import dev.Hilligans.Ourcraft.Tag.CompoundTag;
import dev.Hilligans.Ourcraft.Util.Settings;
import dev.Hilligans.Ourcraft.WorldSave.WorldLoader;

public class ServerPlayerData {

    public PlayerEntity playerEntity;
    public ItemStack heldStack = ItemStack.emptyStack();
    public Container openContainer;
    public Inventory playerInventory;
    public String id;
    public boolean isCreative = true;
    public int opLevel = 1;


    public ServerPlayerData(PlayerEntity playerEntity, String id) {
        this.playerEntity = playerEntity;
        this.id = id;
        playerInventory = playerEntity.inventory;
        openContainer = new InventoryContainer(playerInventory).setPlayerId(playerEntity.id);
        playerInventory.setItem(0,new ItemStack(Items.HASHED_ITEMS.get("chest"), (byte)2));
        playerInventory.setItem(1,new ItemStack(Items.HASHED_ITEMS.get("slab"), (byte)10));
        playerInventory.setItem(2,new ItemStack(Items.HASHED_ITEMS.get("weeping_vine"), (byte)64));
        playerInventory.setItem(3,new ItemStack(Items.HASHED_ITEMS.get("stair"), (byte)63));
        playerInventory.setItem(4,new ItemStack(Items.HASHED_ITEMS.get("grass_plant"), (byte)63));
        playerInventory.setItem(5,new ItemStack(Items.HASHED_ITEMS.get("blue"),(byte)63));

    }

    public ServerPlayerData(PlayerEntity playerEntity, String id, CompoundTag tag) {
        this.playerEntity = playerEntity;
        this.id = id;
        playerInventory = playerEntity.inventory;
        read(tag);
        openContainer = new InventoryContainer(playerInventory).setPlayerId(playerEntity.id);
    }

    public static ServerPlayerData loadOrCreatePlayer(PlayerEntity playerEntity, String id) {
        CompoundTag tag = WorldLoader.loadTag(path + id + ".dat");
        if(tag == null) {
            return new ServerPlayerData(playerEntity,id);
        } else {
            //System.out.println("asdaw");
            return new ServerPlayerData(playerEntity,id,tag);
        }
    }

    public static String path = "world/" + Settings.worldName + "/player-data/";

    public void read(CompoundTag tag) {
        try {
            CompoundTag inventory = tag.getCompoundTag("inventory");
            if (inventory != null) {
                for (int x = 0; x < 27; x++) {
                    playerInventory.setItem(x, inventory.readStack(x));
                }
                heldStack = inventory.readStack(-1);
            }
            if (playerEntity != null) {
                playerEntity.x = (float) tag.getDouble("x").val;
                playerEntity.y = (float) tag.getDouble("y").val;
                playerEntity.z = (float) tag.getDouble("z").val;
                playerEntity.pitch = tag.getFloat("pitch").val;
                playerEntity.yaw = tag.getFloat("yaw").val;
            }
        } catch (Exception ignored) {}
    }

    public void write(CompoundTag tag) {
        CompoundTag inventory = new CompoundTag();
        for(int x = 0; x < 27; x++) {
            inventory.writeStack(x,playerInventory.getItem(x));
        }
        inventory.writeStack(-1,heldStack);
        tag.putTag("inventory",inventory);

        tag.putDouble("x",playerEntity.x);
        tag.putDouble("y",playerEntity.y);
        tag.putDouble("z",playerEntity.z);
        tag.putFloat("pitch",playerEntity.pitch);
        tag.putFloat("yaw",playerEntity.yaw);
    }

    public void save() {
        CompoundTag compoundTag = new CompoundTag();
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
                    ServerMain.getWorld(getDimension()).addEntity(new ItemEntity(playerEntity.x, playerEntity.y, playerEntity.z, Entity.getNewId(), heldStack).setVel(playerEntity.getForeWard().mul(-0.5f).add(0, 0.25f, 0)));
                    heldStack = ItemStack.emptyStack();
                } else {
                    ServerMain.getWorld(getDimension()).addEntity(new ItemEntity(playerEntity.x,playerEntity.y,playerEntity.z,Entity.getNewId(),new ItemStack(heldStack.item,count)).setVel(playerEntity.getForeWard().mul(-0.5f).add(0, 0.25f, 0)));
                    heldStack.count -= count;
                }
            }
        } else {
            Slot itemSlot = openContainer.getSlot(slot);
            if(itemSlot != null) {
                if(!itemSlot.getContents().isEmpty()) {
                    if(count == -1 || count >= itemSlot.getContents().count) {
                        ServerMain.getWorld(getDimension()).addEntity(new ItemEntity(playerEntity.x, playerEntity.y, playerEntity.z, Entity.getNewId(), itemSlot.getContents()).setVel(playerEntity.getForeWard().mul(-0.5f).add(0, 0.25f, 0)));
                        itemSlot.setContents(ItemStack.emptyStack());
                    } else {
                        ServerMain.getWorld(getDimension()).addEntity(new ItemEntity(playerEntity.x,playerEntity.y,playerEntity.z,Entity.getNewId(),new ItemStack(itemSlot.getContents().item,count)).setVel(playerEntity.getForeWard().mul(-0.5f).add(0, 0.25f, 0)));
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

}
