package com.games.business.game.component;

import java.lang.annotation.*;

/**
 * 定义在类级别上的注解，用于指示该类或接口是一个“日志记录器”的仓库
 *
 * <p>通过在类上应用这个注解，可以集中管理日志记录器，而不需要在每个需要日志记录
 * 的地方手动获取。这提供了一种更高级别的抽象，使得日志记录器更加集中和易于管理。
 *
 * @author liu xuan jie
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoggerRepository {

}