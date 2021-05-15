package Hilligans.Command;

import Hilligans.Entity.Entity;

import java.util.ArrayList;

public abstract  class CommandHandler {

    public CommandHandler(String command) {
        Commands.commands.put("/" + command,this);
    }

    public CommandHandler addAlias(String alias) {
        Commands.commands.put("/" + alias,this);
        return this;
    }

    public abstract Object handle(Entity executor, String[] args);

    public static boolean isNumber(String arg) {
        try {
            Float.parseFloat(arg);
            return true;
        } catch (Exception ignored) {}
        return false;
    }

    public ArrayList<Entity> processSelector(String selector) {

        return null;
    }

    public Entity processSelectorSingle(String selector) {

    }

}
