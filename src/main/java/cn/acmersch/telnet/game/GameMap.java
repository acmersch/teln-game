package cn.acmersch.telnet.game;

import cn.acmersch.telnet.cmd.contanst.GameCommandTypeEnum;
import cn.acmersch.telnet.cmd.impl.GameCmdObject;
import cn.acmersch.telnet.controller.Controller;
import cn.acmersch.telnet.game.object.GameObject;
import cn.acmersch.telnet.game.object.GameObjectFactory;
import cn.acmersch.telnet.game.object.GameObjectTypeEnum;
import cn.acmersch.telnet.game.object.WoundObject;
import cn.acmersch.telnet.game.object.impl.MovableObject;
import cn.acmersch.telnet.game.object.impl.PlayerGameObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Company: 政采云有限公司
 * @Author: 正则
 * @CreateDate: 2020-03-02 22:31
 * @UpdateDate: 2020-03-02 22:31
 * @UpdateRemark: init
 * @Version: 1.0
 * @Description: 游戏地图
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 */
@Log4j
public class GameMap implements Runnable {
    private static final int SCREEN_ROW = 100;
    private static final int SCREEN_COL = 100;
    private static final GameObjectTypeEnum[] typeMapping = GameObjectTypeEnum.createMapping();
    /**
     * 高度（行）
     */
    @Getter
    private int maxRow;
    /**
     * 宽度（列）
     */
    @Getter
    private int maxCol;
    /**
     * 地图
     */
    private char[][] tile;
    private GameObject[][] tileObject;
    /**
     * 对象索引
     */
    private Map<Byte, List<GameObject>> objectMapping = Maps.newHashMap();
    /**
     * 可移动物体
     */
    private List<MovableObject> movableObjectList;
    /**
     * 大屏
     */
    private ScreenObject screen = new ScreenObject(SCREEN_ROW, SCREEN_COL);
    /**
     * 游戏命令阻塞队列
     */
    private BlockingQueue<GameCmdObject> cmdObjectBlockingQueue = new ArrayBlockingQueue<GameCmdObject>(100);
    private Thread thread;

    /**
     * 构造方法
     *
     * @param row               初始化高度（行）
     * @param col               初始化宽度（列）
     * @param movableObjectList 可移动物体
     */
    public GameMap(int row, int col, List<MovableObject> movableObjectList) {
        maxCol = col;
        maxRow = row;
        tile = new char[maxRow][maxCol];
        tileObject = new GameObject[maxRow][maxCol];
        this.movableObjectList = movableObjectList;
        randomMap();
        randomObject();
    }

    /**
     * 随机生成对象
     */
    public void randomObject() {
        if (CollectionUtils.isEmpty(movableObjectList)) {
            return;
        }
        List<GameObject> floorList = objectMapping.get(GameObjectTypeEnum.floor.getCode());
        boolean[] selected = new boolean[floorList == null ? 1 : floorList.size()];
        Arrays.fill(selected, false);

        Random random = new Random();

        for (MovableObject mo : movableObjectList) {
            int r = -1;
            do {
                r = random.nextInt(floorList == null ? 1 : floorList.size());
            } while (selected[r]);
            selected[r] = true;
            GameObject floor = floorList.get(r);
            int row = floor.getRow();
            int col = floor.getCol();
            putObject(row, col, mo);
            floorList.remove(r);
        }
    }

    /**
     * map放置元素
     *
     * @param row
     * @param col
     * @param gameObject
     */
    private void putObject(int row, int col, GameObject gameObject) {
        tile[row][col] = gameObject.getFlag();
        tileObject[row][col] = gameObject;
        gameObject.setRow(row);
        gameObject.setCol(col);
        if (gameObject instanceof MovableObject) {
            if (!movableObjectList.contains(gameObject)) {
                movableObjectList.add((MovableObject) gameObject);
            }
        }
        List<GameObject> list = objectMapping.get(gameObject.getType().getCode());
        if (list == null) {
            list = Lists.newArrayList();
            objectMapping.put(gameObject.getType().getCode(), list);
        }
        if (!list.contains(gameObject)) {
            list.add(gameObject);
        }
    }

