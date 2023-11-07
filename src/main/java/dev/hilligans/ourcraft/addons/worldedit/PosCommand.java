package dev.hilligans.ourcraft.addons.worldedit;

import dev.hilligans.ourcraft.command.executors.CommandExecutor;
import dev.hilligans.ourcraft.command.executors.EntityExecutor;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.primitives.Tuple;

public class PosCommand extends WorldEditCommand {

    int num;

    public PosCommand(int num, WorldEditData worldEditData) {
        super("/pos" + num, worldEditData);
        this.num = num;
    }

    @Override
    public Object handle(CommandExecutor executor, String[] args) {
        if(executor instanceof EntityExecutor entityExecutor) {
            Tuple<BlockPos, BlockPos> posSet = worldEditData.setPositions.computeIfAbsent(entityExecutor.entity.id, a -> new Tuple<>());
            BlockPos pos;
            if(args.length == 0) {
                pos = new BlockPos(entityExecutor.getX(),entityExecutor.getY(),entityExecutor.getZ());
            } else if(args.length == 3) {
                try {
                    pos = new BlockPos(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                } catch (Exception e) {
                    return "Not a number";
                }
            } else {
                return "Invalid arguments";
            }
            if (num == 1) {
                posSet.typeA = pos;
            } else {
                posSet.typeB = pos;
            }
            return "Set pos to " + pos.toString();
        }
        return "Failed Command";
    }
}
