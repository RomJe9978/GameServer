package com.romje.component.pool.object;

/**
 * 统一“对象创建”接口
 *
 * <p> 提供统一对象创建接口，可以与其他组件配合使用
 * <p> 避免简单粗暴的在运行时通过反射创建新的对象实例
 *
 * @author liu xuan jie
 */
public interface IObjectFactory<T> {

    /**
     * 保证创建一个新的实例
     * <p> 注意该接口关注的是创建过程，而非结果
     *
     * @return 一个全新的实例，对应泛型，不允许为{@code null}
     */
    T create();
}