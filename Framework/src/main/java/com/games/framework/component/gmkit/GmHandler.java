package com.games.framework.component.gmkit;

/**
 * 标识一个Gm命令处理类
 * <p> 注解标识的类，必须是{@code public}的，可以供外部访问的
 *
 * @author liu xuan jie
 */
public @interface GmHandler {

    /**
     * GM命令标识，唯一字符串，建议统一常量或者枚举维护
     *
     * @return 不允许为{@code null}
     */
    String value();
}