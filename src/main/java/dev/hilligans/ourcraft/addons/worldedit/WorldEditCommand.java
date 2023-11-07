package dev.hilligans.ourcraft.addons.worldedit;

import dev.hilligans.ourcraft.command.CommandHandler;
import dev.hilligans.ourcraft.data.other.blockstates.BlockState;

import java.util.ArrayList;

public abstract class WorldEditCommand extends CommandHandler {

    public WorldEditData worldEditData;

    public WorldEditCommand(String command, WorldEditData worldEditData) {
        super(command);
        this.worldEditData = worldEditData;
    }



    public static ArrayList<BlockState> resolve(String input) {

        return null;
    }



}
