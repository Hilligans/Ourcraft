package Hilligans.Command;

import Hilligans.Entity.Entity;

public abstract  class CommandHandler {

    public CommandHandler(String command) {
        Commands.commands.put("/" + command,this);
    }

    public CommandHandler addAlias(String alias) {
        Commands.commands.put(alias,this);
        return this;
    }

    public abstract String handle(Entity executor, String[] args);
}
