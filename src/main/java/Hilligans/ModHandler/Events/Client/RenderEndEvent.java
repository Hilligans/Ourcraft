package Hilligans.ModHandler.Events.Client;

import Hilligans.Client.Client;
import Hilligans.Client.MatrixStack;
import Hilligans.ModHandler.Event;

public class RenderEndEvent extends Event {

    public MatrixStack worldStack;
    public MatrixStack screenStack;
    public Client client;

    public RenderEndEvent(MatrixStack worldStack, MatrixStack screenStack, Client client) {
        this.worldStack = worldStack;
        this.screenStack = screenStack;
        this.client = client;
    }
}
