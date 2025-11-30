package dev.hilligans.engine2d.gametypes;

import dev.hilligans.engine.gametype.GameType;
import dev.hilligans.engine2d.Engine2D;

public class TopDown2DGameType extends GameType {

    public static final TopDown2DGameType instance = new TopDown2DGameType();

    public TopDown2DGameType() {
        super("top_down_2d");
    }

    @Override
    public String getResourceOwner() {
        return Engine2D.MOD_ID;
    }
}
