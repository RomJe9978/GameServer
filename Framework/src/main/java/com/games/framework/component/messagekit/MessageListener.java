package com.games.framework.component.messagekit;

import java.lang.annotation.*;

/**
 * 标识监听"Message"的最终处理方法
 *
 * <p> 注解标识的方法，必须是{@code public static}的，可以供外部访问的，
 * 使用{@code static}是为了直接进行方法调用，而不用对类进行实例化。
 *
 * <p> 一个监听方法，仅对应唯一一个网络包的处理逻辑。
 *
 * @author liu xuan jie
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MessageListener {

    /**
     * <p> 相当于是{@code MessageKey}，建议使用枚举或者常量类进行统一管理维护。
     * <p> 使用{@code value}作为命名，是为了使用处省略字段，直接对注解赋值。
     *
     * @return 无默认值，业务层必须指定
     */
    int value();
}