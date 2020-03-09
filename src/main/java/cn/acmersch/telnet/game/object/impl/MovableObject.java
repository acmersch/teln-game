package cn.acmersch.telnet.game.object.impl;

import cn.acmersch.telnet.game.object.GameObject;
import cn.acmersch.telnet.game.object.WoundObject;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-06 00:29
 * @UpdateDate: 2020-03-06 00:29
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description:
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
public class MovableObject extends GameObject {
    // 方向row
    private int dirRow = 0;
    // 方向col
    private int dirCol = 0;

    /**
     * 即将移动
     *
     * @param dyRow
     * @param dyCol
     * @return true，表示可移动；false，表示移动方向与当前方向不一致，只改变方向
     */
    public boolean moving(int dyRow, int dyCol) {
        if (ifFitDir(dyRow, dyCol)) {
            return true;
        }
        dirRow = fitDir(dyRow);
        dirCol = fitDir(dyCol);
        return false;
    }

    /**
     * 坐标值适应到方向单元值上
     *
     * @param x
     * @return
     */
    private int fitDir(int x) {
        return x == 0 ? 0 : x / Math.abs(x);
    }

    /**
     * 是否与当前方向一致
     *
     * @param dyRow
     * @param dyCol
     * @return
     */
    private boolean ifFitDir(int dyRow, int dyCol) {
        int cRow = dyRow == 0 ? 0 : dyRow / Math.abs(dyRow);
        int cCol = dyCol == 0 ? 0 : dyCol / Math.abs(dyCol);
        if (dirRow == cRow && dirCol == cCol) {
            return true;
        }
        return false;
    }


    public String dirFlag() {
        if (dirRow > 0 && dirCol == 0) {
            return "↓";
        }
        if (dirRow < 0 && dirCol == 0) {
            return "↑";
        }
        if (dirRow == 0 && dirCol > 0) {
            return "→";
        }
        if (dirRow == 0 && dirCol < 0) {
            return "←";
        }
        return "none";

    }

    /**
     * 物体移动
     *
     * @param row
     * @param col
     */
    public void move(int row, int col) {
        this.row += row;
        this.col += col;
    }

    @Override
    public List<WoundObject> agg() {
        if (dirRow != 0) {
            int mRow = dirRow + row;
            WoundObject first = new WoundObject(mRow, col - 1, agg <= 1 ? 1 : agg / 2);
            WoundObject two = new WoundObject(mRow, col, agg);
            WoundObject three = new WoundObject(mRow, col + 1, agg <= 1 ? 1 : agg / 2);
            return Lists.newArrayList(first, two, three);
        } else if (dirCol != 0) {
            int mCol = col + dirCol;
            WoundObject first = new WoundObject(row - 1, mCol, agg <= 1 ? 1 : agg / 2);
            WoundObject two = new WoundObject(row, mCol, agg);
            WoundObject three = new WoundObject(row + 1, mCol, agg <= 1 ? 1 : agg / 2);
            return Lists.newArrayList(first, two, three);
        }
        return Lists.newArrayList();
    }

    @Override
    public boolean wound(WoundObject woundObject) {
        int mAgg = woundObject.getAgg() - def;
        mAgg = mAgg <= 0 ? 1 : mAgg;
        hp -= mAgg;
        return hp <= 0;
    }
}
