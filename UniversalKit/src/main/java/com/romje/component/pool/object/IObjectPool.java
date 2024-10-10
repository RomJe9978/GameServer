package com.romje.component.pool.object;

/**
 * “对象池”接口
 *
 * @author liu xuan jie
 */
public interface IObjectPool<T extends Reusable> {

    /**
     * 从对象池中获取一个可复用对象。
     *
     * @return 如果池中没有可复用对象且已经到最大值，返回{@code null}。
     */
    T borrowObject();

    /**
     * 将可复用对象放回对象池中，非池中对象不允许放回！
     * 必须严格保证对象池内部管理的一致性，以及保障对象生命周期的正确性！
     *
     * @param object 不允许为{@code null}。
     */
    void returnObject(T object);

    /**
     * 是否是池中对象，实现类自己保证检查方法
     *
     * @param object 不允许为{@code null}
     * @return 只有是池中创建的对象，才会返回{@code true}
     */
    boolean isPoolObject(T object);

    /**
     * 当前对象池中可以直接使用的池化对象数量
     * 是当前存在在池中的数量，可以创建但还没有创建的不算，已经被占用的不算
     */
    int size();

    /**
     * @return 不允许为{@code null}
     */
    ObjectPoolConfig getConfig();
}