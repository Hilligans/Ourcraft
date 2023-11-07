package dev.hilligans.ourcraft.addons.worldedit;

import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.command.executors.CommandExecutor;
import dev.hilligans.ourcraft.command.executors.EntityExecutor;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.data.primitives.Tuple;
import dev.hilligans.ourcraft.server.concurrent.Lock;
import dev.hilligans.ourcraft.world.modifications.set.SetSingleModification;
import dev.hilligans.ourcraft.world.newworldsystem.IChunk;
import dev.hilligans.ourcraft.world.newworldsystem.IServerWorld;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;

public class SetCommand extends WorldEditCommand {

    public SetCommand(WorldEditData worldEditData) {
        super("/set", worldEditData);
    }

    @Override
    public Object handle(CommandExecutor executor, String[] args) {
        if(executor instanceof EntityExecutor entityExecutor) {
            IWorld world = executor.getWorld();

            Tuple<BlockPos,BlockPos> pos = worldEditData.setPositions.getOrDefault(entityExecutor.entity.id,null);
            if(pos == null || !pos.has()) {
                return "No Position Set";
            }

            if(args.length == 0) {
                return "No Block Provided";
            }

            try {
                int val = Integer.parseInt(args[0]);

                Block b = modContent.gameInstance.getBlockWithID(val);
                long time = System.currentTimeMillis();
                BlockPos f = pos.getTypeA();
                BlockPos s = pos.typeB;

                if(world instanceof IServerWorld serverWorld) {
                    serverWorld.queuePostTickEvent(serverWorld1 -> {
                        BlockPos min = new BlockPos(f.minX(s), f.minY(s), f.minZ(s));
                        BlockPos max = new BlockPos(f.maxX(s), f.maxY(s), f.maxZ(s));
                        SetSingleModification modification = new SetSingleModification(min, max, b.getDefaultState1());
                        try(Lock l = new Lock(serverWorld1.getChunkLocker())) {
                            for(IChunk chunk : serverWorld1.getChunks(min, max)) {
                                if(chunk != null) {
                                    modification.apply(l, chunk);
                                }
                            }
                        }
                    });
                }
                return "Placed " + f.createBoundingBox(s).getVolume() + " Blocks in " + (System.currentTimeMillis() - time) + " Milliseconds";

            } catch (Exception e) {
                e.printStackTrace();
                return "Block is string";
            }
        }
        return "Not an entity";
    }
}
