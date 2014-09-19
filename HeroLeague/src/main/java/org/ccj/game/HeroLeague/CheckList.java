package org.ccj.game.HeroLeague;

import java.util.Vector;

/**
 * Created by Allen on 2014/9/19.
 */
public class CheckList extends Vector<Check> {

    public Check getCheckById(int id) {
        for (int i=0; i<this.size(); i++) {
            Check check = this.get(i);
            if (check.id == id) {
                return check;
            }
        }
        return null;
    }
}
