package org.ccj.game.HeroLeague;

import org.ccj.Event;
import org.ccj.Touch;
import org.ccj.d2.Label;
import org.ccj.editor.cce.Bind;
import org.ccj.editor.cce.NodeController;

/**
 * Created by Allen on 2014-9-22.
 */
public class OverController extends NodeController{

    @Bind("scoreLabel")
    public Label scoreLabel;

    @Bind("doubleLabel")
    public Label doubleLabel;

    @Bind("threeLabel")
    public Label threeLabel;

    @Bind("fourLabel")
    public Label fourLabel;

    @Bind("fiveLabel")
    public Label fiveLabel;


    @Override
    public void onExit() {
        super.onExit();
    }

    @Override
    public void onTouchEnded(Touch touch, Event event) {
        super.onTouchEnded(touch, event);
    }

    @Override
    public boolean onTouchBegan(Touch touch, Event event) {
        return super.onTouchBegan(touch, event);
    }

    @Override
    public void onEnter() {
        super.onEnter();

        setTouchEnabled(true);
        setTouchMode(Touch.MODE_ONE_BY_ONE);

        scoreLabel.setString("score:" + Statistics.getInstance().successCount);
        doubleLabel.setString("double:" + Statistics.getInstance().doubleSuccessCount);
        threeLabel.setString("three:" + Statistics.getInstance().threeSuccessCount);
        fourLabel.setString("four:" + Statistics.getInstance().fourSuccessCount);
        fiveLabel.setString("five:" + Statistics.getInstance().fiveSuccessCount);
    }
}
