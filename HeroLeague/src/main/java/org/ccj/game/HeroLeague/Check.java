package org.ccj.game.HeroLeague;

import org.ccj.Director;
import org.ccj.audio.AudioEngine;
import org.ccj.d2.Sprite;
import org.ccj.d2.action.MoveTo;
import org.ccj.math.Vec2;

/**
 * Created by Allen on 2014-9-18.
 */
public class Check extends Sprite{
    int id;

    public static final String MOVE_SOUND = "landing.wav";
    public void moveTo(Vec2 aimPosition) {
        this.runAction(MoveTo.create(0.2f, aimPosition));
        AudioEngine.getInstance().playEffect(MOVE_SOUND);
    }
}
