package dev.Hilligans.Ourcraft.ModHandler.Events.Common;

import dev.Hilligans.Ourcraft.Block.Block;
import dev.Hilligans.Ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.Ourcraft.ModHandler.Event;

public class RegisterBlockEvent extends Event {

    public ModContent modContent;

    public RegisterBlockEvent(ModContent modContent) {
        this.modContent = modContent;
    }

    public void registerBlock(Block block) {
        modContent.blocks.add(block);
    }

}
