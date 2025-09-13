package dev.hilligans.ourcraft.server;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.engine.network.Protocol;
import dev.hilligans.engine.network.engine.NetworkEntity;
import dev.hilligans.engine.authentication.IAccount;
import dev.hilligans.ourcraft.util.ConsoleReader;
import dev.hilligans.engine.util.IByteArray;
import dev.hilligans.ourcraft.world.newworldsystem.IServerWorld;

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
    public Iterable<IServerWorld> getWorlds() {
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
        return null;
        //return Commands.executeCommand(command, null);
    }

    @Override
    public void sendPacket(Protocol matchingProtocol, IByteArray array) {

    }

    @Override
    public ServerPlayerData loadPlayer(String player, NetworkEntity entity) {
        return null;
    }

    @Override
    public GameInstance getGameInstance() {
        return null;
    }

    @Override
    public void stop() {

    }

    @Override
    public IAccount<?> authenticate(String scheme, String username, IByteArray data) {
        return null;
    }
}
