package Hilligans;

import Hilligans.Block.BlockTypes.ColorBlock;
import Hilligans.Client.*;
import Hilligans.Client.Rendering.NewRenderer.BlockVertices;
import Hilligans.Client.Rendering.World.Managers.VertexManagers.CubeManager;
import Hilligans.Data.Primitives.FloatList;
import Hilligans.ModHandler.Mod;
import Hilligans.ModHandler.ModLoader;
import Hilligans.Util.Vector5f;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientMain {

    public static Client client;

    public static Client getClient() {
        return client;
    }


    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();

        for(int x = 0; x < 6; x++) {
            JSONArray vertices = new JSONArray();
            JSONArray indices = new JSONArray();

            Vector5f[] vector5fs = CubeManager.getVertices1(null, x,1.0f);
            Integer[] integers = CubeManager.getIndices(x,0);

            for (Vector5f vector5f : vector5fs) {
                JSONArray vals = new JSONArray();
                for(float val : vector5f.values) {
                    //val += 0.5;
                    vals.put(val);
                }
                vertices.put(vals);
            }
            indices.putAll(integers);
            JSONObject newObject = new JSONObject();
            newObject.put("vertices", vertices);
            newObject.put("indices",indices);
            jsonObject.put(x + "",newObject);
        }
        System.out.println(jsonObject.toString());
        try {
            new BlockVertices(jsonObject.toString());
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }



        client = new Client();
        client.startClient();
    }

    public static int getWindowX() {
        return client == null ? 0 : client.windowX;
    }

    public static int getWindowY() {
        return client == null ? 0 : client.windowY;
    }
}
