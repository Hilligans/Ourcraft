package dev.Hilligans.ourcraft.Util;

import dev.Hilligans.ourcraft.Data.Other.BoundingBox;

public class Settings {

    public static int renderDistance = 8 ;
    public static boolean requestChunks = true;

    public static final int chunkHeight = 16;
    public static final int maxHeight = chunkHeight * 16;
    public static final int minHeight = 0;

    public static final int gameVersion = 3;

    public static final int maxFps = 20000;

    public static final boolean renderSameTransparent = false;
    public static boolean renderTransparency = true;

    public static final int playerInventorySize = 45;
    public static final int hotBarWidth = 9;

    public static float guiSize = 4.0f;

    public static BoundingBox playerBoundingBox = new BoundingBox(-0.35f,-1.9f,-0.35f,0.35f,0.0f,0.35f);

    public static String worldName = "world2";

    public static boolean isServer = false;
    public static boolean isOnlineServer = false;
    public static boolean forceDifferentName = false;

    public static boolean sounds = true;

    public static int tickingDistance = 5;

    public static boolean autoSave = false;

    public static boolean optimizeMesh = true;
    public static int destroyChunkDistance = renderDistance;



    public static boolean pullResourcesFromUnloadedMods = true;
    public static boolean loadModsWithoutInfo = true;
    public static boolean asyncModLoading = true;
    public static boolean cacheDownloadedMods = true;
    public static boolean storeServerModsIndividually = false;

    public static boolean asyncChunkBuilding = true;

    public static boolean asyncWorldSave = true;

    public static String getVersion() {
        return gameVersion + "";
    }

}
