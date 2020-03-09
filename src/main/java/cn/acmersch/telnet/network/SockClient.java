package cn.acmersch.telnet.network;

import cn.acmersch.telnet.Main;
import cn.acmersch.telnet.cmd.Command;
import cn.acmersch.telnet.cmd.impl.GameCommand;
import cn.acmersch.telnet.controller.Controller;
import cn.acmersch.telnet.controller.impl.TelnetController;
import cn.acmersch.telnet.game.GameManage;
import cn.acmersch.telnet.game.GameMap;
import cn.acmersch.telnet.game.object.GameObjectTypeEnum;
import cn.acmersch.telnet.game.object.MovableObjectFactory;
import cn.acmersch.telnet.game.object.impl.PlayerGameObject;
import lombok.Getter;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-07 19:40
 * @UpdateDate: 2020-03-07 19:40
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 客户端
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
@Log4j
public class SockClient {
    private GameManage gameManage;
    private Controller controller;

    public SockClient(int sortNo, Socket socket, ThreadPool pool) throws IOException {
        PlayerGameObject playerGameObject = (PlayerGameObject) MovableObjectFactory.newInstance(GameObjectTypeEnum.player);
        playerGameObject.setFlag((char) ((int) 'A' + sortNo));
        Main.gameMap.addPlayer(playerGameObject);
        gameManage = new GameManage(playerGameObject, Main.gameMap);
        controller = new TelnetController(gameManage.getCmd(), socket.getInputStream(), socket.getOutputStream(), pool);
        gameManage.setController(controller);
    }

    public void listen() {
        controller.startListen();
    }

    public void send(String s) {
        controller.send(s);
    }
}
