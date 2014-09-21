package org.ccj.game.HeroLeague;

import org.ccj.Director;
import org.ccj.audio.AudioEngine;
import org.ccj.d2.Node;
import org.ccj.d2.Sprite;
import org.ccj.d2.action.MoveTo;
import org.ccj.d2.action.ScaleTo;
import org.ccj.d2.action.Sequence;
import org.ccj.math.Vec2;

/**
 * Created by Allen on 2014-9-18.
 */
public class Check {
    Sprite sprite;
    int id;

    public static final int CHECK_SIZE_H = 80;
    public static final int CHECK_SIZE_V = 80;
    public static final int SPLIT_SIZE = 3;
    public static final int CHECK_REAL_SIZE_H = 40;
    public static final int CHECK_REAL_SIZE_V = 40;

    public static final float CHECK_SCALE_SIZE = (CHECK_SIZE_H-2.0f)/CHECK_REAL_SIZE_H;

    public static final String MOVE_SOUND = "sound/landing.wav";
    public static final int CHECK_TOTAL_COUNT = 255;

    private Check() {}

    /**
     * 新建Check
     * @param parent 父节点
     * @param position 坐标
     * @param id id
     */
    public Check(Node parent,Vec2 position, int id) {
        this.id = id;
        this.sprite = newSprite(id);
        parent.addChild(this.sprite);
        this.sprite.setPosition(position);
    }

    /**
     * 切换Check精灵
     * @param parent 父节点
     * @param position 坐标
     * @param id id
     */
    public void changeId(Node parent, Vec2 position, int id) {
        parent.removeChild(this.sprite);
        this.id = id;
        this.sprite = newSprite(id);
        parent.addChild(this.sprite);
        this.sprite.setPosition(position);
    }

    /**
     * 新的精灵
     * @param id
     * @return
     */
    private Sprite newSprite(int id) {
        String fileName = this.id + "-40.png";
        Sprite spriteTemp = ResourceMng.getInstance().getSpriteFromCache(fileName);
        spriteTemp.setScale(CHECK_SCALE_SIZE);
        return spriteTemp;
    }

    /**
     * 移动
     * @param aimPosition 移动目的地
     */
    public void moveTo(Vec2 aimPosition) {
        this.sprite.runAction(MoveTo.create(0.2f, aimPosition));
        AudioEngine.getInstance().playEffect(MOVE_SOUND);
    }

    /**
     * 闪烁
     */
    public void flash() {
        sprite.runAction(Sequence.create(
                ScaleTo.create(0, CHECK_SCALE_SIZE),
                ScaleTo.create(0.2f, CHECK_SCALE_SIZE * 1.2f),
                ScaleTo.create(0, CHECK_SCALE_SIZE)));
    }
}
