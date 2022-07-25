package dev.Hilligans.ourcraft.Addons.Bot.PathFinding;

import dev.Hilligans.ourcraft.Addons.Bot.BotInstance;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;

public interface IPathfinder {

    Path findPath(BotInstance bot, BlockPos startPos, BlockPos endPos, boolean canMine);

}
