package cn.acmersch.telnet.game.object;

import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-05 12:13
 * @UpdateDate: 2020-03-05 12:13
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 物体
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
@Data
public abstract class GameObject {
    /**
     * 唯一标识
     */
    @Getter
    protected final String uuid = UUID.randomUUID().toString();
    /**
     * 生命值
     */
    protected int hp = 50;
    /**
     * 攻击力
     */
    protected int agg = 10;
    /**
     * 防御力
     */
    protected int def = 2;
    /**
     * 行坐标
     */
    protected int row = -1;
    /**
     * 列坐标
     */
    protected int col = -1;
    /**
     * 标记值
     */
    protected char flag;
    /**
     * 类型
     */
    protected GameObjectTypeEnum type;

    public GameObject() {
    }

    /**
     * 初始化类型
     *
     * @param type
     */
    public void initType(GameObjectTypeEnum type) {
        this.type = type;
        flag = type.getFlag();
    }

    /**
     * 参数初始化
     *
     * @param row
     * @param col
     * @param type
     */
    public void init(int row, int col, GameObjectTypeEnum type) {
        this.row = row;
        this.col = col;
        initType(type);
    }

    public boolean isEqualPosition(GameObject go) {
        return go.getCol() == col && go.getRow() == row;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GameObject) {
            GameObject that = (GameObject) obj;
            return uuid.equals(that.getUuid());
        }
        return false;
    }

    /**
     * 返回受伤对象
     *
     * @return
     */
    public List<WoundObject> agg() {
        return null;
    }

    /**
     * 受伤
     * @param woundObject
     * @return 返回是否死亡
     */
    public boolean wound(WoundObject woundObject) {
        return false;
    }
}
