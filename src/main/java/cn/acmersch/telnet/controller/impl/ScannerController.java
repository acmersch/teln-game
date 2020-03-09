package cn.acmersch.telnet.controller.impl;

import cn.acmersch.telnet.controller.Controller;
import lombok.extern.log4j.Log4j;

import java.util.Scanner;

import static org.apache.log4j.helpers.Loader.getResource;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-06 23:51
 * @UpdateDate: 2020-03-06 23:51
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: command输入控制器
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
@Log4j
public class ScannerController extends Controller {
    private Scanner cin = new Scanner(System.in);

    /**
     * 阻塞监听
     */
    @Override
    protected String receive() {
        String str = cin.nextLine();
        return str;
    }

    /**
     * 开启监听
     */
    public void startListen() {
        while (!breakListen) {
            String recv = receive();
            cmd.put(recv);
        }
    }

    @Override
    public void stopListen() {
        breakListen = true;
        cin.close();
    }

    @Override
    public void send(String s) {

    }

    @Override
    public void run() {

    }
}
