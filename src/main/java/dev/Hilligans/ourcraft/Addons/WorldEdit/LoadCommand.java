package dev.Hilligans.ourcraft.Addons.WorldEdit;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Command.CommandExecutors.CommandExecutor;
import dev.Hilligans.ourcraft.Command.CommandExecutors.EntityExecutor;
import dev.Hilligans.ourcraft.Command.CommandHandler;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Primitives.Tuple;
import dev.Hilligans.ourcraft.World.SubChunk;
import dev.Hilligans.ourcraft.World.World;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoadCommand extends WorldEditCommand {

    public LoadCommand(WorldEditData worldEditData) {
        super("/load", worldEditData);
    }

    @Override
    public Object handle(CommandExecutor executor, String[] args) {
        if(executor instanceof EntityExecutor entityExecutor) {
            World world = executor.getWorld();
            Tuple<BlockPos,BlockPos> pos = worldEditData.setPositions.getOrDefault(entityExecutor.entity.id,null);
            if(pos == null || !pos.has()) {
                return "No Position Set";
            }
            try {
                BlockPos f = pos.getTypeA();
                BlockPos s = pos.typeB;

                for(int x = f.minX(s); x < f.maxX(s); x += 16) {
                    for(int y = f.minY(s); y < f.maxY(s); y += 16) {
                        for(int z = f.minZ(s); z < f.maxZ(s); z += 16) {
                            world.generateChunk(x >> 4, z >> 4);
                        }
                    }
                }
                System.out.println("finished loading");
                return "Loaded Chunks";

            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
        }
        return "Not an entity";
    }
}
