package org.ccj.game.HeroLeague;

import org.ccj.*;
import org.ccj.d2.Sprite;
import org.ccj.d2.action.ScaleTo;
import org.ccj.d2.action.Sequence;
import org.ccj.editor.cce.NodeController;
import org.ccj.editor.cce.NodeReader;
import org.ccj.math.Vec2;

/**
 */
public class SceneController extends NodeController {



    // 成员变量
    public static final int HEIGHT = 6;
    public static final int WIDTH = 10;
    public static final int BANNER_SIZE = 6;

    int bannerStartX = 320;
    int bannerStartY = GlobalDefine.DESIGN_HEIGHT - 50;

    public static final float SELECT_SPLIT_TIME = 0.2f;     // 点击操作间隔时间

    CheckList checks = new CheckList();                      // 寻找的方格
    CheckList checksBanner = new CheckList();                // 第一行方格
    float timeBase = 0f;                                        //时钟基准
    float timeLastSelect = 0f;                              // 上次选择时间

    boolean canSelect = true;                               // 每次select之后0.2秒才能再次select

    @Override
    public boolean onTouchBegan(Touch touch, Event event) {

        //记录点击时间
        timeLastSelect = timeBase;

        //判断是否正确选择
        if (canSelect) {
            for(int i=0; i<checks.size(); i++) {
                canSelect = false;
                final Check check = checks.get(i);
                if (check.sprite.getBoundingBox().containsPoint(touch.getLocation())) {
                    check.flash();
                    checkMatch(check);
                }
            }
        }

        return super.onTouchBegan(touch, event);
    }

    public void checkMatch(Check check) {

        Check checkBanner = checksBanner.get(0);
        Logger.log("select check id : " + check.id);
        Logger.log("banner check id : " + checkBanner.id);

        if (check.id == checkBanner.id) {
            for(int i=1; i<checksBanner.size(); i++) {
                Check checkPre = checksBanner.get(i-1);
                Check checkSuf = checksBanner.get(i);
                checkSuf.moveTo(checkPre.sprite.getPosition());
            }
            owner.removeChild(checkBanner.sprite);
            checksBanner.remove(0);

            // 更换check的精灵
            int nNewCheckId = rand(Check.CHECK_TOTAL_COUNT);
            Vec2 posOld = check.sprite.getPosition();
            Vec2 position = new Vec2(posOld.getX(), posOld.getY());         //注意:Vec2.x,Vec2.y这种访问方式有问题，必须用get方法
            check.changeId(owner, position, nNewCheckId);

            // 新建bannercheck
            int randIndex = rand(checks.size()-1);
            Check checkTemp = checks.elementAt(randIndex);
            Check checkBannerNew = new Check(owner, new Vec2(bannerStartX, bannerStartY), check.id);
            checkBannerNew.moveTo(new Vec2(Check.CHECK_SIZE_H + bannerStartX, bannerStartY));
            checksBanner.add(checkBanner);
        }
    }

    @Override
    public void onEnter() {
        super.onEnter();
        setTouchEnabled(true);
        setTouchMode(Touch.MODE_ONE_BY_ONE);

        initChecks();
        initChecksBanner();
        initState();
    }

    private void initState() {
        timeBase = 0f;
        timeLastSelect = 0f;
        owner.schedule(new Scheduler.SchedulerCallback() {
            @Override
            public void onUpdate(float delta) {
                super.onUpdate(delta);

                // 基准时钟矫正
                timeBase += delta;

                //判断是否能进行选择
                if (timeBase-timeLastSelect>SELECT_SPLIT_TIME) {
                    canSelect = true;
                }
            }
        });
    }

    private void initChecksBanner() {
        checksBanner.removeAllElements();

        for (int i=0; i< BANNER_SIZE; i++) {
            int randIndex = rand(checks.size()-1);
            Check check = checks.elementAt(randIndex);
            Check checkBanner = new Check(owner, new Vec2(bannerStartX, bannerStartY), check.id);
            checkBanner.moveTo(new Vec2((BANNER_SIZE-i)*Check.CHECK_SIZE_H+bannerStartX, bannerStartY));
            checksBanner.add(checkBanner);
        }

        String showName = "quan_1.png";
        String showActionName = "animates/show.cce";
        Sprite showSprite = ResourceMng.getInstance().getSpriteFromCache(showName);
        showSprite.setPosition(new Vec2(bannerStartX+(BANNER_SIZE)*Check.CHECK_SIZE_H, bannerStartY));
        showSprite.setZOrder(100);
        showSprite.setScale(1f);
        showSprite.runAction(NodeReader.create().readAnimation(showActionName));
        owner.addChild(showSprite);
    }

    private void initChecks() {
        checks.removeAllElements();
        for (int x=0; x<WIDTH; x++) {
            for (int y=0; y<HEIGHT; y++) {
                int randId = rand(Check.CHECK_TOTAL_COUNT);
                Check check = new Check(owner, new Vec2((x+1)*Check.CHECK_SIZE_H, (y+1)*Check.CHECK_SIZE_V), randId);
                checks.add(check);
            }
        }
    }

    public int rand(int max) {
        int temp = max-1;
        long rand = Math.round(Math.random()*temp) + 1;
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
