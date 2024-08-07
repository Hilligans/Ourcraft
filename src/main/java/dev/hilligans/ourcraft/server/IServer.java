package dev.hilligans.ourcraft.server;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.ourcraft.entity.living.entities.PlayerEntity;
import dev.hilligans.ourcraft.mod.handler.events.server.ServerTickEvent;
import dev.hilligans.ourcraft.network.PacketBase;
import dev.hilligans.ourcraft.network.ServerNetworkHandler;
import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.world.newworldsystem.IServerWorld;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;

public interface IServer {


    void addWorld(IServerWorld world);
    IServerWorld getWorld(ServerPlayerData serverPlayerData);

    long getTime();

    void setTime(long time);

    void tick();

    Object executeCommand(String command);

    ServerNetworkHandler getServerNetworkHandler();

    void sendPacketToAllVisible(PacketBase<?> packet, long x, long y, long z, IWorld serverWorld);

    void sendPacket(PacketBase<?> packetBase);

    void sendPacket(PacketBase<?> packetBase, PlayerEntity playerEntity);

    void sendPacket(PacketBase<?> packetBase, ServerPlayerData playerData);

    GameInstance getGameInstance();

    void stop();

    class Server implements Runnable {
        public IServer server;
        //public IGameProcessor gameProcessor = new TE2GameProcessor(new TickEngineSettings());
        public Server(IServer server) {
            this.server = server;
        }
        @Override
        public void run() {
            server.setTime(server.getTime() + 1);
            Ourcraft.GAME_INSTANCE.EVENT_BUS.postEvent(new ServerTickEvent(server));
            server.tick();
           // gameProcessor.tickServer(server);
            //for(World world : server.getWorlds()) {
            //    world.tick();
            //}
        }
    }
}



