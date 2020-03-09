package cn.acmersch.telnet.cmd.contanst;

import lombok.Getter;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-07 14:52
 * @UpdateDate: 2020-03-07 14:52
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 游戏命令类型
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
public enum GameCommandTypeEnum {
    up('W', -1, 0),
    down('S', 1, 0),
    left('A', 0, -1),
    right('D', 0, 1),
    refresh('R', 0, 0),
    agg('F', 0, 0);

    @Getter
    private char code;
    @Getter
    private int row;
    @Getter
    private int col;


    GameCommandTypeEnum(char code, int row, int col) {
        this.code = code;
        this.row = row;
        this.col = col;
    }

    public boolean isEqual(char c) {
        return c == code;
    }

    /**
     * 创建命令索引
     *
     * @return
     */
    public static GameCommandTypeEnum[] createMapping() {
        int N = 127;
        GameCommandTypeEnum[] values = values();
        int len = values.length;
        GameCommandTypeEnum[] ret = new GameCommandTypeEnum[N];
        for (int i = 0; i < N; i++) {
            if (i >= len) {
                continue;
            }
            ret[(byte) values[i].getCode()] = values[i];
        }
        return ret;
    }
}