package dev.Hilligans.ourcraft.ModHandler.Events.Common;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.ModHandler.Event;

public class RegisterBlockEvent extends Event {

    public ModContent modContent;

    public RegisterBlockEvent(ModContent modContent) {
        this.modContent = modContent;
    }

    public void registerBlock(Block block) {
        modContent.blocks.add(block);
    }

}
