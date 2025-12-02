package dev.hilligans.engine.client.lwjgl;

import org.lwjgl.stb.STBDXT;
import org.lwjgl.system.Library;
//import org.lwjgl.system.ffm.*;

import java.lang.foreign.*;
import java.lang.invoke.*;
import java.util.*;
import java.lang.foreign.Arena;
import java.lang.foreign.SymbolLookup;
import java.util.Objects;

public class STBInterface {
    static final SymbolLookup STB = SymbolLookup.libraryLookup(
            Objects.requireNonNull(Library.loadNative(STBDXT.class, "org.lwjgl.sbt", "lwjgl_stb", true).getPath()),
            Arena.global()
    );

    static {

/*
        ffmConfig(
                YGMeasureFuncTest.class,
                ffmConfigBuilder(MethodHandles.lookup())
                        .withNullableAnnotation(Nullable.class)
                        .withSymbolLookup(YOGA)
                        .build()
        );
    }

 */

    }
}
