package dev.hilligans.ourcraft.ModHandler.Events.Client;

import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.ModHandler.Event;

public class RenderStartEvent extends Event {

    public MatrixStack worldStack;
    public MatrixStack screenStack;
    public Client client;

    public RenderStartEvent(MatrixStack worldStack, MatrixStack screenStack, Client client) {
        this.worldStack = worldStack;
        this.screenStack = screenStack;
        this.client = client;
    }


}
