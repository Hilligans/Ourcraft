package dev.hilligans.ourcraft.Util.Usage;

public class StatisticsTracker implements IUsageTracker {


    @Override
    public String getResourceName() {
        return "statistics_tracker";
    }

    @Override
    public String getIdentifierName() {
        return "ourcraft:statistics_tracker";
    }

    @Override
    public String getUniqueName() {
        return "usage_tracker.ourcraft.statistics_tracker";
    }
}
