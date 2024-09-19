package com.games.framework.component.eventkit;

import com.games.framework.constants.PriorityEnum;

import java.lang.annotation.*;

/**
 * 标识监听事件的最终处理方法
 *
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

    /**
     * 指定优先级，优先级决定当前监听者执行顺序
     *
     * <p> 优先级越高，执行越靠前，{@link PriorityEnum#HIGHEST}要比{@link PriorityEnum#LOWEST}更早执行
     * <p> 使用枚举限制使用，保证优先级级别可控，禁止无限泛滥
     *
     * @return 不会为{@code null}
     */
    PriorityEnum priority() default PriorityEnum.COMMON;

    /**
     * 当{@link #priority()}相同的时候，同级别内部的执行顺序
     *
     * <p> 该字段大多数情况用不到，只是为了方便业务层扩展使用
     * <p> 该值越小，代表执行顺序越靠前
     * <p> 该顺序开放给业务层管理，业务层保证{@code 0}是普通顺序即可
     * <p> 业务层应该使用枚举或者统一常量类进行统一管理，避免不可控
     *
     * @return 默认是{@code 0}，大部分都是默认情况。
     */
    int innerOrder() default 0;
}