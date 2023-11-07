package dev.hilligans.ourcraft.util;

import java.util.HashMap;

public class ArgumentRegistry {

    public String modID;

    public HashMap<String, String> registeredArguments;

    public void registerCLIArgument(String argument, String description) {
        registeredArguments.put(argument, description);
    }
}
