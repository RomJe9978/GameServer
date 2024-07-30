package com.romje.component.checker.enumcheck;

import java.lang.annotation.*;

/**
 * 此注解用于标记枚举字段，确保同一枚举的所有实例的该字段唯一性。
 *
 * @author liu xuan jie
 * @Documented 标记这个注解将被包含在文档中。
 * @Target(ElementType.FIELD) 指定这个注解只能应用于字段上。
 * @Retention(RetentionPolicy.RUNTIME) 表示这个注解在运行时是可见的，可以被反射读取。
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumUnique {

}