package cn.acmersch.telnet.network;

import cn.acmersch.telnet.Main;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-07 19:35
 * @UpdateDate: 2020-03-07 19:35
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 服务
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
@Log4j
public class Server implements Runnable, ThreadPool {
    private ServerSocket serverSocket;
    private ExecutorService pool = Executors.newFixedThreadPool(4);
    private List<SockClient> clients = Lists.newArrayList();
    private volatile boolean started = false;

    public Server() {
        try {
            serverSocket = new ServerSocket(8080);
            addTask(this);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!started) ;
                    log.debug("倒计时");
                    try {
                        for (int i = 0; i < 5; i++) {
                            Thread.sleep(1000);
                            String s = String.format("Count Downing: %s\r\n", i + 1);
                            sendAll(s);
                            log.debug(s);
                        }
                    } catch (InterruptedException e) {
                        log.error("中断", e);
                    } finally {
                        sendAll("Start Load Object.\r\n");
                        Main.gameMap.randomObject();
                        sendAll("Finish Load Object.\r\n");
                    }

                }
            }).start();

        } catch (IOException e) {
            log.error("IO错误", e);
        }
    }

    private void sendAll(String s) {
        clients.forEach(c -> {
            c.send(s);
        });
    }


    @Override
    public void run() {
        try {
            Socket socket = serverSocket.accept();
            log.debug(String.format("一个客户端连接上线: %s", socket.getInetAddress().getAddress().toString()));
            SockClient cli = new SockClient(clients.size(), socket, this);
            clients.add(cli);
            cli.listen();
            started = true;
        } catch (IOException e) {
            log.error("IO错误", e);
        } finally {
            addTask(this);
        }
    }

    @Override
    public void addTask(Runnable task) {
        pool.submit(task);
    }
}
