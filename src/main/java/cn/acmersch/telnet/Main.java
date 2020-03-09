package cn.acmersch.telnet;


import cn.acmersch.telnet.cmd.Command;
import cn.acmersch.telnet.cmd.impl.GameCommand;
import cn.acmersch.telnet.controller.Controller;
import cn.acmersch.telnet.controller.impl.ScannerController;
import cn.acmersch.telnet.game.GameManage;
import cn.acmersch.telnet.game.GameMap;
import cn.acmersch.telnet.game.object.GameObjectTypeEnum;
import cn.acmersch.telnet.game.object.MovableObjectFactory;
import cn.acmersch.telnet.game.object.impl.MovableObject;
import cn.acmersch.telnet.game.object.impl.PlayerGameObject;
import cn.acmersch.telnet.network.Server;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import static org.apache.log4j.helpers.Loader.getResource;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-01 14:43
 * @UpdateDate: 2020-03-01 14:43
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 启动类
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
@Log4j
public class Main {
    public final static GameMap gameMap = new GameMap(15, 30, null);

    static {
        String customizedPath = "logback.xml";
        DOMConfigurator.configure(getResource(customizedPath));
    }

    public static void main(String[] args) {
        log.debug("程序启动");
        new Server();
        Main.gameMap.start();
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }


    public static void temp() {
        PlayerGameObject player = (PlayerGameObject) MovableObjectFactory.newInstance(GameObjectTypeEnum.player);
        // 设置移动物
        List<MovableObject> gameObjectList = Lists.newArrayList(
                player,
                MovableObjectFactory.newInstance(GameObjectTypeEnum.enemy)
        );
        GameMap gameMap = new GameMap(10, 18, gameObjectList);
        // 游戏管理器
        GameManage manage = new GameManage(player, gameMap);
        // 设置命令接收器
        Command cmd = new GameCommand();
        cmd.addCallback(manage);
        // 启动控制器
        Controller controller = new ScannerController();
        controller.addCommand(cmd);
        controller.startListen();

        System.out.println(gameMap.toString());
    }

}

