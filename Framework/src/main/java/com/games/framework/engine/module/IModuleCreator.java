package com.games.framework.engine.module;

/**
 * {@code Module}创建接口
 * 该接口提供统一的创建方式，可以统一管理和维护，代替反射时动态创建
 *
 * @author liu xuan jie
 */
public interface IModuleCreator<T extends IModule> {

    /**
     * @return 创建出一个{@code module}的统一接口
     */
    T create();
}