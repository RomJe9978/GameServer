package com.romje.component.pool.object;

import java.util.ArrayDeque;
import java.util.IdentityHashMap;
import java.util.Objects;

/**
 * 默认“单线程对象池”
 *
 * @author liu xuan jie
 */
public class DefaultObjectPool<T extends Reusable> implements IObjectPool<T> {

    /**
     * 对象池参数
     */
    private final ObjectPoolConfig config;

    /**
     * 实际对象池容器，高效双端队列
     */
    private final ArrayDeque<T> queue;

    /**
     * 池中对象集合，用于检查指定对象是否是池中对象，这个非常重要
     * 必须严格保证对象池内部管理的一致性，以及保障对象生命周期的正确性
     * 强制使用{@code IdentityHashMap},因为需要使用内存地址判断对象是否一致
     */
    private final IdentityHashMap<T, Boolean> poolObjectMap;

    /**
     * 可复用对象的统一实例创建接口，不允许为{@code null}
     */
    private final IObjectFactory<T> factory;

    /**
     * 从对象池中获取的“正在使用，且还没有归还”的池化对象数量
     */
    private int borrowedCount;

    /**
     * @param config  对象池参数
     * @param factory 可复用对象创建器
     */
    public DefaultObjectPool(ObjectPoolConfig config, IObjectFactory<T> factory) {
        Objects.requireNonNull(config);
        Objects.requireNonNull(factory);

        this.config = config;
        this.factory = factory;
        this.queue = new ArrayDeque<>();
        this.poolObjectMap = new IdentityHashMap<>();

        // 是否需要初始化
        this.tryAddPoolObject(this.config.getMinIdle());
    }

    @Override
    public T borrowObject() {
        if (this.borrowedCount >= this.config.getMaxCount()) {
            return null;
        }

        // 没有达到最大值，并且池内为空，则创建新的即可
        if (this.queue.isEmpty()) {
            this.tryAddPoolObject(1);
        }

        // 池内不为空，直接获取
        T instance = this.queue.poll();
        Objects.requireNonNull(instance);
        instance.clear();
        instance.init();
        this.borrowedCount++;

        // 最小空闲数
        this.tryAddPoolObject(this.config.getMinIdle() - this.queue.size());
        return instance;
    }

    @Override
    public void returnObject(T object) {
        Objects.requireNonNull(object);
        if (!isPoolObject(object)) {
            return;
        }

        // 这一步不可能出现，作为容错校验一下
        if (this.queue.size() + this.borrowedCount > this.config.getMaxCount()) {
            throw new RuntimeException("The returned object exceeds the maximum value.");
        }

        // 先设置已经还回来了
        this.borrowedCount--;

        // 检查最大空闲
        if (this.queue.size() < this.config.getMaxIdle()) {
            object.clear();
            this.queue.offer(object);
        } else {
            // 直接销毁对象
            object.clear();
            this.poolObjectMap.remove(object);
        }
    }

    @Override
    public boolean isPoolObject(T object) {
        return Objects.nonNull(object) && this.poolObjectMap.containsKey(object);
    }

    @Override
    public int size() {
        return this.queue.size();
    }

    @Override
    public ObjectPoolConfig getConfig() {
        return this.config;
    }

    private void tryAddPoolObject(int addCount) {
        if (addCount <= 0) {
            return;
        }

        // 保证不能超过池中对象最大值
        addCount = Math.min(addCount, this.config.getMaxCount() - this.queue.size() - this.borrowedCount);
        for (int i = 0; i < addCount; i++) {
            T instance = this.factory.create();
            Objects.requireNonNull(instance);
            this.queue.add(instance);
            this.poolObjectMap.put(instance, Boolean.TRUE);
        }
    }
}