package dev.Hilligans.ModHandler.Events.Common;

import dev.Hilligans.Block.Block;
import dev.Hilligans.ModHandler.Content.ModContent;
import dev.Hilligans.ModHandler.Event;

public class RegisterBlockEvent extends Event {

    public ModContent modContent;

    public RegisterBlockEvent(ModContent modContent) {
        this.modContent = modContent;
    }

    public void registerBlock(Block block) {
        modContent.blocks.add(block);
    }

}
