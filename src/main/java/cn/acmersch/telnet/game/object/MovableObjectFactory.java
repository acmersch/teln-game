package cn.acmersch.telnet.game.object;

import cn.acmersch.telnet.game.object.impl.MovableObject;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-05 12:15
 * @UpdateDate: 2020-03-05 12:15
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 物体工厂
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
//@Slf4j
public abstract class MovableObjectFactory {

    public static MovableObject newInstance(GameObjectTypeEnum type) {
        try {
            GameObject gameObject = type.getClazz().newInstance();
            gameObject.initType(type);
            if (gameObject instanceof MovableObject) {
                return (MovableObject) gameObject;
            }
        } catch (InstantiationException e) {
            //   log.error("{}", e.fillInStackTrace().toString());
        } catch (IllegalAccessException e) {
            // log.error("", e.fillInStackTrace());
        }
        return null;
    }


}
