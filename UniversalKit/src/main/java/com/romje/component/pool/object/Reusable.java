package com.romje.component.pool.object;

/**
 * 可复用对象接口
 *
 * @author liu xuan jie
 */
public interface Reusable {

    /**
     * 可复用对象每次重新使用的时候的初始化操作
     */
    void init();

    /**
     * 可复用对象用完之后的清理操作，保证没有脏数据
     * 该接口最好配合检查工具，保证开发者不会遗漏某些字段，尤其新增的时候
     */
    void clear();
}