package com.games.framework.component.eventkit;

import lombok.Getter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Event事件监听最终对应的处理方法信息封装
 *
 * @author liu xuan jie
 */
@Getter
public class EventListenerEntry implements Comparable<EventListenerEntry> {

    /**
     * 事件监听，最终针对事件的处理方法所在的类
     */
    private Class<?> listenClass;

    /**
     * 事件监听，最终针对事件的处理方法的原始方法
     */
    private Method listenMethod;

    /**
     * 事件监听，最终处理方法标识的注解信息
     */
    private EventListener annotation;

    /**
     * 事件监听，最终处理方法的函数式接口（动态代理）
     */
    private Consumer<Object> proxyConsumer;

    private EventListenerEntry() {
    }

    public static EventListenerEntry newInstance(Class<?> listenClass, Method listenMethod,
                                                 EventListener annotation, Consumer<Object> consumer) {
        EventListenerEntry instance = new EventListenerEntry();
        instance.listenClass = listenClass;
        instance.listenMethod = listenMethod;
        instance.annotation = annotation;
        instance.proxyConsumer = consumer;
        return instance;
    }

    /**
     * 与另一个{@link EventListenerEntry}进行比较
     *
     * @param other the object to be compared. Not null.
     * @return {@code -1}代表当前实例比另一个实例小，{@code 0}代表一样，{@code 1}代表当前实例更大。
     */
    @Override
    public int compareTo(EventListenerEntry other) {
        int curPriority = this.annotation.priority().getPriority();
        int curInnerOrder = this.annotation.innerOrder();

        int otherPriority = other.annotation.priority().getPriority();
        int otherInnerOrder = other.annotation.innerOrder();

        if (curPriority == otherPriority) {
            return Integer.compare(curInnerOrder, otherInnerOrder);
        } else if (curPriority < otherPriority) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (Objects.nonNull(this.listenClass)) {
            stringBuilder.append("class:").append(this.listenClass.getSimpleName()).append(",");
        }

        if (Objects.nonNull(this.listenMethod)) {
            stringBuilder.append("method:").append(this.listenMethod.getName()).append(",");
        }

        if (Objects.nonNull(this.annotation)) {
            stringBuilder.append("listen keys:").append(Arrays.toString(this.annotation.value())).append(",");
            stringBuilder.append("priority:").append(this.annotation.priority().getPriority()).append(",");
            stringBuilder.append("inner order").append(this.annotation.innerOrder());
        }
        return stringBuilder.toString();
    }
}