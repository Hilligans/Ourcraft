package Hilligans;

import Hilligans.ModHandler.EventBus;
import Hilligans.ModHandler.Mod;
import Hilligans.ModHandler.ModLoader;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class Ourcraft {

    public static final EventBus EVENT_BUS = new EventBus();
    public static final ModLoader MOD_LOADER = new ModLoader();

    public static String path = System.getProperty("user.dir");

    public static String hashString(String password, String salt) {
        return new String(BCrypt.withDefaults().hash(12,"abcdefghjklmmopq".getBytes(), (password + salt).getBytes()), StandardCharsets.UTF_8);
    }
}
