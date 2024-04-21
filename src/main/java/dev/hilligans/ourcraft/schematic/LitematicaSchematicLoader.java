package dev.hilligans.ourcraft.schematic;

import dev.hilligans.ourcraft.resource.loaders.ResourceLoader;
import dev.hilligans.ourcraft.tag.CompoundNBTTag;

import java.nio.ByteBuffer;

public class LitematicaSchematicLoader extends ResourceLoader<Schematic> {

    public LitematicaSchematicLoader() {
        super("litematica_schematic_loader", "schematic_loader");
    }


    //TODO finish
    @Override
    public Schematic read(ByteBuffer buffer) {
        try {
            CompoundNBTTag compoundTag = toCompoundTag(buffer);
            if(compoundTag.getInt("Version") != 5) {
                throw new RuntimeException("Tried to load a litematica schematic with unknown version " + compoundTag.getInt("Version"));
            }

            CompoundNBTTag metaData = compoundTag.getCompoundTag("Metadata");
            Schematic schematic = new Schematic(metaData.getString("Name"));
            schematic.withAuthor(metaData.getString("Author"));

            CompoundNBTTag regions = compoundTag.getCompoundTag("Regions");

            //TODO fix for multiple regions
            CompoundNBTTag region = regions.getFirstCompoundTag();
            CompoundNBTTag position = region.getCompoundTag("Position");
            CompoundNBTTag size = region.getCompoundTag("Size");

            schematic.withPosition(position.getInt("x"), position.getInt("y"), position.getInt("z"));
            schematic.withSize(size.getInt("x"), size.getInt("y"), size.getInt("z"));

            return schematic;
        } catch (Exception e) {}
        return null;
    }

    @Override
    public ByteBuffer write(Schematic schematic) {
        return null;
    }
}
