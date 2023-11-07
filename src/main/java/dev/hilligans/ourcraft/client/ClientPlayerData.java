package dev.hilligans.ourcraft.client;

import dev.hilligans.ourcraft.container.Container;
import dev.hilligans.ourcraft.data.other.Inventory;
import dev.hilligans.ourcraft.item.ItemStack;
import dev.hilligans.ourcraft.util.Settings;

public class ClientPlayerData {

    public Inventory inventory = new Inventory(Settings.playerInventorySize);
    public ItemStack heldStack = ItemStack.emptyStack();
    public Container openContainer;
    public boolean f3 = false;
    public boolean creative = true;
    public int handSlot = 0;
    public boolean flying = true;
    public boolean spectator = true;

    public String authToken = "";
    public String userName = "";
    public String email = "";
    public String login_token = "";
    public boolean valid_account = false;


}
