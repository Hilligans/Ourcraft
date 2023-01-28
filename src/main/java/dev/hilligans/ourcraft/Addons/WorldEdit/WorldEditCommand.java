package dev.hilligans.ourcraft.Addons.WorldEdit;

import dev.hilligans.ourcraft.Command.CommandHandler;
import dev.hilligans.ourcraft.Data.Other.BlockStates.BlockState;

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
