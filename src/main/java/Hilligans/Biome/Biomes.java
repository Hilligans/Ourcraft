package Hilligans.Biome;

import Hilligans.Block.Blocks;
import Hilligans.World.Builders.Foliage.CactusBuilder;
import Hilligans.World.Builders.Foliage.LargeTreeBuilder;
import Hilligans.World.Builders.Foliage.TreeBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Biomes {

    public static ArrayList<Biome> biomes = new ArrayList<>();

    public static final Biome PLAINS = new Biome("plains" ).setParams(0.5f,1.0f,1.0f);
    public static final Biome DESERT = new Biome("desert", new CactusBuilder().setFrequency(7)).setSurfaceBlock(Blocks.SAND).setUnderBlock(Blocks.SAND).setParams(0.2f,0.1f,1.0f);
    public static final Biome FOREST = new Biome("forest", new TreeBuilder().setFrequency(2)).setParams(0.7f,1.0f,1.0f);
    //public static final Biome DESERT1 = new Biome("desert1").setSurfaceBlock(Blocks.PHIL);



    private static final int noiseFactor = 1;
    private static final int tempFactor = 1;
    private static final int humidityFactor = 1;

    public static Biome getBiome(double noise, double temp, double humidity) {
        ArrayList<Double> doubles = new ArrayList<>(biomes.size());



       // System.out.println("noise " + noise + " temp " + temp + " humidity " + humidity);

        for(Biome biome : biomes) {
            doubles.add(Math.abs(noise - biome.noise) * noiseFactor + Math.abs(temp - biome.temp) * tempFactor + Math.abs(humidity - biome.humidity) * humidityFactor);
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
