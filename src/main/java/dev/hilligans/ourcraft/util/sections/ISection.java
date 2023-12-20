package dev.hilligans.ourcraft.util.sections;

public interface ISection {

    SectionView startSection(String name);

    void stopSection(String name);

}
