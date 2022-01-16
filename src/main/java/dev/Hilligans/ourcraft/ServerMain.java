package dev.Hilligans.ourcraft;

import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Server.MultiPlayerServer;
import dev.Hilligans.ourcraft.Util.Profiler;
import dev.Hilligans.ourcraft.Util.Settings;
import dev.Hilligans.ourcraft.Util.Side;
import dev.Hilligans.ourcraft.World.Builders.OreBuilder;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.ServerWorld;
import dev.Hilligans.ourcraft.World.World;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerMain {


    public static Profiler profiler;
    //public static ServerWorld world;

    public static MultiPlayerServer server;

    public static void main(String[] args) {
        Settings.isServer = true;
        GameInstance gameInstance = Ourcraft.GAME_INSTANCE;
        gameInstance.handleArgs(args);
        gameInstance.side = Side.SERVER;
        gameInstance.loadContent();
       // gameInstance.register("testt",gameInstance);

        ServerWorld world = new ServerWorld(gameInstance);
        world.worldBuilders.add(new OreBuilder(Blocks.GRASS,Blocks.STONE).setFrequency(20));

       // Ourcraft.CONTENT_PACK.releaseMod("test_mod");
        new Thread() {
            @Override
            public void run() {
                super.run();
               /* try {
                    Thread.sleep(10000);
                } catch (Exception e) {}
                System.out.println("running");
                long mili = System.currentTimeMillis();
                Chunk chunk = server.getDefaultWorld().getChunk(0,0);
                chunk.chunks.get(0).set(Blocks.GRASS.getDefaultState().blockId << 16 | 65535);
                AtomicInteger count = new AtomicInteger();
                world.chunkContainer.forEach(chunk1 -> {
                    chunk1.fastSet(chunk.chunks.get(0).vals);
                    count.getAndIncrement();
                });
                System.out.println((System.currentTimeMillis() - mili) + " Miliseconds to do " + (count.get() * 16*16*4096) + " Blocks");

                */
            }
        }.start();
        server = new MultiPlayerServer();
        server.addWorld(0,world);
        server.startServer("25586");

    }

    public static World getWorld(int id) {
        return server.worlds.get(id);
    }

    public static MultiPlayerServer getServer() {
        return server;
    }

    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }

}
