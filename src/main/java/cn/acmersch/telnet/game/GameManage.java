package cn.acmersch.telnet.game;

import cn.acmersch.telnet.cmd.Command;
import cn.acmersch.telnet.cmd.CommandCallback;
import cn.acmersch.telnet.cmd.contanst.GameCommandTypeEnum;
import cn.acmersch.telnet.cmd.impl.GameCmdObject;
import cn.acmersch.telnet.cmd.impl.GameCommand;
import cn.acmersch.telnet.controller.Controller;
import cn.acmersch.telnet.game.object.GameObjectTypeEnum;
import cn.acmersch.telnet.game.object.impl.PlayerGameObject;
import cn.acmersch.telnet.network.ThreadPool;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.util.concurrent.BlockingQueue;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-07 15:21
 * @UpdateDate: 2020-03-07 15:21
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 游戏管理器
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
@AllArgsConstructor
@Log4j
public class GameManage implements CommandCallback<GameCmdObject> {
    private PlayerGameObject player;
    private GameMap gameMap;
    @Getter
    private Command cmd;
    /**
     * 游戏控制器
     */
    @Setter
    @Getter
    private Controller controller;

    public GameManage(PlayerGameObject player, GameMap gameMap) {
        this.player = player;
        this.gameMap = gameMap;
        cmd = new GameCommand();
        cmd.addCallback(this);
    }

    @Override
    public void execute(GameCmdObject object) {
        object.setController(controller);
        object.setPlayer(player);
        gameMap.addCmd(object);
    }


}
