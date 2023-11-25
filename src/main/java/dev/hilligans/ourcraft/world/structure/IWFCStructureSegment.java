package dev.hilligans.ourcraft.world.structure;

import org.json.JSONObject;

public interface IWFCStructureSegment {

    IWFCStructureSegment connects(String... connectionNames);

    String getType();

    void encode(JSONObject jsonObject);

    void decode(JSONObject jsonObject);

    public WFCStructureBuilder.Builder build();

}
