package org.ccj.game.HeroLeague;

import org.ccj.*;
import org.ccj.d2.Sprite;
import org.ccj.editor.cce.NodeController;
import org.ccj.editor.cce.NodeReader;
import org.ccj.math.Vec2;

/**
 */
public class MainController extends NodeController {

    // 成员变量
    public static final int HEIGHT = 4;
    public static final int WIDTH = 10;
    public static final int BANNER_SIZE = 6;

    int bannerStartX = 200;
    int bannerStartY = GlobalDefine.DESIGN_HEIGHT - 200;
    int checkStartX = 120;
    int checkStartY = 80;

    public static final float SELECT_SPLIT_TIME = 0.2f;     // 点击操作间隔时间
    public static final float SUCESS_LAST_TIME = 1.5f;      // 成功保持时间
    public static final float SUCESS_LIMIT_TIME = 5.0f;     // 成功时限，超过则认为失败
    public static final float GAME_TIME = 30;

    CheckList checks = new CheckList();                         // 寻找的方格
    CheckList checksBanner = new CheckList();                   // 第一行方格
    float timeReal = 0f;                                        // 时钟基准，每一帧都会刷新改时间
    float timeLastSelect = 0f;                                 // 上次选择时间
    float timeLastSuccess = 0f;                                 // 上次成功选择时间
    int lastSuccessCount = 0;                                   // 上次连续选中个数

    boolean canSelect = true;                                  // 每次select之后0.2秒才能再次select

    @Override
    public boolean onTouchBegan(Touch touch, Event event) {

        //记录点击时间
        timeLastSelect = timeReal;

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
        if (checksBanner.size() < 1) {
            return;
        }

        Check checkBanner = checksBanner.get(0);
        Logger.log("select check id : " + check.id);
        Logger.log("banner check id : " + checkBanner.id);

        if (check.id == checkBanner.id) {

            // 更换check的精灵
            int nNewCheckId = randomCheckId();
            Vec2 posOld = check.sprite.getPosition();
            Vec2 position = new Vec2(posOld.getX(), posOld.getY());         //注意:Vec2.x,Vec2.y这种访问方式有问题，必须用get方法
            check.changeId(owner, position, nNewCheckId);

            // 更换bannercheck
            Vec2 lastBannerPosition = checksBanner.get(checksBanner.size()-1).sprite.getPosition();
            Vec2 newBannerPosiontion = new Vec2(lastBannerPosition.getX(), lastBannerPosition.getY());
            for(int i=1; i<checksBanner.size(); i++) {
                Check checkPre = checksBanner.get(i-1);
                Check checkSuf = checksBanner.get(i);
                checkSuf.moveTo(checkPre.sprite.getPosition());
            }

            owner.removeChild(checkBanner.sprite);
            checksBanner.remove(0);

            int randId = randomBannerCheckId();
            Check bannerCheckNew = new Check(owner, newBannerPosiontion, randId);
            checksBanner.add(bannerCheckNew);

            // 成功时的数学逻辑
            successLogic();
        }
    }

    public void failure() {
        if (checksBanner.size() < 1) {
            return;
        }

        Logger.log("time out, failure!");

        Statistics.getInstance().failureCount++;

        // 更换bannercheck
        Vec2 lastBannerPosition = checksBanner.get(checksBanner.size()-1).sprite.getPosition();
        Vec2 newBannerPosiontion = new Vec2(lastBannerPosition.getX(), lastBannerPosition.getY());
        for(int i=1; i<checksBanner.size(); i++) {
            Check checkPre = checksBanner.get(i-1);
            Check checkSuf = checksBanner.get(i);
            checkSuf.moveTo(checkPre.sprite.getPosition());
        }

        // 1\删除原来的bannercheck
        Check checkBannerFirst = checksBanner.get(0);
        owner.removeChild(checkBannerFirst.sprite);
        checksBanner.remove(0);

        // 2\增加新的check
        int randId = randomBannerCheckId();
        Check bannerCheckNew = new Check(owner, newBannerPosiontion, randId);
        checksBanner.add(bannerCheckNew);
    }

    public void successLogic() {
        // 在连击时间范围内
        if (timeReal -timeLastSuccess<SUCESS_LAST_TIME) {
            lastSuccessCount++;

            if (2 == lastSuccessCount) {
                Statistics.getInstance().doubleSuccessCount++;
            } else if (3 == lastSuccessCount) {
                Statistics.getInstance().threeSuccessCount++;
            } else if (4 == lastSuccessCount) {
                Statistics.getInstance().fourSuccessCount++;
            } else if (5 == lastSuccessCount) {
                Statistics.getInstance().fiveSuccessCount++;
            }

            if (Statistics.getInstance().maxLastSuccessCount < lastSuccessCount) {
                Statistics.getInstance().maxLastSuccessCount = lastSuccessCount;
            }
        } else {
            lastSuccessCount = 0;
        }

        Statistics.getInstance().successCount++;
        timeLastSuccess = timeReal;
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

        Statistics.getInstance().resetData();

        timeReal = 0f;
        timeLastSelect = 0f;
    }

    private void initChecksBanner() {
        checksBanner.removeAllElements();

        for (int i=0; i< BANNER_SIZE; i++) {
            int randId = randomBannerCheckId();
            Check checkBanner = new Check(owner, new Vec2(bannerStartX, bannerStartY), randId);
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
                int randId = randomCheckId();
                Check check = new Check(owner, new Vec2(checkStartX+x*Check.CHECK_SIZE_H, checkStartY+y*Check.CHECK_SIZE_V), randId);
                checks.add(check);
            }
        }
    }

    public int random(int max) {
        int temp = max-1;
        long rand = Math.round(Math.random()*temp) + 1;
        int nRand = (int)rand;
        return nRand;
    }

    public int randomBannerCheckId() {
        boolean bOk = false;
        int nNewId = 1;
        while (!bOk) {
            int nCheckIndex = random(checks.size()-1);
            nNewId = checks.get(nCheckIndex).id;

            bOk = true;
            for (int i=0; i<checksBanner.size(); i++) {
                if (checksBanner.get(i).id == nNewId) {
                    bOk = false;
                    break;
                }
            }
        }
        return nNewId;
    }

    public int randomCheckId() {
        boolean bOk = false;
        int nNewId = 1;
        while (!bOk) {
            nNewId = random(Check.CHECK_TOTAL_COUNT);

            bOk = true;
            for (int i=0; i<checks.size(); i++) {
                if (checks.get(i).id == nNewId) {
                    bOk = false;
                    break;
                }
            }
        }
        return nNewId;
    }

    @Override
    public void onExit() {
        super.onExit();
    }

    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);

        // 基准时钟矫正
        timeReal += delta;

        // 判断是否能进行选择
        if (timeReal -timeLastSelect>=SELECT_SPLIT_TIME) {
            canSelect = true;
        }

        // 判断是否失败
        if (timeReal -timeLastSuccess>=SUCESS_LIMIT_TIME) {
            failure();
            timeLastSuccess = timeReal;
        }

        // 判断游戏时间是否到了
        if (timeReal >= GAME_TIME) {
            Director.getInstance().replaceScene(NodeReader.create().readScene("layouts/Over.cce"));
        }
    }
}
