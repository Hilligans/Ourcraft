package Hilligans;

import Hilligans.Client.*;
import Hilligans.Client.Rendering.NewRenderer.BlockModel;
import Hilligans.Client.Rendering.World.Managers.VertexManagers.CubeManager;
import Hilligans.Util.Vector5f;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class ClientMain {

    public static Client client;

    public static Client getClient() {
        return client;
    }


    public static void main(String[] args) {


        int rotX = 0;
        int rotY = 2;
        int side = 0;
        for(int x = 0; x < 4; x++) {
            System.out.println(rotX | rotY << 2 | side << 4);
            rotX++;
         //   System.out.println(Integer.toBinaryString(rotX | rotY << 2 | side << 4));
        }


        JSONObject jsonObject = new JSONObject();

        for(int x = 0; x < 6; x++) {
            JSONArray vertices = new JSONArray();
            JSONArray indices = new JSONArray();

            Vector5f[] vector5fs = CubeManager.getHorizontalSlabVertices(null, x,1.0f,0.0f);
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

        System.out.println(rotX >> 2 | rotY);

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(0).put(4).put(5).put(6).put(7).put(8);


        jsonObject.put("modelRotations",jsonArray);
        System.out.println(jsonObject.toString());
        try {
            new BlockModel(jsonObject.toString());
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
