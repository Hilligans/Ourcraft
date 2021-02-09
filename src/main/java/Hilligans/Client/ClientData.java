package Hilligans.Client;

import Hilligans.Client.Rendering.World.TextureManager;
import Hilligans.Container.Container;
import Hilligans.Data.Other.Inventory;
import Hilligans.Item.ItemStack;
import Hilligans.Util.Settings;

public class ClientData {

    public static Inventory inventory = new Inventory(Settings.playerInventorySize);
    public static ItemStack heldStack = ItemStack.emptyStack();
    public static Container openContainer;


    public static void register() {

    }


}
