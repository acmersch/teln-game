package cn.acmersch.telnet.cmd;

import cn.acmersch.telnet.cmd.impl.GameCmdObject;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-07 15:14
 * @UpdateDate: 2020-03-07 15:14
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 命令回调
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
public interface CommandCallback<T extends CommandObject> {
    void execute(T object);
}
