package Hilligans;

import Hilligans.EventHandler.EventBus;
import at.favre.lib.crypto.bcrypt.BCrypt;

public class Ourcraft {

    public static final EventBus EVENT_BUS = new EventBus();

    public static String hashString(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
}
