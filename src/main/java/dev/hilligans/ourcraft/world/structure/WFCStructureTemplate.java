package dev.hilligans.ourcraft.world.structure;

import dev.hilligans.ourcraft.mod.handler.content.ModContent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class WFCStructureTemplate implements IStructureTemplate {

    public String name;
    public ModContent source;
    public ArrayList<IWFCStructureSegment> segments = new ArrayList<>();

    public WFCStructureTemplate(String name) {
        this.name = name;
    }

    @Override
    public String serialize() {
        JSONArray jsonArray = new JSONArray(segments.size());
        for(IWFCStructureSegment segment : segments) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", segment.getType());
            segment.encode(jsonObject);
            jsonObject.put(segment.getType(), jsonArray);
        }
        return jsonArray.toString();
    }

    @Override
    public void assignModContent(ModContent modContent) {
        this.source = modContent;
    }

    @Override
    public String getResourceName() {
        return "wave_function_collapse";
    }

    @Override
    public String getResourceOwner() {
        return source.getModID();
    }
}
