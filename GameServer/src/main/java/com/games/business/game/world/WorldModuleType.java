package com.games.business.game.world;

import com.games.business.game.world.gm.WorldGmModule;
import com.games.framework.engine.module.IModuleCreator;
import com.romje.component.manager.enumlookup.EnumKey;
import com.romje.component.manager.enumlookup.EnumLookup;
import lombok.Getter;
import lombok.NonNull;

/**
 * “WorldModule”的所有标识类型枚举，统一定义管理
 * 约定：当前枚举定义顺序即为所有Module的优先级处理顺序
 *
 * @author liu xuan jie
 */
@Getter
public enum WorldModuleType {

    GM(1, WorldGmModule::new, "World gm logic module"),
    ;

    /**
     * 唯一类型标识，类型不代表顺序，只做标识
     */
    @EnumKey
    private final int type;

    /**
     * 对应类型的模块创建接口
     */
    private final IModuleCreator<? extends AbstractWorldModule> creator;

    /**
     * 简单描述信息
     */
    private final String describe;

    WorldModuleType(int type, @NonNull IModuleCreator<? extends AbstractWorldModule> creator, @NonNull String describe) {
        this.type = type;
        this.creator = creator;
        this.describe = describe;
    }

    /**
     * @return 参数没有对应枚举时，返回{@code null}
     */
    public static WorldModuleType valueOf(int type) {
        return EnumLookup.INSTANCE.getEnum(WorldModuleType.class, type);
    }
}