package dev.Hilligans.ourcraft.Schematic;

import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import org.joml.Vector3i;

public class Schematic {

    public BlockPos offset;
    public Vector3i size;
    public String name;
    public String author;


    public Schematic(String name) {
        this.name = name;
    }

    public Schematic withPosition(int x, int y, int z) {
        offset = new BlockPos(x,y,z);
        return this;
    }

    public Schematic withSize(int x, int y, int z) {
        size = new Vector3i(x,y,z);
        return this;
    }

    public Schematic withAuthor(String name) {
        this.author = name;
        return this;
    }






}
