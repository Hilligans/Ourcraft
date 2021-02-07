package Hilligans.Biome;

import Hilligans.Block.Blocks;
import Hilligans.World.Builders.Foliage.CactusBuilder;
import Hilligans.World.Builders.Foliage.LargeTreeBuilder;
import Hilligans.World.Builders.Foliage.TreeBuilder;
import org.joml.Vector3i;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Biomes {

    public static ArrayList<Biome> biomes = new ArrayList<>();

    public static final Biome PLAINS = new Biome("plains" ).setParams(0,0.5f,0,0,0);
    public static final Biome DESERT = new Biome("desert", new CactusBuilder().setFrequency(7)).setSurfaceBlock(Blocks.SAND).setUnderBlock(Blocks.SAND).setParams(0.5f,-0.5f,0,0,0);
    public static final Biome FOREST = new Biome("forest", new TreeBuilder().setFrequency(2)).setParams(0,0.7f,0,0.5f,0);

    public static final Biome MOUNTAIN = new Biome("mountain").setParams(-0.2f,-0.2f,0,0,0.4f).setHeight(new Vector3i(-5,5,50));
    public static final Biome MOUNTAIN_EDGE = new Biome("mountain_edge").setParams(-0.2f,-0.2f,0,0,0.3f).setHeight(new Vector3i(-3,5,25));
    //public static final Biome DESERT1 = new Biome("desert1").setSurfaceBlock(Blocks.PHIL);



    private static final float noiseFactor = 1;
    private static final float tempFactor = 2;
    private static final float humidityFactor = 2;
    private static final float randFactor = 1;
    private static final float variationFactor = 1;

    public static Biome getBiome(double noise, double temp, double humidity, double rand, double variation) {
        ArrayList<Double> doubles = new ArrayList<>(biomes.size());


        for(Biome biome : biomes) {
            doubles.add(Math.abs(noise - biome.noise) * noiseFactor + Math.abs(temp - biome.temp) * tempFactor + Math.abs(humidity - biome.humidity) * humidityFactor + Math.abs(rand - biome.rand) * randFactor + Math.abs(variation - biome.variation) * variationFactor);
        }

        Biome biome = PLAINS;
        double val = 10000;
        for(int x = 0; x < biomes.size(); x++) {
            if(val > doubles.get(x)) {
                biome = biomes.get(x);
                val = doubles.get(x);
            }
        }
        return biome;
    }


}
