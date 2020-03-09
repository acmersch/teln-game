package cn.acmersch.telnet.game.object;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-07 18:37
 * @UpdateDate: 2020-03-07 18:37
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 受伤对象
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
@Data
@AllArgsConstructor
public class WoundObject {
    private int row;
    private int col;
    private int agg;
}
