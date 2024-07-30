package com.romje.component.checker.constcheck;

import java.lang.annotation.*;

/**
 * 此注解用于标记常量类，表示指定常量类中的所有常量的唯一性，没有重复。
 *
 * @author liu xuan jie
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstUnique {

}