package com.games;

import com.games.context.GameContext;
import com.games.log.GameLog;

/**
 * @author romje
 */
public class GameBoot {
    /**
     * 启动服务器，启动过程
     *
     * @return {@code false}标识启动失败
     */
    public static boolean boot() {
        // 游戏上下文初始化
        boolean bootResult = GameContext.getInstance().initDefault();
        return bootResult;
    }

    public static void main(String[] args) {
        try {
            boolean bootResult = boot();
            if (!bootResult) {
                GameLog.logic().error("Game server boot fail!");
                System.exit(-1);
            }
        } catch (Exception e) {
            GameLog.logic().error("Game server boot fail!");
            System.exit(-1);
        }
    }
}