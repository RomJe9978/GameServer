package com.games.context;

import com.games.framework.component.configuration.ConfigContext;
import com.games.framework.component.configuration.PriorityPropertyProxy;
import com.games.log.Log;
import com.romje.component.clock.ClockContext;
import com.romje.component.clock.OffsetClock;
import com.romje.constants.TimeConst;
import com.romje.utils.DateUtil;

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
        Log.LOGIC.info("[GameContext] Init default start do!");

        PriorityPropertyProxy propertyProxy = PriorityPropertyProxy.newInstance();
        boolean result = propertyProxy.initConfig(GameStaticParam.listConfigFileName());
        ConfigContext.setConfiguration(propertyProxy);

        result &= initClock();

        Log.LOGIC.info("[GameContext] Server ip:{}, port:{}", ConfigContext.getConfiguration().getLong("server.serverId"), ConfigContext.getConfiguration().getLong("server.port"));
        return result;
    }

    private boolean initClock() {
        Log.LOGIC.info("[Clock] Init game clock start!");

        // todo:用枚举常量去优化，不要每次都去读取key
        long configOffset = ConfigContext.getConfiguration().getLong("server.time.offset", 0L);
        Log.LOGIC.info("[Clock] Game config time offset minutes is:{}", configOffset);

        long millis = Math.multiplyExact(configOffset, TimeConst.MILLIS_OF_MINUTE);
        ClockContext.setClock(OffsetClock.newInstance(millis));
        Log.LOGIC.info("[Clock] Init clock finish: {}!", ClockContext.getClock());

        long curTime = ClockContext.getClock().currentTimeMillis();
        Log.LOGIC.info("[Clock] Init clock finish,cur clock time:{}", DateUtil.parseDate(curTime));
        return true;
    }
}
