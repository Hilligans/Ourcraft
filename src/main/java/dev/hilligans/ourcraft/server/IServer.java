package dev.hilligans.ourcraft.server;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.data.other.server.ServerPlayerData;
import dev.hilligans.engine.mod.handler.events.server.ServerTickEvent;
import dev.hilligans.engine.network.Protocol;
import dev.hilligans.engine.network.engine.NetworkEntity;
import dev.hilligans.engine.authentication.IAccount;
import dev.hilligans.engine.util.IByteArray;
import dev.hilligans.ourcraft.world.newworldsystem.IServerWorld;

public interface IServer {


    void addWorld(IServerWorld world);
    IServerWorld getWorld(ServerPlayerData serverPlayerData);
    Iterable<IServerWorld> getWorlds();

    long getTime();

    void setTime(long time);

    void tick();

    Object executeCommand(String command);

    void sendPacket(Protocol matchingProtocol, IByteArray array);

    ServerPlayerData loadPlayer(String player, NetworkEntity entity);

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

    default String getMOTD() {
        return "";
    }

    default String getVersion() {
        return "0";
    }

    IAccount<?> authenticate(String scheme, String username, IByteArray data);
}



