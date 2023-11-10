package com.games.context;

import com.games.log.GameLoggers;
import com.romje.component.clock.ClockContext;
import com.romje.component.clock.DefaultClock;
import com.romje.component.configuration.ConfigContext;
import com.romje.component.configuration.PriorityPropertyProxy;
import com.romje.constants.TimeConst;
import com.romje.utils.EmptyUtil;
import com.romje.utils.TimeUtil;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
        GameLoggers.system().info("[GameContext] Init default start do!");
        boolean result = initConfiguration(GameStaticParam.listConfigFileName());
        result &= initClock();
        GameLoggers.system().info("[GameContext] Init default start finish!");
        return result;
    }

    private boolean initConfiguration(List<String> fileNameList) {
        if (EmptyUtil.isEmpty(fileNameList)) {
            GameLoggers.system().info("[Configuration] Load configuration, file list is empty!");
            return true;
        }

        PriorityPropertyProxy propertyProxy = PriorityPropertyProxy.newInstance();
        for (String fileName : fileNameList) {
            try (InputStream inputStream = GameContext.class.getClassLoader().getResourceAsStream(fileName)) {
                YAMLConfiguration config = new YAMLConfiguration();
                config.read(inputStream);
                propertyProxy.appendProxy(config);
            } catch (ConfigurationException | IOException e) {
                GameLoggers.system().error("[Configuration] Load config file:{} exception:{}", fileName, e.getMessage());
                return false;
            }
        }

        ConfigContext.setConfiguration(propertyProxy);
        GameLoggers.system().info("[Configuration] Load config file success:{}!", fileNameList);
        return true;
    }

    private boolean initClock() {
        GameLoggers.system().info("[Clock] Init game clock start!");

        long configOffset = ConfigContext.getConfiguration().getLong("server.time.offset", 0L);
        GameLoggers.system().info("[Clock] Game config time offset minutes is:{}", configOffset);

        long millis = Math.multiplyExact(configOffset, TimeConst.MILLIS_OF_MINUTE);
        ClockContext.setClock(DefaultClock.newInstance(millis));
        GameLoggers.system().info("[Clock] Init clock finish: {}!", ClockContext.getClock());

        long curTime = ClockContext.getClock().currentTimeMillis();
        GameLoggers.system().info("[Clock] Init clock finish,cur clock time:{}", TimeUtil.parseTime(curTime));
        return true;
    }
}
