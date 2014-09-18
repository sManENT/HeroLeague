package org.ccj.game.HeroLeague;

import org.ccj.Director;
import org.ccj.base.Ref;
import org.ccj.d2.Sprite;
import org.ccj.d2.SpriteFrameCache;
import org.ccj.d2.action.ScaleTo;
import org.ccj.d2.action.Sequence;
import org.ccj.editor.cce.Action;
import org.ccj.editor.cce.Bind;
import org.ccj.editor.cce.NodeController;
import org.ccj.math.Vec2;

import java.util.Random;
import java.util.Vector;

/**
 */
public class SceneController extends NodeController {



    // 成员变量
    public static final int HEIGHT = 6;
    public static final int WIDTH = 10;

    Vector<Sprite> checks = new Vector<Sprite>();                      // 寻找的方格
    Vector<Sprite> checksBanner = new Vector<Sprite>();                // 第一行方格

    @Override
    public void onEnter() {
        super.onEnter();

        checks.removeAllElements();
        checksBanner.removeAllElements();

        initFaceCache();

        for (int x=0; x<WIDTH; x++) {
            for (int y=0; y<HEIGHT; y++) {
                long rand = rand(255);
                String fileName = rand + "-40.png";
                Sprite sprite = getSpriteFromFaceCache(fileName);
                sprite.setScale(1.8f);
                sprite.setPosition(new Vec2((x+1)*80, (y+1)*80));
                checks.add(sprite);
                owner.addChild(sprite);
            }
        }
    }

    public void initFaceCache() {
        SpriteFrameCache.getInstance().addSpriteFramesWithFile("textures/main.plist");
    }

    public Sprite getSpriteFromFaceCache(String name) {
        Sprite sprite = Sprite.createWithSpriteFrame(SpriteFrameCache.getInstance().getSpriteFrameByName(name));
        return sprite;
    }

    public long rand(int max) {
        int temp = max-1;
        long rand = Math.round(Math.random()*254) + 1;
        return rand;
    }

    @Override
    public void onExit() {
        super.onExit();
    }

    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
    }

    @Bind("closeButton")
    @Action(Action.ActionType.WidgetTouchUp)
    public void onCloseClicked(Ref ref) {
        Director.getInstance().end();
    }
}
