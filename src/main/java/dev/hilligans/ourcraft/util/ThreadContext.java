package dev.hilligans.ourcraft.util;

import dev.hilligans.ourcraft.util.sections.EmptySection;
import dev.hilligans.ourcraft.util.sections.ISection;
import org.jetbrains.annotations.NotNull;

public class ThreadContext {

    protected ISection section = EmptySection.EMPTY_SECTION_INSTANCE;

    public ThreadContext() {
    }

    public ThreadContext setSection(@NotNull ISection section) {
        if(section == null) {
            throw new NullPointerException();
        }
        this.section = section;
        return this;
    }

    @NotNull
    public ISection getSection() {
        return section;
    }
}
