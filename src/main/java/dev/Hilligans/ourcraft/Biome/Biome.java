package dev.Hilligans.ourcraft.Biome;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;
import dev.Hilligans.ourcraft.World.Builders.WorldBuilder;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.Arrays;

public class Biome implements IRegistryElement {

    public String name;
    public int terrainHeight = 10;
    public ModContent source;

    public Vector3i terrainHeights = new Vector3i(0,5,5);

    public ArrayList<WorldBuilder> worldBuilders = new ArrayList<>();

    public float temp = 1.0f;
    public float humidity = 1.0f;
    public float noise = 1.0f;
    public float rand = 1.0f;
    public float variation = 1.0f;

    public Biome(String name) {
        this.name = name;
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

    public Biome setHeight(Vector3i height) {
        this.terrainHeights = height;
        return this;
    }

    public Biome setParams(float temp, float humidity, float noise, float rand, float variation) {
        this.temp = temp;
        this.humidity = humidity;
        this.noise = noise;
        this.rand = rand;
        this.variation = variation;
        return this;
    }

    public Block surfaceBlock = Blocks.GRASS;
    public Block underBlock = Blocks.DIRT;


    @Override
    public void load() {
    }

    @Override
    public String getResourceName() {
        return name;
    }
}
