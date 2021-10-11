package Hilligans.Bot;

import Hilligans.Client.Rendering.Screens.ContainerScreens.InventoryScreen;
import org.joml.Vector3d;
import org.joml.Vector3i;

public class BotActions {

    public BotActionResult goTo(BotInstance botInstance, Vector3d pos) {
        return BotActionResult.SUCCESS;
    }

    public BotActionResult breakBlock(BotInstance botInstance, Vector3i pos) {

        return BotActionResult.SUCCESS;
    }

    public BotActionResult breakBlockSmart(BotInstance botInstance, Vector3i pos) {


        return BotActionResult.SUCCESS;
    }

    public BotActionResult openInventory(BotInstance botInstance) {
        botInstance.client.openScreen(new InventoryScreen(botInstance.client));
        return BotActionResult.SUCCESS;
    }

    public BotActionResult swapItem(BotInstance botInstance, int startPos, int toPos) {
        if(!(botInstance.client.screen instanceof InventoryScreen)) {
            return BotActionResult.NO_PERMISSION;
        }
        return BotActionResult.SUCCESS;
    }

    public int findItem(BotInstance botInstance) {

        return -1;
    }




}
