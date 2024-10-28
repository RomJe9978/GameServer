package com.games.business.game.component;

import java.lang.annotation.*;

/**
 * 作用于类的字段上，用于标识指定“Logger”需要被检查
 *
 * @author liu xuan jie
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoggerCheck {

    /**
     * Logger名称，必许指定
     */
    String name();

    /**
     * {@link #name()}该名称是否必许在配置文件中存在
     *
     * <p>配置中如果没有该名称，其实会默认使用{@code root}
     * <p>{@code required == false},则表示默认使用{@code root}也算作配置存在
     * <p>{@code required == true}，则表示名称必许在配置中有配置，使用{@code root}不算有配置
     */
    boolean required() default true;
}