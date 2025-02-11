package com.games.framework.component.messagekit;

import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * "Message"最终监听处理方法的信息封装
 *
 * @author liu xuan jie
 */
@Getter
public class MessageListenerEntry {

    /**
     * 最终处理方法所在类名，简称
     */
    private String className;

    /**
     * 最终处理方法的原始方法
     */
    private Method listenMethod;

    /**
     * 最终处理方法标识的注解信息
     */
    private MessageListener annotation;

    /**
     * 最终处理方法的函数式接口（动态代理）
     */
    private BiConsumer<Object, Object> proxyConsumer;

    private MessageListenerEntry() {
    }

    public static MessageListenerEntry of(Class<?> handler, Method listenMethod,
                                          MessageListener annotation, BiConsumer<Object, Object> proxyConsumer) {
        MessageListenerEntry entry = new MessageListenerEntry();
        entry.className = handler.getSimpleName();
        entry.listenMethod = listenMethod;
        entry.annotation = annotation;
        entry.proxyConsumer = proxyConsumer;
        return entry;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (Objects.nonNull(this.className)) {
            stringBuilder.append("class:").append(this.className).append(",");
        }

        if (Objects.nonNull(this.listenMethod)) {
            stringBuilder.append("method:").append(this.listenMethod.getName()).append(",");
        }

        if (Objects.nonNull(this.annotation)) {
            stringBuilder.append("listen key:").append(this.annotation.value()).append(";");
        }
        return stringBuilder.toString();
    }
}