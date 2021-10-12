package dev.Hilligans.Ourcraft.ModHandler.Events.Client;

import dev.Hilligans.Ourcraft.Client.Client;
import dev.Hilligans.Ourcraft.Client.MatrixStack;
import dev.Hilligans.Ourcraft.ModHandler.Event;

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
