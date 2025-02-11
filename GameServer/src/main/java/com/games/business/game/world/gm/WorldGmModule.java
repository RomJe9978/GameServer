package com.games.business.game.world.gm;

import com.games.business.game.world.AbstractWorldModule;
import com.games.business.game.world.WorldModuleType;

/**
 * GM相关处理模块
 *
 * @author liu xuan jie
 */
public class WorldGmModule extends AbstractWorldModule {

    public WorldGmModule() {
    }

    @Override
    public WorldModuleType moduleType() {
        return WorldModuleType.GM;
    }

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public void tick(long millisTimestamp) {
    }
}