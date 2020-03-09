package cn.acmersch.telnet.game;

import lombok.Getter;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-07 15:51
 * @UpdateDate: 2020-03-07 15:51
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 屏幕对象
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
public class ScreenObject {
    /**
     * 行
     */
    @Getter
    private int rowLen;
    /**
     * 列
     */
    @Getter
    private int colLen;
    /**
     * 最大行
     */
    private int maxRow;
    /**
     * 最大列
     */
    private int maxCol;
    /**
     * 方块
     */
    @Getter
    private char[][] tile;

    public ScreenObject(int row, int col) {
        rowLen = row;
        colLen = col;
        tile = new char[rowLen][colLen];
    }

    /**
     * 重置
     */
    public void reset() {
        for (int i = 0; i < rowLen; i++) {
            for (int j = 0; j < colLen; j++) {
                tile[i][j] = ' ';
            }
        }
    }

    /**
     * 打印字符串
     *
     * @param row 从row行开始
     * @param s
     */
    public void println(int row, String s) {
        for (int i = 0; i < s.length(); i++) {
            tile[row][i] = s.charAt(i);
        }
        maxPoint(row, s.length());
    }

    /**
     * 最大位置设置
     *
     * @param row
     * @param col
     */
    private void maxPoint(int row, int col) {
        maxRow = Math.max(row, maxRow);
        maxCol = Math.max(col, maxCol);
    }

    /**
     * 放入放个数组
     *
     * @param row
     * @param col
     * @param tile
     * @param destRow
     * @param destCol
     */
    public void putTile(int row, int col, char[][] tile, int destRow, int destCol) {
        if (row > rowLen) row = rowLen;
        if (col > colLen) col = colLen;
        int rowLimit = row + destRow;
        int colLimit = col + destCol;
        for (int i = row; i < rowLimit; i++) {
            for (int j = col; j < colLimit; j++) {
                this.tile[i][j] = tile[i - row][j - col];
            }
        }
        maxPoint(rowLimit, colLimit);
    }

    /**
     * 放入screen
     *
     * @param row
     * @param col
     * @param screen
     */
    public void putScreen(int row, int col, ScreenObject screen) {
        putTile(row, col, screen.getTile(), screen.getRowLen(), screen.getColLen());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\r\n\r\n");
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                stringBuilder.append(tile[i][j]);
            }
            stringBuilder.append("\r\n");
        }
        stringBuilder.append("\r\n\r\n");
        return stringBuilder.toString();
    }
}
