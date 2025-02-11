package com.games.business.game.world;

import com.games.framework.engine.module.IModule;
import com.games.framework.engine.service.AbstractService;
import com.games.framework.engine.service.AbstractServiceMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * 针对“WorldService”下的具体业务逻辑的最上层抽象
 *
 * @author liu xuan jie
 */
@Getter
public abstract class AbstractWorldModule implements IModule {

    /**
     * 当前业务模块所依附的“Service”，不允许为{@code null}
     */
    @Setter
    private WorldService worldService;

    /**
     * 实现类强制统一关联枚举类型
     *
     * @return 不允许为{@code null}
     */
    public abstract WorldModuleType moduleType();

    public void sendMessage(AbstractService targetService, AbstractServiceMessage message) {
        this.worldService.sendMessage(targetService, message);
    }
}