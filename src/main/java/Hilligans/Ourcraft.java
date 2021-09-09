package Hilligans;

import Hilligans.ModHandler.Content.ContentPack;
import Hilligans.ModHandler.Content.ModContent;
import Hilligans.ModHandler.EventBus;
import Hilligans.ModHandler.ModLoader;
import Hilligans.Resource.ResourceManager;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class Ourcraft {

    public static final EventBus EVENT_BUS = new EventBus();
    public static final ModLoader MOD_LOADER = new ModLoader();
    public static final Logger LOGGER = Logger.getGlobal();
    public static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    public static final ResourceManager RESOURCE_MANAGER = new ResourceManager();
    public static final ModContent OURCRAFT = new ModContent("ourcraft").addClassLoader(new URLClassLoader(new URL[]{Ourcraft.class.getProtectionDomain().getCodeSource().getLocation()}));
    public static final ContentPack CONTENT_PACK = new ContentPack();
    public static final AtomicBoolean REBUILDING = new AtomicBoolean(false);

    public static String path = System.getProperty("user.dir");

    public static String hashString(String password, String salt) {
        return new String(BCrypt.withDefaults().hash(12,"abcdefghjklmmopq".getBytes(), (password + salt).getBytes()), StandardCharsets.UTF_8);
    }

    public static ResourceManager getResourceManager() {
        return RESOURCE_MANAGER;
    }

    public static File getFile(String path) {
        return new File(path + "/" + path);
    }


    static {
        CONTENT_PACK.mods.put("ourcraft",OURCRAFT);
    }

    public static void register() { }
}
