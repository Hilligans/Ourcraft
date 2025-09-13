package dev.hilligans.engine.client.input.handlers.controller;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

public class ControllerSettings {

    public long controllerID;

    public ControllerSettings(long id) {
        this.controllerID = id;
    }

    public float[] axesCutoff = new float[6];

    public String mappings;

    public boolean axesPressed(float value, int id) {
        return Math.abs(value) > axesCutoff[id];
    }

    public void setMappings(String mapping) {
        ByteBuffer buf = MemoryStack.stackASCII(mapping);
        GLFW.glfwUpdateGamepadMappings(buf);
    }
}
