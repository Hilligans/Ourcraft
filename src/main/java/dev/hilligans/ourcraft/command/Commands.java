package dev.hilligans.ourcraft.command;


import dev.hilligans.engine.command.CommandBuilder;
import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.engine.command.executors.CommandExecutor;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.network.packet.SSendChunkPacket;

public class Commands {

    public static void register(ModContainer mod) {
        mod.registerCommands(
                CommandBuilder.create("stop").build(Commands::stop),

                CommandBuilder.create(PlayerExecutor.class, "setBlock")
                        .withXYZ()
                        .withElement(Block.class)
                        .build(Commands::setBlock));
    }

    public static void setBlock(PlayerExecutor playerExecutor, int x, int y, int z, Block block) {
        System.out.println("Setting Block");
        playerExecutor.getWorld().setBlockState(x, y, z, block.getDefaultState());
        SSendChunkPacket.send(playerExecutor.getPlayerData().networkEntity, playerExecutor.getWorld().getChunk(x, y, z));
    }

    public static void stop(CommandExecutor executor) {
        System.out.println("stop");
    }
}
