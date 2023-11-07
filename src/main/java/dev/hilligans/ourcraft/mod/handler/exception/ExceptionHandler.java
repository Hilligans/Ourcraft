package dev.hilligans.ourcraft.mod.handler.exception;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import org.jetbrains.annotations.Nullable;

public abstract class ExceptionHandler<T extends Exception> implements IRegistryElement {
    
    public Class<T> exceptionClass;
    public GameInstance gameInstance;

    public ExceptionHandler(Class<T> exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public Class<T> getExceptionClass() {
        return exceptionClass;
    }

    public abstract boolean handleException(T exception, @Nullable IContext context, CrashReport crashReport);

    public boolean handle(Exception e, @Nullable IContext context, CrashReport crashReport) {
        return handleException((T)e, context, crashReport);
    }

    @Override
    public void load(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }
}