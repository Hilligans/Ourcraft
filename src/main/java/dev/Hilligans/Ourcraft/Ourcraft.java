package dev.Hilligans.Ourcraft;

import dev.Hilligans.Ourcraft.Resource.ResourceManager;
import dev.Hilligans.Ourcraft.Util.NamedThreadFactory;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ourcraft {

    public static final GameInstance GAME_INSTANCE = new GameInstance();
    public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2,new NamedThreadFactory("random_executor"));

 /*   public static final EventBus EVENT_BUS = new EventBus();
    public static final ModLoader MOD_LOADER = new ModLoader();
    public static final Logger LOGGER = Logger.getGlobal();

    public static final ResourceManager RESOURCE_MANAGER = new ResourceManager();
    public static final ModContent OURCRAFT = new ModContent("ourcraft",GAME_INSTANCE).addClassLoader(new URLClassLoader(new URL[]{Ourcraft.class.getProtectionDomain().getCodeSource().getLocation()})).addMainClass(Ourcraft.class);
    public static final ContentPack CONTENT_PACK = new ContentPack();
    public static final AtomicBoolean REBUILDING = new AtomicBoolean(false);


  */
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
}
