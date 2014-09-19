package org.ccj.game.HeroLeague;

import org.ccj.Director;
import org.ccj.Event;
import org.ccj.Scheduler;
import org.ccj.Touch;
import org.ccj.base.Ref;
import org.ccj.d2.Sprite;
import org.ccj.d2.SpriteFrameCache;
import org.ccj.d2.action.ScaleTo;
import org.ccj.d2.action.Sequence;
import org.ccj.editor.cce.Action;
import org.ccj.editor.cce.Bind;
import org.ccj.editor.cce.NodeController;
import org.ccj.editor.cce.NodeReader;
import org.ccj.math.Vec2;

import java.util.Vector;

/**
 */
public class SceneController extends NodeController {



    // 成员变量
    public static final int HEIGHT = 6;
    public static final int WIDTH = 10;
    public static final int BANNER_SIZE = 6;
    public static final int CHECK_SIZE_H = 80;
    public static final int CHECK_SIZE_V = 80;

    CheckList checks = new CheckList();                      // 寻找的方格
    CheckList checksBanner = new CheckList();                // 第一行方格

    boolean canSelect = true;                               // 每次select之后0.2秒才能再次select

    @Override
    public boolean onTouchBegan(Touch touch, Event event) {

        if (canSelect) {
            for(int i=0; i<checks.size(); i++) {
                canSelect = false;
                final Check check = checks.get(i);
                if (check.getBoundingBox().containsPoint(touch.getLocation())) {
                    check.runAction(Sequence.create(
                            ScaleTo.create(0.2f, 1.2f),
                            ScaleTo.create(0, 1)));
                    owner.scheduleOnce(new Scheduler.SchedulerCallback() {
                        @Override
                        public void onUpdate(float delta) {
                            super.onUpdate(delta);
                            canSelect = true;
                            checkMatch(check.id);
                        }
                    }, 0.2f);
                }
            }
        }

        return super.onTouchBegan(touch, event);
    }

    public void checkMatch(int id) {
        Check checkBanner = checksBanner.get(0);
        if (id == checkBanner.id) {
            for(int i=1; i<BANNER_SIZE; i++) {
                Check checkPre = checksBanner.get(i-1);
                Check checkSuf = checksBanner.get(i);
                checkSuf.moveTo(checkPre.getPosition());
            }
        }
aaaa
        checksBanner.remove(0);

    }

    @Override
    public void onEnter() {
        super.onEnter();
        setTouchEnabled(true);
        setTouchMode(Touch.MODE_ONE_BY_ONE);

        initCheckCache();
        initChecks();
        initChecksBanner();
    }

    private void initChecksBanner() {
        checksBanner.removeAllElements();

        int bannerStartX = 0;
        int bannerStartY = (int) Director.getInstance().getWinSize().height-50;
        for (int i=0; i< BANNER_SIZE; i++) {
            int randIndex = rand(WIDTH * HEIGHT);
            Check check = checks.elementAt(randIndex);
            int id = check.id;
            String fileName = randIndex + "-40.png";
            Check checkBanner = (Check) getSpriteFromFaceCache(fileName);
            checkBanner.id = id;
            checkBanner.setScale(1.8f);
            checkBanner.setPosition(new Vec2(bannerStartX, bannerStartY));
            checkBanner.moveTo(new Vec2((i+1)*CHECK_SIZE_H+bannerStartX, bannerStartY));
            checksBanner.add(checkBanner);
            owner.addChild(checkBanner);
        }

        String showName = "quan_1.png";
        String showActionName = "show.cce";
        Sprite showSprite = getSpriteFromFaceCache(showName);
        showSprite.setPosition(new Vec2((BANNER_SIZE)*CHECK_SIZE_H, bannerStartY));
        showSprite.setZOrder(100);
        showSprite.setScale(0.6f);
        showSprite.runAction(NodeReader.create().readAnimation(showActionName));
        owner.addChild(showSprite);
    }

    private void initChecks() {
        checks.removeAllElements();
        for (int x=0; x<WIDTH; x++) {
            for (int y=0; y<HEIGHT; y++) {
                int randId = rand(255);
                String fileName = randId + "-40.png";
                Check check = (Check)getSpriteFromFaceCache(fileName);
                check.id = randId;
                check.setScale(1.8f);
                check.setPosition(new Vec2((x+1)*CHECK_SIZE_H, (y+1)*CHECK_SIZE_V));
                checks.add(check);
                owner.addChild(check);
            }
        }
    }

    public void initCheckCache() {
        SpriteFrameCache.getInstance().addSpriteFramesWithFile("textures/main.plist");
    }

    public Sprite getSpriteFromFaceCache(String name) {
        Sprite sprite = Sprite.createWithSpriteFrame(SpriteFrameCache.getInstance().getSpriteFrameByName(name));
        return sprite;
    }

    public int rand(int max) {
        int temp = max-1;
        long rand = Math.round(Math.random()*254) + 1;
        int nRand = (int)rand;
        return nRand;
    }

    @Override
    public void onExit() {
        super.onExit();
    }

    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
    }
}
