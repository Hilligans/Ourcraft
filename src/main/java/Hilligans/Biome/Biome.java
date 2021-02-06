package Hilligans.Biome;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.World.Builders.WorldBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class Biome {

    public String name;
    public int terrainHeight = 0;
    public ArrayList<WorldBuilder> worldBuilders = new ArrayList<>();

    public float temp = 1.0f;
    public float humidity = 1.0f;
    public float noise = 1.0f;

    public Biome(String name) {
        this.name = name;
        Biomes.biomes.add(this);
    }

    public Biome(String name, WorldBuilder... worldBuilders) {
        this(name);
        this.worldBuilders.addAll(Arrays.asList(worldBuilders));
    }

    public Biome setSurfaceBlock(Block block) {
        this.surfaceBlock = block;
        return this;
    }

    public Biome setUnderBlock(Block block) {
        this.underBlock = block;
        return this;
    }

    public Biome setParams(float temp, float humidity, float noise) {
        this.temp = temp;
        this.humidity = humidity;
        this.noise = noise;
        return this;
    }

    public Block surfaceBlock = Blocks.GRASS;
    public Block underBlock = Blocks.DIRT;




}
