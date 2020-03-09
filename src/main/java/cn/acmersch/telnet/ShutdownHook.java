package cn.acmersch.telnet;

import lombok.extern.log4j.Log4j;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-07 19:50
 * @UpdateDate: 2020-03-07 19:50
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 关闭事件
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
@Log4j
public class ShutdownHook extends Thread {
    @Override
    public void run() {
        log.debug("关闭");
    }
}
