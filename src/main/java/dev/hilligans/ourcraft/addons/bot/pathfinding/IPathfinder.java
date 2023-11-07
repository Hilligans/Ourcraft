package dev.hilligans.ourcraft.addons.bot.pathfinding;

import dev.hilligans.ourcraft.addons.bot.BotInstance;
import dev.hilligans.ourcraft.data.other.BlockPos;

public interface IPathfinder {

    Path findPath(BotInstance bot, BlockPos startPos, BlockPos endPos, boolean canMine);

}
