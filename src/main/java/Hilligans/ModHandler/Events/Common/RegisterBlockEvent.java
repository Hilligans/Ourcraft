package Hilligans.ModHandler.Events.Common;

import Hilligans.Block.Block;
import Hilligans.ModHandler.Content.ModContent;
import Hilligans.ModHandler.Event;

public class RegisterBlockEvent extends Event {

    public ModContent modContent;

    public RegisterBlockEvent(ModContent modContent) {
        this.modContent = modContent;
    }

    public void registerBlock(Block block) {
        modContent.blocks.add(block);
    }

}
