package dev.hilligans.engine.gametype;

public abstract class GameType implements IGameType {

    public final String name;

    public GameType(String name) {
        this.name = name;

        track();
    }

    @Override
    public String getResourceName() {
        return name;
    }
}
