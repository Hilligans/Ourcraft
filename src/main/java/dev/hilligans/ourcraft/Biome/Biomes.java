package dev.hilligans.ourcraft.Biome;

import dev.hilligans.ourcraft.Block.Blocks;
import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Data.Other.BlockTemplate;
import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.Util.CurvedRay;
import dev.hilligans.ourcraft.Util.CurvedRayEngine;
import dev.hilligans.ourcraft.World.Builders.Foliage.CactusBuilder;
import dev.hilligans.ourcraft.World.Builders.Foliage.CustomTreeBuilder;
import dev.hilligans.ourcraft.World.Builders.Foliage.TreeBuilder;
import org.joml.Vector2d;

import java.util.ArrayList;

public class Biomes {

    public static final Biome PLAINS = new Biome("plains", new TreeBuilder("oak_tree").setChance(5), new CustomTreeBuilder(Blocks.SAND).setChance(10).setFrequency(1), new CustomTreeBuilder(Blocks.WILLOW_LOG){
        @Override
        public void build(BlockPos startPos) {
            if(!isPlacedOn(startPos,Blocks.DIRT) && !isPlacedOn(startPos, Blocks.GRASS)) {
                return;
            }
            this.height = 15;
            baseSize = 2;
            buildStem(startPos,new Vector2d(),height, Blocks.WILLOW_LOG);
            buildSphereLeaves(startPos.copy().add(0,height,0),5,3,Blocks.LEAVES);
            buildRoots(startPos, Blocks.WILLOW_LOG);

            for(int x = 0; x < 8; x++) {
                buildSphereLeaves(buildBranch(startPos.copy().add(0, (int) (height / 2 * random.nextFloat() + height / 2),0),8,new Vector2d(random.nextFloat() * 75,random.nextFloat() * 360),Blocks.WILLOW_LOG),5,3,Blocks.LEAVES);
            }
        }
    }.setChance(8), new CustomTreeBuilder(Blocks.SAND){
        @Override
        public void build(BlockPos startPos) {
            if (!isPlacedOn(startPos, Blocks.DIRT) && !isPlacedOn(startPos, Blocks.GRASS)) {
                return;
            }
            this.height = 15;
            baseSize = 1;
            int heightRatio = height / baseSize;
            BlockTemplate blockTemplate = getTemplate(baseSize);
            for (int y = 0; y < heightRatio; y++) {
                blockTemplate.placeTemplate(world, new BlockPos(startPos.x, y + startPos.y, startPos.z), Blocks.SPRUCE_LOG);
                if (y > 4) {
                    BlockTemplate leaveTemplate = getTemplate(50 / y);
                    leaveTemplate.placeTemplateOnAirChanced(world, startPos.copy().add(0, y, 0), Blocks.LEAVES, random, 2,1);
                }
            }
            BlockTemplate leaveTemplate = getTemplate(50 / height);
            leaveTemplate.placeTemplateOnAirChanced(world, startPos.copy().add(0, height, 0), Blocks.LEAVES, random, 2,1);
        }
    }.setChance(8), new CustomTreeBuilder(Blocks.WILLOW_LOG) {
        @Override
        public void build(BlockPos startPos) {
            if(!isPlacedOn(startPos,Blocks.DIRT) && !isPlacedOn(startPos, Blocks.GRASS)) {
                return;
            }
            int height = (int) (Math.random() * 80 + 100);
            for(int x = 0; x < height; x++) {
                getTemplate((int) Math.ceil((height - x) / 7f)).placeTemplateOnAir(world,new BlockPos(startPos.x, x + startPos.y, startPos.z), Blocks.WILLOW_LOG);
            }
        }
    }.setChance(100), new CustomTreeBuilder(Blocks.WILLOW_LOG) {
        @Override
        public void build(BlockPos startPos) {
            if (!isPlacedOn(startPos, Blocks.DIRT) && !isPlacedOn(startPos, Blocks.GRASS)) {
                return;
            }
            double rot = Math.PI * 2 / 4f;
            for(int x = 0; x < 4; x++) {
                BlockTemplate template = STEM_5;
                double c = Math.cos(rot * x);
                double s = Math.sin(rot * x);
                CurvedRayEngine curvedRayEngine = new CurvedRayEngine((rotation, integer, pos) -> {
                    pos.add(5, 0, 0);
                    rotation = rotation;
                    template.placeTemplateOnAir(world, pos.copy(),startPos, Blocks.LOG, rotation,c,s);
                }, 40, 20, 20, new CurvedRay(), 0);
                curvedRayEngine.run();
            }
        }
    }.setChance(100)).setParams(0,0.3f,0,1,0);


    public static final Biome DESERT = new Biome("desert", new CactusBuilder("cactus_builder").setFrequency(7)).setSurfaceBlock(Blocks.SAND).setUnderBlock(Blocks.SAND).setParams(0,-0.5f,0,0,0);
    public static final Biome FOREST = new Biome("forest", new TreeBuilder("forest_tree").setFrequency(2)).setParams(0.2f,0.0f,0,1.2f,0);
    public static final Biome SANDY_HILLS = new Biome("sandy_hills");
 //   public static final Biome MOUNTAIN = new Biome("mountain").setParams(-0.2f,-0.2f,0.5f,0,0.6f).setHeight(new Vector3i(0,-5,50));
 //   public static final Biome MOUNTAIN_EDGE = new Biome("mountain_edge").setParams(-0.2f,-0.2f,0.5f,0,0.3f).setHeight(new Vector3i(0,-5,25));
    //public static final Biome DESERT1 = new Biome("desert1").setSurfaceBlock(Blocks.PHIL);


    private static final float noiseFactor = 1;
    private static final float tempFactor = 1;
    private static final float humidityFactor = 1;
    private static final float randFactor = 1;
    private static final float variationFactor = 1;

    public static Biome getBiome(GameInstance gameInstance, double noise, double temp, double humidity, double rand, double variation) {
        ArrayList<Double> doubles = new ArrayList<>(gameInstance.BIOMES.ELEMENTS.size());


        for(Biome biome : gameInstance.BIOMES.ELEMENTS) {
            doubles.add(Math.abs(noise - biome.noise) * noiseFactor + Math.abs(temp - biome.temp) * tempFactor + Math.abs(humidity - biome.humidity) * humidityFactor + Math.abs(rand - biome.rand) * randFactor + Math.abs(variation - biome.variation) * variationFactor);
        }

        Biome biome = PLAINS;
        double val = 1000000;
        for(int x = 0; x < gameInstance.BIOMES.ELEMENTS.size(); x++) {
          //  System.out.println(biomes.get(x).name + " " + doubles.get(x));
            if(val > doubles.get(x)) {
                biome = gameInstance.BIOMES.get(x);
                val = doubles.get(x);
            }
        }
        //System.out.println(biome.name);
        return biome;
    }


}
