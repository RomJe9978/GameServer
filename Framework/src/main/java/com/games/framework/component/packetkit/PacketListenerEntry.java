package com.games.framework.component.packetkit;

import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * "Packet"最终监听处理方法的信息封装
 *
 * @author liu xuan jie
 */
@Getter
public class PacketListenerEntry {

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
    private PacketListener annotation;

    /**
     * 最终处理方法的函数式接口（动态代理）
     */
    private BiConsumer<Object, Object> proxyConsumer;

    private PacketListenerEntry() {
    }

    public static PacketListenerEntry of(Class<?> handler, Method listenMethod,
                                         PacketListener annotation, BiConsumer<Object, Object> proxyConsumer) {
        PacketListenerEntry entry = new PacketListenerEntry();
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