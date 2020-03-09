package cn.acmersch.telnet.cmd.impl;

import cn.acmersch.telnet.cmd.CommandObject;
import cn.acmersch.telnet.cmd.contanst.GameCommandTypeEnum;
import cn.acmersch.telnet.controller.Controller;
import cn.acmersch.telnet.game.object.impl.PlayerGameObject;
import lombok.Data;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-07 15:18
 * @UpdateDate: 2020-03-07 15:18
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 游戏命令对象
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
@Data
public class GameCmdObject extends CommandObject {
    private PlayerGameObject player;
    private Controller controller;
    private GameCommandTypeEnum cmdType;
}
