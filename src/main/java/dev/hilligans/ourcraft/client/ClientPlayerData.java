package dev.hilligans.ourcraft.client;

import dev.hilligans.ourcraft.container.Container;
import dev.hilligans.ourcraft.data.other.Inventory;
import dev.hilligans.ourcraft.item.ItemStack;
import dev.hilligans.ourcraft.util.Settings;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientPlayerData {

    public HashMap<String, Object> arbDataMap = new HashMap<>();
    public Inventory inventory = new Inventory(Settings.playerInventorySize);
    public ItemStack heldStack = ItemStack.emptyStack();
    public Container openContainer;
    public boolean f3 = false;
    public boolean creative = true;
    public int handSlot = 0;
    public boolean flying = true;
    public boolean spectator = true;

    public String authToken = "";
    public String userName = "" + x.getAndIncrement();
    public String email = "";
    public String login_token = "";
    public boolean valid_account = false;


    //DEBUG
    public boolean debugChunkRendering = false;

    public Object get(String key) {
        return arbDataMap.get(key);
    }

    public <T> T get(String key, T clazz) {
        return (T) arbDataMap.get(key);
    }

    public <T> T set(String key, T newValue) {
        return (T) arbDataMap.put(key, newValue);
    }

    public static AtomicInteger x = new AtomicInteger();
}
