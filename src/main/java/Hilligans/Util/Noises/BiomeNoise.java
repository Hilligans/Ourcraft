package Hilligans.Util.Noises;

import Hilligans.Biome.Biome;
import Hilligans.Biome.Biomes;

import java.util.Random;

public class BiomeNoise {

    double size = 200;

    KenPerlinNoise biome;
    KenPerlinNoise humidity;

    PerlinNoise perlinNoise;
    PerlinNoise tempNoise;
    PerlinNoise humidityNoise;

    public BiomeNoise(Random random) {
        perlinNoise = new PerlinNoise(random.nextInt(),2,0.01,0.707,1);
        tempNoise = new PerlinNoise(random.nextInt(),2,0.01,0.707,1);
        humidityNoise = new PerlinNoise(random.nextInt(),2,0.01,0.707,1);
        //biome = new KenPerlinNoise(random.nextInt());
        //humidity = new KenPerlinNoise(random.nextInt());

    }

    double max = 0;

    public double noise(int x, int z) {
        double noise = biome.noise(x / size,z / size);
        //double noise = perlinNoise.getHeight(x,z);
        if(noise > max) {
            System.out.println(noise);
            max = noise;
        }
        //noise += humidity.noise(x / size, z / size) + 1;
        return noise;
    }

    public Biome getBiome(int x, int z) {
        double noise = perlinNoise.getHeight(x,z) + 0.5;
        double temp = tempNoise.getHeight(x,z) + 0.5;
        double humidity = tempNoise.getHeight(x,z) + 0.5;


        return Biomes.getBiome(noise,temp,humidity);
    }





}
