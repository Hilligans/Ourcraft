package dev.Hilligans.ourcraft.Addons.WorldEdit;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Command.CommandExecutors.CommandExecutor;
import dev.Hilligans.ourcraft.Command.CommandExecutors.EntityExecutor;
import dev.Hilligans.ourcraft.Command.CommandHandler;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Primitives.Tuple;
import dev.Hilligans.ourcraft.Util.DaisyChain;
import dev.Hilligans.ourcraft.World.SubChunk;
import dev.Hilligans.ourcraft.World.World;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class SetCommand extends WorldEditCommand {

    public SetCommand(WorldEditData worldEditData) {
        super("/set", worldEditData);
    }

    @Override
    public Object handle(CommandExecutor executor, String[] args) {
        if(executor instanceof EntityExecutor entityExecutor) {
            World world = executor.getWorld();
            Tuple<BlockPos,BlockPos> pos = worldEditData.setPositions.getOrDefault(entityExecutor.entity.id,null);
            if(pos == null || !pos.has()) {
                return "No Position Set";
            }

            if(args.length == 0) {
                return "No Block Provided";
            }
            DaisyChain<SubChunk> daisyChain = new DaisyChain<>();

            try {
                int val = Integer.parseInt(args[0]);
                Block b = Blocks.getBlockWithID(val);
                long time = System.currentTimeMillis();
                int blockNum = b.id << 16 | 65535;
                CompletableFuture<Object> future =  runThread(daisyChain,blockNum);
                CompletableFuture<Object> future1 = runThread(daisyChain,blockNum);

                BlockPos f = pos.getTypeA();
                BlockPos s = pos.typeB;

                for(int x = f.minX(s); x < f.maxX(s); x += 16) {
                    for(int y = f.minY(s); y < f.maxY(s); y += 16) {
                        for(int z = f.minZ(s); z < f.maxZ(s); z += 16) {
                            try {
                                daisyChain.add(world.ensureLoaded(x >> 4, z >> 4).getSubChunk(y >> 4));
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println(new BlockPos(x,y,z));
                            }
                        }
                    }
                }
                daisyChain.complete = true;
                future.get();
                future1.get();
                return "Placed " + f.createBoundingBox(s).getVolume() + " Blocks in " + (System.currentTimeMillis() - time) + " Milliseconds";

            } catch (Exception e) {
                e.printStackTrace();
                return "Block is string";
            }
        }
        return "Not an entity";
    }

    public CompletableFuture<Object> runThread(DaisyChain<SubChunk> daisyChain, int block) {
        return new CompletableFuture<>().completeAsync(() -> {
            try {
                SubChunk copyChunk = get(daisyChain);
                for (int x = 0; x < 4096; x++) {
                    copyChunk.vals[x] = block;
                }
                while (!daisyChain.isEmpty() || !daisyChain.isComplete()) {
                    SubChunk subChunk = daisyChain.get();
                    if (subChunk != null) {
                        if (subChunk.vals == null) {
                            subChunk.vals = new int[4096];
                            subChunk.empty = false;
                        }
                        System.arraycopy(copyChunk.vals, 0, subChunk.vals, 0, 4096);
                    }
                }
                System.out.println("Done");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    private static SubChunk get(DaisyChain<SubChunk> daisyChain) {
        while (daisyChain.isEmpty()) {}
        SubChunk copyChunk = daisyChain.get();
        return copyChunk == null ? get(daisyChain) : copyChunk;
    }
}
