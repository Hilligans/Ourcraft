package Hilligans;

import Hilligans.Client.*;
import Hilligans.Client.Rendering.NewRenderer.ImageLocation;
import Hilligans.Client.Rendering.NewRenderer.TextAtlas;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

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
