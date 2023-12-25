package dev.hilligans.ourcraft.util.sections;

public interface ISection {

    SectionView startSection(String name);

    void stopSection(String name);

    static ISection getSection(String name) {
        if("stepping".equals(name)) {
            return new SteppingSection();
        } else if ("profiled".equals(name)) {
            return new ProfiledSection();
        }
        return new EmptySection();
    }
}
