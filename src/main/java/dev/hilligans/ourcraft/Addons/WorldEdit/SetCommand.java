package dev.hilligans.ourcraft.Addons.WorldEdit;

import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.Command.CommandExecutors.CommandExecutor;
import dev.hilligans.ourcraft.Command.CommandExecutors.EntityExecutor;
import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Data.Primitives.Tuple;
import dev.hilligans.ourcraft.Server.Concurrent.Lock;
import dev.hilligans.ourcraft.World.Modifications.SetBlock.SetSingleModification;
import dev.hilligans.ourcraft.World.NewWorldSystem.IChunk;
import dev.hilligans.ourcraft.World.NewWorldSystem.IServerWorld;
import dev.hilligans.ourcraft.World.NewWorldSystem.IWorld;

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
