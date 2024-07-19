package com.games.game.bootstrap;

import com.games.game.constant.ErrorCode;

/**
 * 启动器。不允许实例化
 *
 * @author liu xuan jie
 */
public final class Bootstrapper {

    private Bootstrapper() {
    }

    public static void boot(String[] args) {
        ErrorCode errorCode = ErrorCode.NON_ERROR;
    }
}