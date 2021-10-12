package dev.Hilligans.ourcraft.ModHandler.Events.Client;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.ModHandler.Event;

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
