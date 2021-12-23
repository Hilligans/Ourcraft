package dev.Hilligans.ourcraft.Addons.WorldEdit;

import dev.Hilligans.ourcraft.Command.CommandExecutors.CommandExecutor;
import dev.Hilligans.ourcraft.Command.CommandHandler;
import dev.Hilligans.ourcraft.Data.Other.BlockStates.BlockState;

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
