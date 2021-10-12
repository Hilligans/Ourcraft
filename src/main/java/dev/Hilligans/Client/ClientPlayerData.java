package dev.Hilligans.Client;

import dev.Hilligans.Container.Container;
import dev.Hilligans.Data.Other.Inventory;
import dev.Hilligans.Item.ItemStack;
import dev.Hilligans.Util.Settings;

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
