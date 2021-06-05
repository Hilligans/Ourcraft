package Hilligans;

import Hilligans.ModHandler.EventBus;
import Hilligans.ModHandler.Mod;
import Hilligans.ModHandler.ModLoader;
import Hilligans.Resource.ResourceManager;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Ourcraft {

    public static final EventBus EVENT_BUS = new EventBus();
    public static final ModLoader MOD_LOADER = new ModLoader();
    public static final Logger LOGGER = Logger.getGlobal();
    public static final ExecutorService executor = Executors.newSingleThreadExecutor();
    public static final ResourceManager resourceManager = new ResourceManager();

    public static String path = System.getProperty("user.dir");

    public static String hashString(String password, String salt) {
        return new String(BCrypt.withDefaults().hash(12,"abcdefghjklmmopq".getBytes(), (password + salt).getBytes()), StandardCharsets.UTF_8);
    }

    public static ResourceManager getResourceManager() {
        return resourceManager;
    }

    public static File getFile(String path) {
        return new File(path + "/" + path);
    }
}
