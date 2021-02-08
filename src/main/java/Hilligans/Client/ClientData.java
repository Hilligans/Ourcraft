package Hilligans.Client;

import Hilligans.Client.Rendering.World.TextureManager;
import Hilligans.Data.Other.Inventory;
import Hilligans.Util.Settings;

public class ClientData {

    public static int containerId;
    public static Inventory inventory = new Inventory(Settings.playerInventorySize);


    public static int itemSlot;

    public static void register() {
        itemSlot = TextureManager.loadAndRegisterUnflippedTexture("item_slot.png");

    }


}
