package Hilligans;

import Hilligans.EventHandler.EventBus;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.nio.charset.StandardCharsets;

public class Ourcraft {

    public static final EventBus EVENT_BUS = new EventBus();

    public static String hashString(String password, String salt) {
        return new String(BCrypt.withDefaults().hash(12,"abcdefghjklmmopq".getBytes(), (password + salt).getBytes()), StandardCharsets.UTF_8);
    }
}
