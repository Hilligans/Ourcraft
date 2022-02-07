package dev.Hilligans.ourcraft.World.Builders;

import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.Feature;
import dev.Hilligans.ourcraft.World.World;

import java.util.Random;

public abstract class WorldBuilder extends Feature {

    public World world;
    public int frequency = 1;
    public int chance = 1;

    public Random random;

    public WorldBuilder(String featureName) {
        super(featureName);
    }


    public WorldBuilder setWorld(World world) {
        this.world = world;
        random = world.random;
        return this;
    }

    public WorldBuilder setFrequency(int frequency) {
        this.frequency = frequency;
        return this;
    }

    public WorldBuilder setChance(int chance) {
        this.chance = chance;
        return this;
    }

    public void build(Chunk chunk) {
        world = chunk.world;
        random = world.random;
        for(int a = 0; a < frequency; a++) {
            if(chance == 0 || random.nextInt(chance) == 0) {
                int x = random.nextInt(16);
                int z = random.nextInt(16);
                build(x + chunk.x * 16, z + chunk.z * 16);
            }
        }
    }

    public abstract void build(int x, int z);

    public void build(BlockPos pos) {}

    //public abstract void build(BlockPos startPos);

}
