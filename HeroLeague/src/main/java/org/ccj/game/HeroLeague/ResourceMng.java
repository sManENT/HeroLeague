package org.ccj.game.HeroLeague;

import org.ccj.d2.Sprite;
import org.ccj.d2.SpriteFrameCache;

/**
 * Created by Allen on 2014-9-21.
 */
public class ResourceMng {
    private static ResourceMng gResourceMng = null;

    public static ResourceMng getInstance() {
        if (gResourceMng == null) {
            gResourceMng = new ResourceMng();
        }
        return  gResourceMng;
    }

    private ResourceMng() {
        init();
    }

    private void init() {
        SpriteFrameCache.getInstance().addSpriteFramesWithFile("textures/main.plist");
    }

    public Sprite getSpriteFromCache(String fileName) {
        return  Sprite.createWithSpriteFrame(SpriteFrameCache.getInstance().getSpriteFrameByName(fileName));
    }
}
