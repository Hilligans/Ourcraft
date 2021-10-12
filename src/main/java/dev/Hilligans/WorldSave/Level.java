package dev.Hilligans.WorldSave;

import dev.Hilligans.Block.Block;
import dev.Hilligans.Block.Blocks;
import dev.Hilligans.Data.Primitives.Tuplet;
import dev.Hilligans.Ourcraft;
import dev.Hilligans.Tag.CompoundTag;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.HashMap;
import java.util.Objects;

public class Level {

    public HashMap<String, Tuplet<Integer,Integer>> blocks = new HashMap<>();
    public Int2ObjectOpenHashMap<String> idToNames = new Int2ObjectOpenHashMap<>();
    public int version = 0;

    public Level() {
    }

    public Level(CompoundTag compoundTag) {
        read(compoundTag);
    }

    public int getBlockStateSize(String name) {
        return blocks.get(name).getTypeB();
    }

    public int getBlockStateSize(int id) {
        return getBlockStateSize(idToNames.get(id));
    }

    public int getBlockID(String name) {
        return blocks.get(name).getTypeA();
    }

    public Block getBlock(int id) {
        return Ourcraft.GAME_INSTANCE.MAPPED_BLOCKS.getOrDefault(idToNames.get(id),Blocks.AIR);
    }

    public int ensureHasBlock(Block block) {
        if(blocks.get(block.getName()) == null) {
            blocks.put(block.getName(),new Tuplet<>(blocks.size(),block.blockProperties.blockStateSize));
            idToNames.put(blocks.size() - 1,block.getName());
        }
        return getBlockID(block.getName());
    }

    public void read(CompoundTag compoundTag) {
        CompoundTag blocks = compoundTag.getCompoundTag("blocks");
        for(String string : blocks.tags.keySet()) {
            long val = blocks.getLong(string).val;
            this.blocks.put(string,new Tuplet<>((int)(val >> 32),(int)val));
            this.idToNames.put((int) (val >> 32),string);
        }
    }

    public void write(CompoundTag compoundTag) {
        CompoundTag blocks = new CompoundTag();
        compoundTag.putTag("blocks",blocks);
        for(String string : this.blocks.keySet()) {
            blocks.putLong(string, (long) this.blocks.get(string).getTypeA() << 32 | this.blocks.get(string).getTypeB());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Level level = (Level) o;
        return version == level.version && Objects.equals(blocks, level.blocks) && Objects.equals(idToNames, level.idToNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blocks, idToNames, version);
    }
}
