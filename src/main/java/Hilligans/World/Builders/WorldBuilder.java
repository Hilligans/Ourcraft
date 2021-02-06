package Hilligans.World.Builders;

import Hilligans.World.Chunk;
import Hilligans.World.World;

import java.util.Random;

public abstract class WorldBuilder {

    public World world;
    public int frequency = 1;

    public Random random;


    public WorldBuilder setWorld(World world) {
        this.world = world;
        random = world.random;
        return this;
    }

    public WorldBuilder setFrequency(int frequency) {
        this.frequency = frequency;
        return this;
    }

    public void build(Chunk chunk) {
        world = chunk.world;
        random = world.random;
        for(int a = 0; a < frequency; a++) {
            int x = random.nextInt(16);
            int z = random.nextInt(16);
            build(x + chunk.x * 16,z + chunk.z * 16);
        }
    }

    public abstract void build(int x, int z);

    //public abstract void build(BlockPos startPos);

}
