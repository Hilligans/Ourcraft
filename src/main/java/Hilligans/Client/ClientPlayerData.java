package Hilligans.Client;

import Hilligans.Container.Container;
import Hilligans.Data.Other.Inventory;
import Hilligans.Item.ItemStack;
import Hilligans.Util.Settings;
import at.favre.lib.crypto.bcrypt.BCrypt;

public class ClientPlayerData {

    public static Inventory inventory = new Inventory(Settings.playerInventorySize);
    public static ItemStack heldStack = ItemStack.emptyStack();
    public static Container openContainer;
    public static boolean f3 = false;
    public static boolean creative = true;
    public static int handSlot = 0;
    public static boolean flying = true;
    public static boolean spectator = true;

    public static String authToken = "";
    public static String userName = "";
    public static String password = "";
    public static String email = "";
    public static boolean valid_account = false;

    public static String hashString(String password) {
        return BCrypt.withDefaults().hashToString(16, password.toCharArray());
    }




}
