package dev.hilligans.ourcraft.world.structure;

import org.json.JSONObject;

public class WFCPlaceOnTopSegment implements IWFCStructureSegment {

    public String[] connections;

    @Override
    public WFCPlaceOnTopSegment connects(String... connectionNames) {
        this.connections = connectionNames;
        return this;
    }

    @Override
    public String getType() {
        return "place_on_top";
    }

    @Override
    public void encode(JSONObject jsonObject) {

    }

    @Override
    public void decode(JSONObject jsonObject) {

    }

    @Override
    public WFCStructureBuilder.Builder build() {
        return null;
    }
}
