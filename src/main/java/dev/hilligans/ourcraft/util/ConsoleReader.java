package dev.hilligans.ourcraft.util;

import java.util.Scanner;
import java.util.function.Consumer;

public class ConsoleReader extends Thread {

    Consumer<String> consoleEvent;

    public ConsoleReader(Consumer<String> consoleEvent) {
        this.consoleEvent = consoleEvent;
        this.start();
    }

    @Override
    public void run() {
        Scanner scan = new Scanner(System.in);
        String line;
        while(!(line = scan.nextLine()).equals(""+((char) 3))) {
            consoleEvent.accept(line);
        }
    }
}
