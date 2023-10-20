package com.games.context;

import com.games.GameBoot;
import com.games.log.GameLog;
import com.romje.clock.ClockContext;
import com.romje.clock.DefaultClock;
import com.romje.configuration.ConfigContext;
import com.romje.configuration.PriorityPropertyProxy;
import com.romje.constants.DateConst;
import com.romje.constants.TimeConst;
import com.romje.utils.EmptyUtil;
import com.romje.utils.TimeUtil;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

/**
 * 游戏全局上下文容器
 *
 * @author RomJe
 */
public class GameContext {
    /**
     * 单例模式
     */
    private final static GameContext INSTANCE = new GameContext();

    private GameContext() {
    }

    public static GameContext getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化，默认走{@link GameStaticParam}中的静态参数
     *
     * @return {@code false} if occurs error or exception
     */
    public boolean initDefault() {
        GameLog.logic().info("[GameContext] Init default start do!");

        // 初始化配置文件
        boolean result = initConfiguration(GameStaticParam.listConfigFileName());

        // 初始化时钟
        result &= initClock();

        GameLog.logic().info("[GameContext] Init default start finish!");
        return result;
    }

    private boolean initConfiguration(List<String> fileNameList) {
        if (EmptyUtil.isEmpty(fileNameList)) {
            GameLog.logic().info("[Configuration] Load configuration, file list is empty!");
            return true;
        }

        // 按照顺序添加即可
        PriorityPropertyProxy propertyProxy = PriorityPropertyProxy.newInstance();
        for (String fileName : fileNameList) {
            try (InputStream inputStream = GameBoot.class.getClassLoader().getResourceAsStream(fileName)) {
                YAMLConfiguration config = new YAMLConfiguration();
                config.read(inputStream);
                propertyProxy.appendProxy(config);
            } catch (ConfigurationException | IOException e) {
                GameLog.logic().error("[Configuration] Load config file:{} exception:{}", fileName, e.getMessage());
                return false;
            }
        }

        ConfigContext.setConfiguration(propertyProxy);
        GameLog.logic().info("[Configuration] Load config file success:{}!", fileNameList);
        return true;
    }

    private boolean initClock() {
        GameLog.logic().info("[Clock] Init game clock start!");
        final ZoneId systemZone = ZoneId.systemDefault();

        // 配置的偏移量
        long configOffset = ConfigContext.getConfiguration().getLong("server.time.offset", 0L);
        GameLog.logic().info("[Clock] Game config time offset minutes is:{}", configOffset);

        // 设置时钟
        long millis = Math.multiplyExact(configOffset, TimeConst.MILLIS_OF_MINUTE);
        ClockContext.setClock(DefaultClock.newInstance(millis));
        GameLog.logic().info("[Clock] Init clock finish: {}!", ClockContext.getClock());

        long curTime = ClockContext.getClock().currentTimeMillis();
        GameLog.logic().info("[Clock] Init clock finish,cur clock time:{}", TimeUtil.parseTime(curTime, systemZone, DateConst.YYYY_MM_DD_SPACE_HH_MM_SS));
        return true;
    }
}
