package com.games.framework.component.packetkit;

import java.lang.annotation.*;

/**
 * 标识指定类是一个网络包处理者,内部有对网络包监听的处理方法
 * <p> 注解标识的类，必须是{@code public}的，可以供外部访问的
 *
 * @author liu xuan jie
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PacketHandler {

}