package com.games.framework.component.eventkit;

import com.games.framework.utils.ByteBuddyUtil;
import com.games.framework.utils.ScanUtil;
import com.romje.model.BoolResult;
import com.romje.utils.EmptyUtil;
import com.romje.utils.ReflectionUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 事件发布器，用于事件机制的注册，分发
 *
 * @author liu xuan jie
 */
public enum EventDispatcher {

    INSTANCE;

    private final Map<Class<?>, Consumer<Object>> eventListenerMap = new HashMap<>();

    private final Map<Class<?>, List<Consumer<Object>>> eventListenersMap = new HashMap<>();

    public BoolResult publish(Object param) {
        Consumer<Object> consumer = this.eventListenerMap.get(param.getClass());
        if (Objects.isNull(consumer)) {
            return BoolResult.success();
        }

        consumer.accept(param);
        return BoolResult.success();
    }

    /**
     * 扫描指定包中的所有事件监听类（使用了{@link EventHandler}标识的类），
     * 注册这些监听类内的所有事件监听方法（使用了{@link EventListener}标识的方法）。
     *
     * @param packageName 如果扫描不到任何监听类，返回成功。
     * @return 任何错误或者异常返回{@code false}，失败信息在{@link BoolResult#message()}中。
     */
    public BoolResult registerEventListener(String packageName) {
        // 扫包使用指定注解EventHandler的监听类
        List<Class<?>> classList = ScanUtil.scanAnnotationAsList(packageName, EventHandler.class);
        if (EmptyUtil.isEmpty(classList)) {
            return BoolResult.success();
        }
        return this.registerEventListener(classList);
    }

    /**
     * 注册一组监听类中的，所有监听方法（扫描{@link EventListener}注解）。
     *
     * @param classList 一组监听类，如果列表为空，则返回成功。
     * @return 任何错误或者异常返回{@code false}，失败信息在{@link BoolResult#message()}中。
     */
    public BoolResult registerEventListener(List<Class<?>> classList) {
        for (Class<?> clazz : classList) {
            BoolResult singleResult = registerEventListener(clazz);
            if (singleResult.isFail()) {
                return singleResult;
            }
        }
        return BoolResult.success();
    }

    /**
     * 注册单个监听类中的，所有监听方法（扫描{@link EventListener}注解）。
     *
     * @param eventHandleClass 单个监听类，不允许为{@code null}。
     * @return 任何错误或者异常返回{@code false}，失败信息在{@link BoolResult#message()}中。
     */
    public BoolResult registerEventListener(Class<?> eventHandleClass) {
        List<Method> annotatedMethods = ReflectionUtil.getMethodsWithAnnotation(eventHandleClass, EventListener.class);
        if (EmptyUtil.isEmpty(annotatedMethods)) {
            return BoolResult.success();
        }

        for (Method method : annotatedMethods) {
            BoolResult result = this.registerListenMethod(eventHandleClass, method);
            if (result.isFail()) {
                return result;
            }
        }
        return BoolResult.success();
    }

    /**
     * 最底层注册方法，将监听者的处理方法，注册到分发器中。
     *
     * @param eventHandleClass 监听处理方法所在类，不允许为{@code null}。
     * @param method           最终的监听处理方法，不允许为{@code null}。必须标识{@link EventListener}注解。
     * @return 任何错误或者异常返回{@code false}，失败信息在{@link BoolResult#message()}中。
     */
    private BoolResult registerListenMethod(Class<?> eventHandleClass, Method method) {
        // 监听方法必须是静态的
        if (ReflectionUtil.nonStaticMethod(method)) {
            return BoolResult.fail("event listener method non static: " + method.getName());
        }

        Annotation annotation = method.getAnnotation(EventListener.class);
        Objects.requireNonNull(annotation);

        try {
            Class<?> clazz = ByteBuddyUtil.generateConsumer(eventHandleClass, method, true);
            @SuppressWarnings("unchecked")
            Consumer<Object> consumer = (Consumer<Object>) clazz.getConstructor().newInstance();

            // 放入监听列表

            Consumer<Object> oldValue = this.eventListenerMap.put(method.getParameterTypes()[0], consumer);
            if (Objects.nonNull(oldValue)) {
                return BoolResult.fail("event listener repeated, param: " + method.getParameterTypes()[0]);
            }
        } catch (IOException | InstantiationException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException | NoSuchMethodException | SecurityException e) {
            return BoolResult.fail("register event listener exception: " + e.getMessage());
        }
        return BoolResult.success();
    }
}