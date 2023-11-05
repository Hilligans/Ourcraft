package dev.hilligans.ourcraft.Client;

import dev.hilligans.ourcraft.Data.Primitives.Tuple;

import java.util.ArrayList;

public class ChatMessages {


    private final int MESSAGE_LENGTH = 5000;
    public ArrayList<Tuple<String,Long>> messages = new ArrayList<>();
    public boolean typing = false;

    public void addMessage(String message) {
        Tuple<String,Long> tuple = new Tuple<>(message,System.currentTimeMillis() + MESSAGE_LENGTH);
        messages.add(tuple);
    }

    public String getString() {
        return "";
        //return messageIndex == -1 ? string : sentMessages.size() > messageIndex ? sentMessages.get(sentMessages.size() - messageIndex - 1) : "";
    }
}