    /**
     * 随机生成地图
     */
    private void randomMap() {
        Random random = new Random();
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                int r = random.nextInt(100);
                char c = r < 45 ? GameObjectTypeEnum.wall.getFlag() : GameObjectTypeEnum.floor.getFlag();
                tile[i][j] = c;
                //边界置为墙
                if (i == 0 || j == 0 || i == maxRow - 1 || j == maxCol - 1) {
                    tile[i][j] = GameObjectTypeEnum.wall.getFlag();
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            cellAutoOne();
        }
        for (int i = 0; i < 6; i++) {
            cellAutoTwo();
        }
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                GameObjectTypeEnum type = typeMapping[tile[i][j]];
                GameObject obj = GameObjectFactory.newInstance(i, j, type);
                List<GameObject> list = objectMapping.get(type.getCode());
                if (list == null) {
                    list = Lists.newArrayList();
                    objectMapping.put(type.getCode(), list);
                }
                list.add(obj);
                tileObject[i][j] = obj;
            }
        }
    }

    /**
     * 算法One改变表格
     */
    private void cellAutoOne() {
        char[][] newTile = new char[maxRow][maxCol];
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                // 周围一圈
                int r1 = countForRoundWall(i, j, 1);
                if (GameObjectTypeEnum.wall.isEqual(tile[i][j])) {
                    newTile[i][j] = (r1 >= 4) ? GameObjectTypeEnum.wall.getFlag() : GameObjectTypeEnum.floor.getFlag();
                } else {
                    // 周围二圈
                    int r2 = countForRoundWall(i, j, 2);
                    newTile[i][j] = (r1 >= 5 || r2 <= 2) ? GameObjectTypeEnum.wall.getFlag() : GameObjectTypeEnum.floor.getFlag();
                }
                if (i == 0 || i == maxRow - 1 || j == 0 || j == maxCol - 1) {
                    newTile[i][j] = GameObjectTypeEnum.wall.getFlag();
                }
            }
        }
        copy(newTile);
    }


    /**
     * 算法Two改变表格
     */
    private void cellAutoTwo() {
        char[][] newTile = new char[maxRow][maxCol];
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                // 周围一圈
                int r1 = countForRoundWall(i, j, 1);
                if (GameObjectTypeEnum.wall.isEqual(tile[i][j])) {
                    newTile[i][j] = (r1 >= 4) ? GameObjectTypeEnum.wall.getFlag() : GameObjectTypeEnum.floor.getFlag();
                } else {
                    newTile[i][j] = (r1 >= 5) ? GameObjectTypeEnum.wall.getFlag() : GameObjectTypeEnum.floor.getFlag();
                }
                if (i == 0 || i == maxRow - 1 || j == 0 || j == maxCol - 1) {
                    newTile[i][j] = GameObjectTypeEnum.wall.getFlag();
                }
            }
        }
        copy(newTile);
    }

    /**
     * 复制表格
     *
     * @param newTile
     */
    private void copy(char[][] newTile) {
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                tile[i][j] = newTile[i][j];
            }
        }
    }

    /**
     * 周围墙的梳理
     *
     * @param i 行坐标
     * @param j 列坐标
     * @param n 周围n圈内
     * @return
     */
    private int countForRoundWall(int i, int j, int n) {
        int cnt = 0;
        for (int ii = i - n; ii <= i + n; ii++) {
            for (int jj = j - n; jj <= j + n; jj++) {
                if (assertOutOfMap(ii, jj)) {
                    continue;
                }
                if (GameObjectTypeEnum.wall.isEqual(tile[ii][jj])) {
                    cnt += 1;
                }
            }
        }
        //去除本身是否为墙
        if (GameObjectTypeEnum.wall.isEqual(tile[i][j])) {
            cnt--;
        }
        return cnt;
    }

    /**
     * 判断当前坐标是否在地图外围
     *
     * @param row
     * @param col
     * @return
     */
    private boolean assertOutOfMap(int row, int col) {
        return row < 0 ? true : row >= maxRow ? true : col < 0 ? true : col >= maxCol;
    }

    @Override
    public String toString() {
        screen.reset();
        List<ScreenObject> screenObjects = Lists.newArrayList();
        movableObjectList.forEach(obj -> {
            if (obj instanceof PlayerGameObject) {
                PlayerGameObject player = (PlayerGameObject) obj;
                ScreenObject screenPlayer = player.toScreen();
                screenObjects.add(screenPlayer);
            }
        });
        screen.putTile(0, 0, tile, maxRow, maxCol);

        int curCol = maxCol + 4;
        for (int i = 0; i < screenObjects.size(); i++) {
            ScreenObject screenObject = screenObjects.get(i);
            screen.putScreen(1, curCol, screenObject);
            curCol += screenObject.getColLen() + 4;
        }
        return screen.toString();
    }

    /**
     * 是否可以移动
     *
     * @param row
     * @param col
     * @return
     */
    public boolean canMoving(int row, int col) {
        if (!ifInner(row, col)) {
            return false;
        }
        if (GameObjectTypeEnum.floor.isEqual(tile[row][col])) {
            return true;
        }
        return false;
    }

    /**
     * 是否在地图内
     *
     * @param row
     * @param col
     * @return
     */
    public boolean ifInner(int row, int col) {
        return row >= 0 && col >= 0 && row < maxRow && col < maxCol;
    }

    /**
     * 移动物体
     *
     * @param obj
     * @param row
     * @param col
     */
    public void move(MovableObject obj, int row, int col) {
        // 更新地图
        if (movableObjectList.contains(obj)) {
            int mRow = obj.getRow() + row;
            int mCol = obj.getCol() + col;
            // 将移动到的方格
            GameObject movingObj = tileObject[mRow][mCol];
            // 当前方格设置成将移动到的方格
            tileObject[obj.getRow()][obj.getCol()] = movingObj;
            tile[obj.getRow()][obj.getCol()] = movingObj.getFlag();
            // 移动
            obj.move(row, col);
            // 移动方格设置成原来方格
            tileObject[obj.getRow()][obj.getCol()] = obj;
            tile[obj.getRow()][obj.getCol()] = obj.getFlag();
        }
    }

    /**
     * 地图内元素受伤
     *
     * @param woundObjects
     * @return 死亡单位
     */
    private List<GameObject> wound(List<WoundObject> woundObjects) {
        List<GameObject> deadList = Lists.newArrayList();
        for (WoundObject wound : woundObjects) {
            if (!ifInner(wound.getRow(), wound.getCol())) {
                continue;
            }
            GameObject gameObject = tileObject[wound.getRow()][wound.getCol()];
            if (gameObject instanceof MovableObject) {
                boolean dead = gameObject.wound(wound);
                if (dead) {
                    deadList.add(gameObject);
                }
            }
        }
        return deadList;
    }

    public void addPlayer(PlayerGameObject playerGameObject) {
        if (movableObjectList == null) {
            movableObjectList = Lists.newArrayList();
        }
        movableObjectList.add(playerGameObject);
    }

    /**
     * 启动地图
     */
    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void addCmd(GameCmdObject object) {
        try {
            cmdObjectBlockingQueue.offer(object, 10, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("中断", e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                GameCmdObject object = cmdObjectBlockingQueue.poll(10, TimeUnit.MILLISECONDS);
                if (object == null) {
                    continue;
                }
                execute(object);
            } catch (InterruptedException e) {
                log.error("中断", e);
            } catch (NullPointerException e) {
                // log.error("空指针", e);
            }
        }
    }

    /**
     * 执行命令
     *
     * @param cmd
     */
    private void execute(GameCmdObject cmd) {
        Controller controller = cmd.getController();
        PlayerGameObject player = cmd.getPlayer();

        GameCommandTypeEnum cmdType = cmd.getCmdType();
        if (GameCommandTypeEnum.refresh.isEqual(cmdType.getCode())) {
            controller.send(toString());
            return;
        } else if (GameCommandTypeEnum.agg.isEqual(cmdType.getCode())) {
            List<GameObject> deadObjs = wound(player.agg());
            clearDeadObj(deadObjs);
            return;
        }

        boolean ifMoving = player.moving(cmdType.getRow(), cmdType.getCol());
        if (!ifMoving) {
            controller.send(toString());
            return;
        }
        boolean canMoving = canMoving(player.getRow() + cmdType.getRow(), player.getCol() + cmdType.getCol());
        if (canMoving) {
            move(player, cmdType.getRow(), cmdType.getCol());
            controller.send(toString());
        }
    }

    /**
     * 清理尸体
     *
     * @param deadObjList
     */
    private void clearDeadObj(List<GameObject> deadObjList) {
        deadObjList.forEach(dead -> {
            GameObject newObj = GameObjectFactory.newInstance(dead.getRow(), dead.getCol(), GameObjectTypeEnum.floor);
            tileObject[dead.getRow()][dead.getCol()] = newObj;
            tile[dead.getRow()][dead.getCol()] = newObj.getFlag();

            List<GameObject> listNew = objectMapping.get(newObj.getType().getCode());
            if (listNew == null) {
                listNew = Lists.newArrayList();
                objectMapping.put(newObj.getType().getCode(), listNew);
            }
            listNew.add(newObj);

//            movableObjectList.remove(dead);
//            List<GameObject> listDead = objectMapping.get(dead.getType().getCode());
//            if (!CollectionUtils.isEmpty(listDead)) {
//                listDead.remove(dead);
//            }
        });
    }
}
