package com.games.business.game.world;

import com.games.framework.engine.service.AbstractService;
import com.romje.utils.EmptyUtil;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * “世界Service”，相当于系统级别的逻辑处理
 *
 * @author liu xuan jie
 */
public class WorldService extends AbstractService {

    /**
     * 所有WorldModule的快速映射集合
     * K：统一管理模块类型，V：具体的模块对象实例
     */
    private Map<WorldModuleType, AbstractWorldModule> moduleMap;

    /**
     * 所有WorldModule快速遍历集合，有序（枚举定义顺序）
     */
    private AbstractWorldModule[] modules;

    public WorldService() {
        super(1);
    }

    @Override
    public boolean init() {
        // 初始化所有World module
        WorldModuleType[] moduleTypes = WorldModuleType.values();
        this.moduleMap = new HashMap<>(moduleTypes.length);
        this.modules = new AbstractWorldModule[moduleTypes.length];

        // 先创建出来，并且统一维护，在这里统一关联Service
        for (int i = 0, iSize = moduleTypes.length; i < iSize; i++) {
            AbstractWorldModule newModule = moduleTypes[i].getCreator().create();
            newModule.setWorldService(this);

            this.moduleMap.put(moduleTypes[i], newModule);
            this.modules[i] = newModule;
        }

        // 最后统一初始化module，不要创建一个初始化一个
        // 因为module之间的初始化可能有关联，保证所有module已经存在之后，统一初始化
        for (AbstractWorldModule module : this.modules) {
            module.init();
        }
        return true;
    }

    @Override
    public void tickLogic(long millisTimestamp) {
        for (AbstractWorldModule module : this.modules) {
            module.tick(millisTimestamp);
        }
    }

    /**
     * {@link #getWorldModule(WorldModuleType)}
     */
    public <T extends AbstractWorldModule> T getWorldModule(int moduleType) {
        WorldModuleType worldModuleType = WorldModuleType.valueOf(moduleType);
        Objects.requireNonNull(worldModuleType);
        return getWorldModule(worldModuleType);
    }

    /**
     * @param moduleType 模块类型，不允许为{@code null}
     * @param <T>        对应模块枚举的{@code AbstractWorldModule}实例
     * @return 只有没有对应模块的时候返回{@code null}，正常逻辑不会出现{@code null}
     */
    public <T extends AbstractWorldModule> T getWorldModule(@NonNull WorldModuleType moduleType) {
        if (EmptyUtil.isEmpty(this.moduleMap)) {
            return null;
        }

        AbstractWorldModule worldModule = this.moduleMap.get(moduleType);
        @SuppressWarnings("unchecked")
        T module = (T) worldModule;
        return module;
    }
}