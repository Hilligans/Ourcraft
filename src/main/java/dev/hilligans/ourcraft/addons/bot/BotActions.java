package dev.hilligans.ourcraft.addons.bot;

import dev.hilligans.ourcraft.addons.bot.pathfinding.Path;
import dev.hilligans.ourcraft.client.rendering.screens.container.screens.InventoryScreen;
import dev.hilligans.ourcraft.data.descriptors.ItemDescriptor;
import dev.hilligans.ourcraft.util.Settings;
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

    public BotActionResult selectItem(BotInstance botInstance, int pos) {
        if(pos >= Settings.hotBarWidth) {
            return BotActionResult.FAILURE;
        }
        botInstance.client.playerData.handSlot = pos;
        return BotActionResult.SUCCESS;
    }

    public int findItem(BotInstance botInstance, ItemDescriptor itemDescriptor) {
        for(int x = 0; x < botInstance.client.playerData.inventory.getSize(); x++) {

        }
        return -1;
    }

    public BotActionResult runPath(BotInstance botInstance, Path path) {





        return BotActionResult.SUCCESS;
    }




}
