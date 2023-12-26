package dev.hilligans.ourcraft.world.builders;

import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.world.Feature;
import dev.hilligans.ourcraft.world.newworldsystem.IChunk;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;

import java.util.Random;

public abstract class WorldBuilder extends Feature {

    public IWorld world;
    public int frequency = 1;
    public int chance = 1;

    public Random random;

    public WorldBuilder(String featureName) {
        super(featureName);
    }


    public WorldBuilder setWorld(IWorld world) {
        this.world = world;
        random = new Random();
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

    public void build(IChunk chunk) {
        /*
        world = chunk.world;
        random = world.random;
        for(int a = 0; a < frequency; a++) {
            if(chance == 0 || random.nextInt(chance) == 0) {
                int x = random.nextInt(16);
                int z = random.nextInt(16);
                build(x + chunk.x * 16, z + chunk.z * 16);
            }
        }

         */
    }

    public abstract void build(int x, int z);

    public void build(BlockPos pos) {}

    //public abstract void build(BlockPos startPos);

}
