package dev.hilligans.ourcraft.Addons.Bot.PathFinding;

import dev.hilligans.ourcraft.Addons.Bot.BotInstance;
import dev.hilligans.ourcraft.Data.Other.BlockPos;

public interface IPathfinder {

    Path findPath(BotInstance bot, BlockPos startPos, BlockPos endPos, boolean canMine);

}
