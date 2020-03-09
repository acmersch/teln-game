package cn.acmersch.telnet.cmd;

import cn.acmersch.telnet.controller.Controller;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-07 14:36
 * @UpdateDate: 2020-03-07 14:36
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 游戏命令接口
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
public abstract class Command {
    protected CommandCallback cb;

    public void addCallback(CommandCallback cb) {
        this.cb = cb;
    }

    /**
     * 防止命令
     *
     * @param cmd
     * @return
     */
    public abstract boolean put(String cmd);
}
