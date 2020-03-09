package cn.acmersch.telnet.controller.impl;

import cn.acmersch.telnet.cmd.Command;
import cn.acmersch.telnet.controller.Controller;
import cn.acmersch.telnet.network.ThreadPool;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-07 19:34
 * @UpdateDate: 2020-03-07 19:34
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: telnet控制器
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
@Log4j
public class TelnetController extends Controller {
    private InputStream input;
    private OutputStream output;
    private byte[] buffer = new byte[32];
    private ThreadPool pool;

    public TelnetController(Command cmd, InputStream inputStream, OutputStream outputStream, ThreadPool pool) {
        super();
        this.cmd = cmd;
        this.input = inputStream;
        this.output = outputStream;
        this.pool = pool;
    }

    @Override
    public void startListen() {
        pool.addTask(this);
    }

    @Override
    public void stopListen() {
        breakListen = true;
    }

    @Override
    protected String receive() {
        try {
            int len = input.read(buffer, 0, 32);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < len; i++) {
                if (buffer[i] == '\n' || buffer[i] == '\r') {
                    break;
                }
                stringBuilder.append((char) buffer[i]);
            }
            log.debug(stringBuilder);
            return stringBuilder.toString();
        } catch (IOException e) {
            log.error("IO错误", e);
            return "";
        }
    }

    @Override
    public void send(String s) {
        try {
            output.write(s.getBytes());
        } catch (IOException e) {
            log.error("IO错误", e);
        }
    }

    @Override
    public void run() {
        if (cmd != null) {
            cmd.put(receive());
            if (!breakListen) {
                pool.addTask(this);
            }
        }
    }
}
