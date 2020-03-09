package cn.acmersch.telnet.cmd.impl;

import cn.acmersch.telnet.cmd.Command;
import cn.acmersch.telnet.cmd.CommandObject;
import cn.acmersch.telnet.cmd.contanst.GameCommandTypeEnum;
import cn.acmersch.telnet.controller.Controller;
import cn.acmersch.telnet.game.GameMap;
import cn.acmersch.telnet.network.ThreadPool;
import lombok.extern.log4j.Log4j;
import org.springframework.util.StringUtils;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-07 14:32
 * @UpdateDate: 2020-03-07 14:32
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 游戏命令接收器
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
@Log4j
public class GameCommand extends Command {
    /**
     * 命令类型索引
     */
    private final static GameCommandTypeEnum[] cmdTable = GameCommandTypeEnum.createMapping();

    @Override
    public boolean put(String cmd) {
        if (StringUtils.isEmpty(cmd)) {
            return false;
        }
        byte c = cmd.toUpperCase().getBytes()[0];
        GameCommandTypeEnum cmdType = cmdTable[c];
        if (cmdType == null) {
            log.debug(String.format("命令错误: %s", cmd));
            return false;
        }
        GameCmdObject object = new GameCmdObject();
        object.setCmdType(cmdType);
        cb.execute(object);

        return true;
    }

}
