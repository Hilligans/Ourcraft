package Hilligans;

import Hilligans.Block.BlockTypes.ColorBlock;
import Hilligans.Client.*;
import Hilligans.ModHandler.Mod;
import Hilligans.ModHandler.ModLoader;

import java.io.File;
import java.util.Arrays;

@Mod
public class ClientMain {

    public static Client client;

    public static Client getClient() {
        return client;
    }


    public static void main(String[] args) {
        client = new Client();
        Ourcraft.MOD_LOADER.loadAllMods(new File("mods/"));
        client.startClient();
    }

    @Mod()
    public void a() {

    }

    public static int getWindowX() {
        return client == null ? 0 : client.windowX;
    }

    public static int getWindowY() {
        return client == null ? 0 : client.windowY;
    }
}
