package com.games.framework.component.eventkit;

import java.lang.annotation.*;

/**
 * 标识监听事件的最终处理方法
 * <p> 注解标识的方法，必须是{@code public static}的，可以供外部访问的，
 * 使用{@code static}是为了直接进行方法调用，而不用对类进行实例化。
 *
 * @author liu xuan jie
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventListener {

    /**
     * 标识当前处理方法，监听哪些事件，该参数必须指定
     *
     * <p> 相当于是{@code EventKey}，建议使用枚举或者常量类进行统一管理维护。
     * <p> 使用{@code value}作为命名，是为了使用处省略字段，直接对注解赋值。
     */
    int[] value();
}