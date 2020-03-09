package cn.acmersch.telnet.network;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-07 20:13
 * @UpdateDate: 2020-03-07 20:13
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 线程池
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
public interface ThreadPool {
    public void addTask(Runnable task);
}
