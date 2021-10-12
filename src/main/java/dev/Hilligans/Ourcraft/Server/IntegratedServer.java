package dev.Hilligans.Ourcraft.Server;

import dev.Hilligans.Ourcraft.Command.CommandExecutors.ConsoleExecutor;
import dev.Hilligans.Ourcraft.Command.Commands;
import dev.Hilligans.Ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.Hilligans.Ourcraft.Network.PacketBase;
import dev.Hilligans.Ourcraft.World.ServerWorld;
import dev.Hilligans.Ourcraft.World.World;
import dev.Hilligans.Ourcraft.Util.ConsoleReader;
import dev.Hilligans.Ourcraft.Util.Settings;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IntegratedServer implements IServer {

    public long time = 0;
    public Int2ObjectOpenHashMap<World> worlds = new Int2ObjectOpenHashMap<>();

    public void startServer( ) {
        Server server = new Server(this);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(server, 0, 40, TimeUnit.MILLISECONDS);
        ConsoleReader consoleReader = new ConsoleReader(this::executeCommand);
    }

    @Override
    public void addWorld(int id, World world) {
        ((ServerWorld)world).server = this;
        world.generateChunk(0,0);
        for(int x = -Settings.renderDistance; x < Settings.renderDistance; x++) {
            for(int z = -Settings.renderDistance; z < Settings.renderDistance; z++) {
                world.generateChunk( x,z);
            }
        }
        worlds.put(id,world);
    }

    @Override
    public World getWorld(int id) {
        return worlds.get(id);
    }

    @Override
    public Collection<World> getWorlds() {
        return worlds.values();
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }

    public Object executeCommand(String command) {
        if(!command.startsWith("/")) {
            command = "/" + command;
        }
        return Commands.executeCommand(command,new ConsoleExecutor(this));
    }

    public World getDefaultWorld() {
        return worlds.values().iterator().next();
    }

    @Override
    public void sendPacket(PacketBase packetBase) {
       // PacketData packetData = new PacketData(packetBase);
       // PacketBase packet = packetData.createPacket();
       // packet.handle();
    }

    @Override
    public void sendPacket(PacketBase packetBase, PlayerEntity playerEntity) {
        sendPacket(packetBase);
    }
}
