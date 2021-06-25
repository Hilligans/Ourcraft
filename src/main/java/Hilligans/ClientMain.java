package Hilligans;

import Hilligans.Client.*;
import Hilligans.Data.Other.ObjFile;
import Hilligans.WorldSave.WorldLoader;

public class ClientMain {

    public static Client client;

    public static Client getClient() {
        return client;
    }



    public static void main(String[] args) {
        try {
           // System.out.println(new ObjFile(WorldLoader.readString("/Models/Blocks/3d_printer.obj")).toBlockModel().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


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
