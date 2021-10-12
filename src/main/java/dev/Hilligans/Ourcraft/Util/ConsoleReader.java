package dev.Hilligans.Ourcraft.Util;

import java.io.Console;
import java.util.Scanner;

public class ConsoleReader extends Thread {

    ConsoleEvent consoleEvent;

    public ConsoleReader(ConsoleEvent consoleEvent) {
        this.consoleEvent = consoleEvent;
        this.start();
    }

    @Override
    public void run() {
        Scanner scan = new Scanner(System.in);
        while (scan.hasNext()) {
            consoleEvent.invoke(scan.nextLine());
        }
    }

    public interface ConsoleEvent {
        void invoke(String message);
    }
}
