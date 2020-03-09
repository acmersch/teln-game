package cn.acmersch.telnet.controller;

import cn.acmersch.telnet.cmd.Command;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-06 23:47
 * @UpdateDate: 2020-03-06 23:47
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 控制器
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
public abstract class Controller implements Runnable {
    protected volatile boolean breakListen = false;
    /**
     * 命令处理器
     */
    protected Command cmd;

    /**
     * 开启监听
     */
    public abstract void startListen();

    /**
     * 关闭监听
     */
    public abstract void stopListen();

    /**
     * 监听器
     */
    protected abstract String receive();

    /**
     * 发送指令
     * @param s
     */
    public abstract void send(String s);

    public void addCommand(Command cmd) {
        this.cmd = cmd;
    }
}
