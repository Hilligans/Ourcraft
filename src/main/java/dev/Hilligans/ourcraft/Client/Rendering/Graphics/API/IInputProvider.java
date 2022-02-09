package dev.Hilligans.ourcraft.Client.Rendering.Graphics.API;

public interface IInputProvider {

    // returns a bitmask of the supported inputs from the provider
    public static int KEYBOARD = 0x01;
    public static int MOUSE = 0x02;
    public static int CONTROLLER = 0x04;

    int getSupportedInputs();




}
