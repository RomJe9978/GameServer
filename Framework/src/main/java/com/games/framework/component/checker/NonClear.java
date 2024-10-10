package com.games.framework.component.checker;

import java.lang.annotation.*;

/**
 * 字段注解标识，代表指定字段信息不需要被清除
 *
 * @author liu xuan jie
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NonClear {
}