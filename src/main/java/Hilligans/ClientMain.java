package Hilligans;

import Hilligans.Client.*;
import Hilligans.Data.Other.ObjFile;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketFetcher;
import Hilligans.Network.Protocol;
import Hilligans.Network.Protocols;
import Hilligans.WorldSave.WorldLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

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
