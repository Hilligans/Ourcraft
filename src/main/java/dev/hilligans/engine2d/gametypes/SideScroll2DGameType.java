package dev.hilligans.engine2d.gametypes;

import dev.hilligans.engine.gametype.GameType;
import dev.hilligans.engine2d.Engine2D;

public class SideScroll2DGameType extends GameType {

    public static final SideScroll2DGameType instance = new SideScroll2DGameType();

    public SideScroll2DGameType() {
        super("side_scroll_2d");
    }

    @Override
    public String getResourceOwner() {
        return Engine2D.MOD_ID;
    }
}
