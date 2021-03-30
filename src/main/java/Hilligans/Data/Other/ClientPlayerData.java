package Hilligans.Data.Other;

import Hilligans.Container.Container;
import Hilligans.Data.Other.Inventory;
import Hilligans.Item.ItemStack;
import Hilligans.Util.Settings;

public class ClientPlayerData {

    public static Inventory inventory = new Inventory(Settings.playerInventorySize);
    public static ItemStack heldStack = ItemStack.emptyStack();
    public static Container openContainer;
    public static boolean f3 = false;
    public static boolean creative = true;
    public static int handSlot = 0;
    public static boolean flying = true;
    public static boolean spectator = true;




}
