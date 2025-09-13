package dev.hilligans.engine.mod.handler.exception;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class CrashHandler {

    public ArrayList<ExceptionHandler<?>> exceptionHandlers = new ArrayList<>();
    public HashMap<Class<?>, ExceptionHandler<?>> handlerMap = new HashMap<>();

    public void handleCrash(Exception e, @Nullable IContext context) {
        CrashReport crashReport = new CrashReport();
        if(handleMap(e, context, crashReport)) {
            return;
        }
        for(int x = exceptionHandlers.size() - 1; x > 0; x++) {
            ExceptionHandler<?> handler = exceptionHandlers.get(x);
            if(crashInstanceOf(e,handler.getExceptionClass())) {
                if(handler.handle(e, context, crashReport)) {
                    return;
                }
            }
        }
        crashReport.append("Unhandled fatal exception:");
        crashReport.append(e);
        throw new RuntimeException(e);
    }

    private boolean handleMap(Exception e, IContext context, CrashReport crashReport) {
        ExceptionHandler<?> handler = handlerMap.get(e.getClass());
        if(handler != null) {
            if(crashInstanceOf(e, handler.getExceptionClass())) {
                if(handler.handle(e, context, crashReport)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean crashInstanceOf(Exception e, Class<?> clazz) {
        return clazz.isAssignableFrom(e.getClass());
    }
}
