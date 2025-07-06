package dev.hilligans.ourcraft.server;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.command.executors.ConsoleExecutor;
import dev.hilligans.ourcraft.command.Commands;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;
import dev.hilligans.ourcraft.network.PacketBase;
import dev.hilligans.ourcraft.network.ServerNetworkHandler;
import dev.hilligans.ourcraft.util.IByteArray;
import dev.hilligans.ourcraft.world.newworldsystem.IServerWorld;
import dev.hilligans.ourcraft.util.ConsoleReader;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IntegratedServer implements IServer {

    public long time = 0;
    public void startServer( ) {
        Server server = new Server(this);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(server, 0, 40, TimeUnit.MILLISECONDS);
        ConsoleReader consoleReader = new ConsoleReader(this::executeCommand);
    }

    /*@Override
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
     */

    @Override
    public void addWorld(IServerWorld world) {

    }

    @Override
    public IServerWorld getWorld(ServerPlayerData serverPlayerData) {
        return null;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public void tick() {

    }

    public Object executeCommand(String command) {
        if(!command.startsWith("/")) {
            command = "/" + command;
        }
        return Commands.executeCommand(command,new ConsoleExecutor(this));
    }

    @Override
    public ServerNetworkHandler getServerNetworkHandler() {
        return null;
    }

    @Override
    public void sendPacket(PacketBase<?> packetBase) {
       // PacketData packetData = new PacketData(packetBase);
       // PacketBase packet = packetData.createPacket();
       // packet.handle();
    }

    @Override
    public void sendPacket(PacketBase<?> packetBase, PlayerEntity playerEntity) {
        sendPacket(packetBase);
    }

    @Override
    public void sendPacket(IByteArray array) {

    }

    @Override
    public ServerPlayerData loadPlayer(String player) {
        return null;
    }

    @Override
    public GameInstance getGameInstance() {
        return null;
    }

    @Override
    public void stop() {

    }
}
