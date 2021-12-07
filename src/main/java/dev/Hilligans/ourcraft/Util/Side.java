package dev.Hilligans.ourcraft.Util;

public enum Side {

    CLIENT("client"),
    SERVER("server"),
    COMMON("common");

    Side(String name) {
        this.name = name;
    }

    public String name;
}
