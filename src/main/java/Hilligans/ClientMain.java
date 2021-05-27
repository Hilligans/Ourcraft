package Hilligans;

import Hilligans.Client.*;
import Hilligans.Client.Key.KeyBind;
import Hilligans.Client.Key.KeyPress;
import Hilligans.Client.Lang.Language;
import Hilligans.Client.Rendering.NewRenderer.BlockModel;
import Hilligans.Client.Rendering.World.Managers.VertexManagers.CubeManager;
import Hilligans.Client.Rendering.World.Managers.VertexManagers.PlantManager;
import Hilligans.Client.Rendering.World.StringRenderer;
import Hilligans.Data.Other.BlockShapes.XBlockShape;
import Hilligans.Util.Vector5f;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

public class ClientMain {

    public static Client client;

    public static Client getClient() {
        return client;
    }



    public static void main(String[] args) {
        client = new Client();
        client.startClient();

    }

    public static void handleArgs(String[] args) {
        for(String string : args) {
            if(string.length() >= 5 && string.startsWith("--path")) {
                Ourcraft.path = string.substring(5);
            }
        }
    }

    public static int getWindowX() {
        return client == null ? 0 : client.windowX;
    }

    public static int getWindowY() {
        return client == null ? 0 : client.windowY;
    }
}
