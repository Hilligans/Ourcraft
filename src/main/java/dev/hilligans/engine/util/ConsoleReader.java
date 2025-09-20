package dev.hilligans.engine.util;

import java.util.Scanner;
import java.util.function.Consumer;

public class ConsoleReader extends Thread {

    Consumer<String> consoleEvent;

    public ConsoleReader(Consumer<String> consoleEvent) {
        this.consoleEvent = consoleEvent;
        setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        Scanner scan = new Scanner(System.in);
        String line;
        while(!(line = scan.next()).equals(""+((char) 3))) {
            consoleEvent.accept(line);
        }
    }
}
