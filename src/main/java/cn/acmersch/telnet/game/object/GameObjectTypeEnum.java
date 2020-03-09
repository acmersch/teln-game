package cn.acmersch.telnet.game.object;

import cn.acmersch.telnet.game.object.impl.EnemyGameObject;
import cn.acmersch.telnet.game.object.impl.PlayerGameObject;
import cn.acmersch.telnet.game.object.impl.WallGameObject;
import lombok.Getter;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-02 22:06
 * @UpdateDate: 2020-03-02 22:06
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 物体类型
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
public enum GameObjectTypeEnum {
    wall((byte) '#', '#', WallGameObject.class),
    floor((byte) ' ', ' ', WallGameObject.class),
    player((byte) 'A', 'A', PlayerGameObject.class),
    enemy((byte) 'Z', 'Z', EnemyGameObject.class);

    @Getter
    private byte code;
    @Getter
    private char flag;
    @Getter
    private Class<? extends GameObject> clazz;

    GameObjectTypeEnum(byte code, char flag, Class<? extends GameObject> clazz) {
        this.code = code;
        this.flag = flag;
        this.clazz = clazz;
    }

    public boolean isEqual(char c) {
        return c == flag;
    }

    public static GameObjectTypeEnum[] createMapping() {
        int N = 127;
        GameObjectTypeEnum[] values = values();
        int len = values.length;
        GameObjectTypeEnum[] ret = new GameObjectTypeEnum[N];
        for (int i = 0; i < N; i++) {
            if (i >= len) {
                continue;
            }
            ret[values[i].getCode()] = values[i];
        }
        return ret;
    }
}
