package dev.Hilligans.ourcraft.Server;

import dev.Hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.Hilligans.ourcraft.ModHandler.Events.Server.ServerTickEvent;
import dev.Hilligans.ourcraft.Network.PacketBase;
import dev.Hilligans.ourcraft.Ourcraft;
import dev.Hilligans.ourcraft.Server.TickEngine.IGameProcessor;
import dev.Hilligans.ourcraft.Server.TickEngine.TickEngineParts.TE2GameProcessor;
import dev.Hilligans.ourcraft.Server.TickEngine.TickEngineSettings;
import dev.Hilligans.ourcraft.World.World;

import java.util.Collection;

public interface IServer {


    void addWorld(int id, World world);

    World getWorld(int id);

    Collection<World> getWorlds();

    long getTime();

    void setTime(long time);

    Object executeCommand(String command);

    World getDefaultWorld();

    void sendPacket(PacketBase packetBase);

    void sendPacket(PacketBase packetBase, PlayerEntity playerEntity);

    class Server implements Runnable {
        public IServer server;
        public IGameProcessor gameProcessor = new TE2GameProcessor(new TickEngineSettings());
        public Server(IServer server) {
            this.server = server;
        }
        @Override
        public void run() {
            server.setTime(server.getTime() + 1);
            Ourcraft.GAME_INSTANCE.EVENT_BUS.postEvent(new ServerTickEvent(server));
            gameProcessor.tickServer(server);
            for(World world : server.getWorlds()) {
                world.tick();
            }
        }
    }

}



