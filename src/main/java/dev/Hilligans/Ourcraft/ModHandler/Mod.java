package dev.Hilligans.Ourcraft.ModHandler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Mod {
     String modID() default "";
}
