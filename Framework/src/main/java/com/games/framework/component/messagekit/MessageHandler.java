package com.games.framework.component.messagekit;

import java.lang.annotation.*;

/**
 * 标识指定类是一个消息处理者,内部有对消息监听的处理方法
 * <p> 统一“Message”理解：所有服务器架构内部服务、线程之间的通讯单元
 * <p> 注解标识的类，必须是{@code public}的，可以供外部访问的
 *
 * @author liu xuan jie
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageHandler {

}