package com.romje.component.pool.object;

import com.romje.utils.MathUtil;

/**
 * 对象池配置参数
 *
 * @author liu xuan jie
 */
public class ObjectPoolConfig {

    /**
     * 最大容量，2的次幂
     * 如果赋值不是2的次幂，则会强制转换成最近的下一个2的次幂
     */
    private int maxCount;

    /**
     * 最小空闲数量，当可用池化对象少于该值，则直接自动补上
     * 如果不需要该参数，默认0即可，代表不需要自动创建，直到没有可用池化对象
     */
    private int minIdle;

    /**
     * 最大空闲数量，当可用池化对象大于该值，则会自动销毁释放对象，避免一直有大量的空
     * 闲对象存在于内存中。如果不需要该参数，默认{@link #maxCount}一致，代表不自动回收
     */
    private int maxIdle;

    private ObjectPoolConfig(int maxCount) {
        this.maxCount = MathUtil.nextPowerOf2(maxCount);
        this.minIdle = 0;
        this.maxIdle = this.maxCount;
    }

    public static ObjectPoolConfig newInstance(int maxCount) {
        return new ObjectPoolConfig(maxCount);
    }

    public ObjectPoolConfig setMinIdle(int minIdle) {
        this.minIdle = minIdle;
        return this;
    }

    public ObjectPoolConfig setMaxIdle(int maxIdle) {
        this.maxCount = maxIdle;
        return this;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public int getMaxIdle() {
        return maxIdle;
    }
}