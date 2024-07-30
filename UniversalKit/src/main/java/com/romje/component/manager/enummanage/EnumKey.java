package com.romje.component.manager.enummanage;

import java.lang.annotation.*;

/**
 * 此注解用于标记枚举字段，代表该字段可以作为枚举{@code key}
 *
 * <pre>
 *     1.可以根据{@code key}的值映射唯一的枚举实例
 *     2.可以查重，所有枚举实例的标识{@code key}的字段值不允许重复
 * </pre>
 *
 * @author liu xuan jie
 * @Documented 标记这个注解将被包含在文档中。
 * @Target(ElementType.FIELD) 指定这个注解只能应用于字段上。
 * @Retention(RetentionPolicy.RUNTIME) 表示这个注解在运行时是可见的，可以被反射读取。
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumKey {

}