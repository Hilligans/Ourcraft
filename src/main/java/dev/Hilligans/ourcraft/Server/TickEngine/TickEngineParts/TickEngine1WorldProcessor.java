package dev.Hilligans.ourcraft.Server.TickEngine.TickEngineParts;

import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Server.TickEngine.IWorldProcessor;
import dev.Hilligans.ourcraft.Server.TickEngine.TickEngineSettings;
import dev.Hilligans.ourcraft.World.SubChunk;
import dev.Hilligans.ourcraft.World.World;

import java.util.Random;

public class TickEngine1WorldProcessor implements IWorldProcessor {

    Random random = new Random(System.nanoTime());
    public TickEngineSettings tickEngineSettings;

    public TickEngine1WorldProcessor(TickEngineSettings tickEngineSettings) {
        this.tickEngineSettings = tickEngineSettings;
    }

    @Override
    public int processWorld(World world) {
        try {
            world.chunkContainer.forEach(chunk -> {
                int y = 0;
                for (SubChunk subChunk : chunk.chunks) {
                    for (int x = 0; x < tickEngineSettings.randomTickSpeed; x++) {
                        if (subChunk != null && !subChunk.empty) {
                            BlockPos pos = BlockPos.fromSubChunkPos(random.nextInt(4096));
                            subChunk.getBlock(pos).getBlock().randomTick(world, pos.copy().add(chunk.x * 16, y * 16, chunk.z * 16));
                        }
                    }
                    y++;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
