package dev.hilligans.engine.network;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

public class PortUtil {

    public static final int MIN_PORT_NUMBER = 20000;
    public static final int MAX_PORT_NUMBER = 70000;

    public static int getOpenPort() throws Exception {
        int size = MAX_PORT_NUMBER - MIN_PORT_NUMBER;
        for(int x = 0; x < 100; x++) {
            int port = (int) (Math.random() * size + MIN_PORT_NUMBER);
            if(available(port)) {
                return port;
            }
        }
        for(int x = MIN_PORT_NUMBER;x < MAX_PORT_NUMBER;x++) {
            if(available(x)) {
                return x;
            }
        }
        throw new Exception("Failed to find open port, something is probably wrong");
    }

    public static boolean available(int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {}
            }
        }
        return false;
    }
}
