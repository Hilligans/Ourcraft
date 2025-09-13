package dev.hilligans.engine.mod.handler.exception.loader;

import dev.hilligans.engine.mod.handler.content.ModContent;
import dev.hilligans.engine.mod.handler.exception.CrashReport;
import dev.hilligans.engine.mod.handler.exception.ExceptionHandler;
import dev.hilligans.engine.mod.handler.exception.IContext;
import org.jetbrains.annotations.Nullable;

public class LoaderExceptionHandler extends ExceptionHandler<Exception> {

    public LoaderExceptionHandler() {
        super(Exception.class);
    }

    @Override
    public boolean handleException(Exception exception, @Nullable IContext context, CrashReport crashReport) {
        if(context instanceof LoadingCrashContext crashContext) {
            //TODO update

            // gameInstance.MOD_LOADER.suspend();
            int maxNameLength = 0;
            int maxVersionLength = 0;
            int maxSourceLength = 0;
            for(String s : gameInstance.CONTENT_PACK.mods.keySet()) {
                ModContent mod = gameInstance.CONTENT_PACK.mods.get(s);
                maxNameLength = Math.max(s.length(),maxNameLength);

            }


            for(String s : gameInstance.CONTENT_PACK.mods.keySet()) {
                String state = gameInstance.CONTENT_PACK.modStates.getOrDefault(s, "X");
                if(crashContext.mod.equals(s)) {
                    state = "E";
                }
                crashReport.append("| ").append(state).append("  | ").append(rightpad(s,maxNameLength)).append(" |");
            }
            return true;
        }
        return false;
    }

    private static String rightpad(String text, int length) {
        return String.format("%-" + length + "." + length + "s", text);
    }

    @Override
    public String getResourceName() {
        return "loaderExceptionHandler";
    }

    @Override
    public String getResourceOwner() {
        return "ourcraft";
    }
}
