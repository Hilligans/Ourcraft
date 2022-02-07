package dev.Hilligans.ourcraft.Util;

public enum Side {

    CLIENT("client"),
    SERVER("server"),
    COMMON("common");

    Side(String name) {
        this.name = name;
    }

    public final String name;


    public boolean equals(Side s) {
        if(s == null) {
            return false;
        }
        if(this.name.equals("common")) {
            return true;
        }
        return this == s;
    }
}
