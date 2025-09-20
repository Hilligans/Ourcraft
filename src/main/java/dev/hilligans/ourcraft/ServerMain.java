package dev.hilligans.ourcraft;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.mod.handler.pipeline.standard.StandardPipeline;
import dev.hilligans.ourcraft.server.IServer;
import dev.hilligans.ourcraft.server.MultiPlayerServer;
import dev.hilligans.engine.authentication.IAuthenticationScheme;
import dev.hilligans.ourcraft.util.Profiler;
import dev.hilligans.ourcraft.util.Settings;
import dev.hilligans.engine.util.Side;
import dev.hilligans.engine.util.argument.Argument;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ServerMain {

    public static MultiPlayerServer server;
   // public static ArgumentContainer argumentContainer;


    public static void main(String[] args) {

    }


    public static IServer server(GameInstance gameInstance) {
        System.out.println("Starting server...");
        gameInstance.builtSemaphore.acquireUninterruptibly();
        gameInstance.builtSemaphore.release();
        //System.out.println("Authentication Schemes:" + Arrays.toString(authenticationSchemes.get(gameInstance)));

        gameInstance.THREAD_PROVIDER.map();


        gameInstance.THREAD_PROVIDER.unmap();
        return server;
    }

    public static MultiPlayerServer getServer() {
        return server;
    }

}
