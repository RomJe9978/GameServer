package com.games.framework.component.eventkit;

import com.games.framework.log.Log;
import com.games.framework.utils.ByteBuddyUtil;
import com.games.framework.utils.ScanUtil;
import com.romje.model.BoolResult;
import com.romje.utils.CollectionUtil;
import com.romje.utils.EmptyUtil;
import com.romje.utils.ReflectionUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

/**
 * 事件发布器，用于事件机制的注册，分发
 *
 * @author liu xuan jie
 */
public enum EventDispatcher {

    INSTANCE;

    /**
     * 所有事件，对应的最终监听处理方法的映射
     * key：事件唯一标识，value：对应事件的所有监听方法信息（优先级排序）
     */
    private final Map<Integer, List<EventListenerEntry>> eventListenersMap = new HashMap<>();

    /**
     * 是否需要将“动态字节码代理类”输出到文件展示出来
     */
    @Setter
    @Getter
    private boolean isShow = false;

    /**
     * 事件触发，执行指定事件的所有监听方法
     * <p> 如果针对指定事件，没有任何监听方法需要执行，则认为成功，返回{@code true}
     *
     * @param eventKey 事件唯一标识
     * @param param    最终处理事件的执行方法所需要的参数，不允许为{@code null}
     */
    public void dispatch(int eventKey, @NonNull Object param) {
        List<EventListenerEntry> eventListenerEntries = this.eventListenersMap.get(eventKey);
        if (EmptyUtil.isEmpty(eventListenerEntries)) {
            return;
        }

        for (EventListenerEntry entry : eventListenerEntries) {
            // 异常捕获放到循环内，不要让一个异常中断所有监听者
            try {
                entry.getProxyConsumer().accept(param);
            } catch (Exception e) {
                Log.FRAME.warn("[Event] event key:{} handle:{} exception!", eventKey, entry, e);
            }
        }
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
        if (EmptyUtil.isEmpty(classList)) {
            return BoolResult.success();
        }

        // 先按照类名进行排序，防止随机性（没有任何逻辑作用）
        classList.sort(Comparator.comparing(Class::getName));
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

        EventListener annotation = method.getAnnotation(EventListener.class);
        Objects.requireNonNull(annotation);

        try {
            Class<?> clazz = ByteBuddyUtil.generateConsumer(eventHandleClass, method, this.isShow);
            @SuppressWarnings("unchecked")
            Consumer<Object> consumer = (Consumer<Object>) clazz.getConstructor().newInstance();

            // 循环每一个key，将当前类的当前方法的动态代理类注册监听即可
            for (int eventKey : annotation.value()) {
                EventListenerEntry newEntry = EventListenerEntry.of(eventHandleClass, method, annotation, consumer);
                this.registerKeyListener(eventKey, newEntry);
            }
        } catch (IOException | InstantiationException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException | NoSuchMethodException | SecurityException e) {
            return BoolResult.fail("register event listener exception: " + e.getMessage());
        }
        return BoolResult.success();
    }

    /**
     * 将对一个事件的监听方法注册到监听列表，优先级相同的时候，符合"先进先出"
     *
     * @param eventKey 事件的唯一标识
     * @param newEntry 最终处理方法的统一封装
     */
    private void registerKeyListener(int eventKey, EventListenerEntry newEntry) {
        List<EventListenerEntry> listenerEntryList = this.eventListenersMap.get(eventKey);
        if (Objects.isNull(listenerEntryList)) {
            listenerEntryList = new ArrayList<>();
            this.eventListenersMap.put(eventKey, listenerEntryList);
        }

        CollectionUtil.insertWithCompare(listenerEntryList, newEntry);
    }
}