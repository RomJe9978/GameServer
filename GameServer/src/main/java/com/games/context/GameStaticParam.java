package com.games.context;

import java.util.Arrays;
import java.util.List;

/**
 * 游戏静态参数，统一管理
 *
 * @author RomJe
 */
public class GameStaticParam {
    /**
     * 游戏配置文件列表
     */
    private final static List<String> CONFIG_FILE_NAME_LIST = Arrays.asList("config.yaml", "config-base.yaml");

    /**
     * @return 配置文件的文件名列表
     */
    public static List<String> listConfigFileName() {
        return CONFIG_FILE_NAME_LIST;
    }
}
