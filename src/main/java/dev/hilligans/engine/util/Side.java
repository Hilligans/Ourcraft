package dev.hilligans.engine.util;

public enum Side {

    CLIENT("client"),
    SERVER("server"),
    COMMON("common");

    Side(String name) {
        this.name = name;
    }

    public final String name;

    public boolean isClient() {
        return this != SERVER;
    }

    public boolean isServer() {
        return this != CLIENT;
    }

    public boolean equals(Side s) {
        if(s == null) {
            return false;
        }
        if(this.name.equals("common")) {
            return true;
        }
        return this == s;
    }

    public static Side parseSide(String s) {
        for(Side side : Side.values()) {
            if(side.name.equals(s)) {
                return side;
            }
        }
        throw new RuntimeException("Invalid side " + s);
    }

    @Override
    public String toString() {
        return name;
    }
}
