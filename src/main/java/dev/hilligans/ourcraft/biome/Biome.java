package dev.hilligans.ourcraft.biome;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.block.Blocks;
import dev.hilligans.ourcraft.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import dev.hilligans.ourcraft.world.builders.WorldBuilder;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.Arrays;

public class Biome implements IRegistryElement {

    public String name;
    public int terrainHeight = 10;
    public ModContainer source;

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
    public void load(GameInstance gameInstance) {
    }

    @Override
    public void assignOwner(ModContainer owner) {
        this.source = owner;
    }

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getResourceOwner() {
        return source.getModID();
    }

    @Override
    public String getResourceType() {
        return "biome";
    }
}
