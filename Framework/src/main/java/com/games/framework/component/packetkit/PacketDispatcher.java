package com.games.framework.component.packetkit;

import com.games.framework.log.Log;
import com.games.framework.utils.ByteBuddyUtil;
import com.games.framework.utils.ScanUtil;
import com.romje.model.BoolResult;
import com.romje.utils.EmptyUtil;
import com.romje.utils.ReflectionUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * "网路消息包"的分发器，统一管理网络包的处理方法
 *
 * @author liu xuan jie
 */
@Getter
public enum PacketDispatcher {

    INSTANCE;

    /**
     * 指定网络消息包对应最终“处理方法”的信息
     * key：网络包标识Id（非实例Id），value：最终处理方法的信息封装
     */
    private final Map<Integer, PacketListenerEntry> packetListenersMap = new HashMap<>();

    /**
     * 是否需要将“动态字节码代理类”输出到文件展示出来
     */
    @Setter
    @Getter
    private boolean isShow = false;

    /**
     * 派发网络包，调用监听该消息的最终处理方法
     * <p> 如果针对指定消息包，没有任何监听方法需要执行，则认为成功，返回{@code true}
     *
     * @param packetId 网络包唯一标识id（注意不是实例id，而是同一消息的标识id）
     * @param param1   最终处理方法所需要的参数，不允许为{@code null}
     * @param param2   最终处理方法所需要的参数，不允许为{@code null}
     */
    public void dispatch(int packetId, @NonNull Object param1, @NonNull Object param2) {
        PacketListenerEntry entry = this.packetListenersMap.get(packetId);
        if (Objects.isNull(entry)) {
            return;
        }

        try {
            entry.getProxyConsumer().accept(param1, param2);
        } catch (Exception e) {
            Log.FRAME.warn("[Event] handler:{} handle packet id:{} exception!", entry, packetId, e);
        }
    }

    /**
     * 扫描指定包中的所有网络包处理类（使用了{@link PacketHandler}标识的类），
     * 注册这些处理类内的所有网络包处理方法（使用了{@link PacketListener}标识的方法）。
     *
     * @param packageName 如果扫描不到任何处理类，返回成功。
     * @return 任何错误或者异常返回{@code false}，失败信息在{@link BoolResult#message()}中。
     */
    public BoolResult registerPacketListener(String packageName) {
        // 扫包使用指定注解EventHandler的监听类
        List<Class<?>> classList = ScanUtil.scanAnnotationAsList(packageName, PacketHandler.class);
        if (EmptyUtil.isEmpty(classList)) {
            return BoolResult.success();
        }
        return this.registerPacketListener(classList);
    }

    /**
     * 注册一组处理类中的，所有最终处理方法（扫描{@link PacketHandler}注解）。
     *
     * @param classList 一组处理类，如果列表为空，则返回成功。
     * @return 任何错误或者异常返回{@code false}，失败信息在{@link BoolResult#message()}中。
     */
    public BoolResult registerPacketListener(List<Class<?>> classList) {
        if (EmptyUtil.isEmpty(classList)) {
            return BoolResult.success();
        }

        for (Class<?> clazz : classList) {
            BoolResult singleResult = registerPacketListener(clazz);
            if (singleResult.isFail()) {
                return singleResult;
            }
        }
        return BoolResult.success();
    }

    /**
     * 注册单个处理类中的，所有处理方法（扫描{@link PacketListener}注解）。
     *
     * @param handlerClass 单个处理类，不允许为{@code null}。
     * @return 任何错误或者异常返回{@code false}，失败信息在{@link BoolResult#message()}中。
     */
    public BoolResult registerPacketListener(Class<?> handlerClass) {
        List<Method> annotatedMethods = ReflectionUtil.getMethodsWithAnnotation(handlerClass, PacketListener.class);
        if (EmptyUtil.isEmpty(annotatedMethods)) {
            return BoolResult.success();
        }

        for (Method method : annotatedMethods) {
            BoolResult result = this.registerListenMethod(handlerClass, method);
            if (result.isFail()) {
                return result;
            }
        }
        return BoolResult.success();
    }

    /**
     * 最底层注册方法，将处理类的处理方法，注册到分发器中。
     *
     * @param handlerClass 处理方法所在的处理类，不允许为{@code null}。
     * @param method       最终的处理方法，不允许为{@code null}。必须标识{@link PacketListener}注解。
     * @return 任何错误或者异常返回{@code false}，失败信息在{@link BoolResult#message()}中。
     */
    private BoolResult registerListenMethod(Class<?> handlerClass, Method method) {
        // 监听方法必须是静态的
        if (ReflectionUtil.nonStaticMethod(method)) {
            return BoolResult.fail("packet listener method non static: " + method.getName());
        }

        PacketListener annotation = method.getAnnotation(PacketListener.class);
        Objects.requireNonNull(annotation);

        try {
            Class<?> clazz = ByteBuddyUtil.generateBiConsumer(handlerClass, method, this.isShow);
            @SuppressWarnings("unchecked")
            BiConsumer<Object, Object> consumer = (BiConsumer<Object, Object>) clazz.getConstructor().newInstance();

            // 注册即可
            PacketListenerEntry newEntry = PacketListenerEntry.of(handlerClass, method, annotation, consumer);
            this.packetListenersMap.put(annotation.value(), newEntry);
        } catch (IOException | InstantiationException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException | NoSuchMethodException | SecurityException e) {
            return BoolResult.fail("register packet listener exception: " + e.getMessage());
        }
        return BoolResult.success();
    }
}