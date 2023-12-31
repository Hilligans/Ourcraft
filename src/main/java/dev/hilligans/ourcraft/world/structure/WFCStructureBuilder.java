package dev.hilligans.ourcraft.world.structure;

public class WFCStructureBuilder implements IStructureBuilder {


    public Builder builder(String name) {
        return new Builder(name);
    }

    @Override
    public String getResourceName() {
        return "wave_function_collapse";
    }

    @Override
    public String getResourceOwner() {
        return "ourcraft";
    }

    @Override
    public String getUniqueName() {
        return "structure_builder.ourcraft.wave_function_collapse";
    }

    @Override
    public IStructureTemplate parse(String input) {
        return null;
    }

    public static class Builder {

        public WFCStructureTemplate template;

        public Builder(String name) {
            template = new WFCStructureTemplate(name);
        }

        public WFCPlaceOnTopSegment addPlaceOnTopSegment() {
            WFCPlaceOnTopSegment segment = new WFCPlaceOnTopSegment();

            return null;
        }
    }
}
