package dev.Hilligans.ourcraft;

import dev.Hilligans.ourcraft.ModHandler.Content.ContentPack;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Resource.Loaders.ImageLoader;
import dev.Hilligans.ourcraft.Resource.Loaders.JsonLoader;
import dev.Hilligans.ourcraft.Resource.ResourceManager;
import dev.Hilligans.ourcraft.Util.NamedThreadFactory;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ourcraft {

    public static final GameInstance GAME_INSTANCE = new GameInstance();
    public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2,new NamedThreadFactory("random_executor"));

    public static String path = System.getProperty("user.dir");


    public static String hashString(String password, String salt) {
        return new String(BCrypt.withDefaults().hash(12,"abcdefghjklmmopq".getBytes(), (password + salt).getBytes()), StandardCharsets.UTF_8);
    }

    public static synchronized ResourceManager getResourceManager() {
        return GAME_INSTANCE.RESOURCE_MANAGER;
    }

    public static File getFile(String path) {
        return new File(path + "/" + path);
    }

    static {
        GAME_INSTANCE.loadContent();
    }

    public static void registerDefaultContent(ModContent modContent) {
        modContent.registerResourceLoaders(new JsonLoader(), new ImageLoader());
    }
}
