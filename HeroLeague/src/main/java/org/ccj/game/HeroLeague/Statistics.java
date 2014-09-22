package org.ccj.game.HeroLeague;

/**
 * Created by Allen on 2014/9/22.
 */
public class Statistics {
    public int doubleSuccessCount = 0;                                  //
    public int threeSuccessCount = 0;
    public int fourSuccessCount = 0;
    public int fiveSuccessCount = 0;
    public int maxLastSuccessCount = 0;
    public int successCount = 0;
    public int failureCount = 0;

    private static Statistics gSt = null;

    public static Statistics getInstance() {
        if (null == gSt) {
            gSt = new Statistics();
        }
        return gSt;
    }

    public void resetData() {
        doubleSuccessCount = 0;
        threeSuccessCount = 0;
        fourSuccessCount = 0;
        fiveSuccessCount = 0;
        maxLastSuccessCount = 0;
        successCount = 0;
        failureCount = 0;
    }
}
