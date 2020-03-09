package cn.acmersch.telnet.game.object.impl;

import cn.acmersch.telnet.Main;
import cn.acmersch.telnet.game.ScreenObject;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-05 12:24
 * @UpdateDate: 2020-03-05 12:24
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 玩家
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
public class PlayerGameObject extends MovableObject {
    /**
     * 屏幕
     */
    private ScreenObject screenObject;

    public ScreenObject toScreen() {
        String nameInfo = "uid: " + uuid.hashCode();
        String typeInfo = "type: " + getFlag();
        String dirInfo = "dir: " + dirFlag() + "; r:" + row + " c:" + col;
        String hpInfo = "hp: " + hp;
        String aggInfo = "agg: " + agg;
        String defInfo = "def: " + def;

        int maxCol = Math.max(nameInfo.length(), hpInfo.length());
        maxCol = Math.max(maxCol, dirInfo.length());
        maxCol = Math.max(maxCol, aggInfo.length());
        maxCol = Math.max(maxCol, defInfo.length());
        int maxRow = 7;

        if (screenObject == null) {
            screenObject = new ScreenObject(7, maxCol + 4);
        }
        screenObject.reset();
        screenObject.println(0, nameInfo);
        screenObject.println(1, dirInfo);
        screenObject.println(2, typeInfo);
        screenObject.println(3, "");
        screenObject.println(4, hpInfo);
        screenObject.println(5, aggInfo);
        screenObject.println(6, defInfo);
        return screenObject;
    }
}
