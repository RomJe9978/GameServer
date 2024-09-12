package com.games.framework.utils;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

/**
 * “Byte Buddy”字节码操作库封装工具（不允许实例化）
 *
 * <p> Byte Buddy 是一个用于在运行时动态生成、修改和操作 Java 类和方法的字节码操作库。
 * 它旨在简化字节码操作的复杂性，使得开发者可以通过简洁的 API 进行复杂的类生成和字节码
 * 修改，而无需直接处理底层的 ASM 框架。
 *
 * <p> Byte Buddy 在 2019 年获得了 Oracle 授予的 Duke's Choice Award 重要奖项。
 *
 * @author liu xuan jie
 */
public class ByteBuddyUtil {

    private static final String CONSUMER_ACCEPT_METHOD_NAME = "accept";

    private ByteBuddyUtil() {
    }

    /**
     * 使用Byte Buddy动态生成一个{@link Consumer}的实现类。
     * <p> 实现类会实现{@link Consumer#accept(Object)}方法，方法体直接调用{@code method}方法。
     *
     * @param handler 方法所依附的类，动态生成的实现类使用该类的“类加载器”。
     * @param method  外部保证方法符合{@link Consumer}要求，一个参数，无返回值。
     * @param isShow  是否将动态生成的类的字节码，输出保存到文件。{@code true}代表输出。
     * @return {@link Consumer}的实现类的{@code Class}，并且已经加载到JVM内。
     */
    public static Class<?> generateConsumer(Class<?> handler, Method method, boolean isShow) throws IOException {
        // 使用 Byte Buddy 生成动态类
        String newClassName = handler.getName() + "$" + method.getName().toUpperCase();
        DynamicType.Builder<?> builder = new ByteBuddy()
                .subclass(Object.class).name(newClassName).implement(Consumer.class);

        // 在生成的类中定义并实现方法
        DynamicType.Unloaded<?> dynamicType = builder
                .defineMethod(
                        CONSUMER_ACCEPT_METHOD_NAME,
                        void.class,
                        net.bytebuddy.description.modifier.Visibility.PUBLIC)
                .withParameter(Object.class)
                .intercept(MethodCall.invoke(method)
                        .withArgument(0)
                        .withAssigner(Assigner.DEFAULT, Assigner.Typing.DYNAMIC))
                .make();

        // 将类的字节码输出到文件
        if (isShow) {
            saveClassToFile(newClassName, dynamicType.getBytes());
        }

        // 加载并返回生成的类
        ClassLoader classLoader = handler.getClassLoader();
        return dynamicType.load(classLoader, ClassLoadingStrategy.Default.INJECTION).getLoaded();
    }

    /**
     * 将动态字节码技术生成的Class，输出到文件中展示代码
     *
     * @param className  动态生成的类名字
     * @param classBytes 动态生成的类的字节码
     * @throws IOException 文件输出异常
     */
    private static void saveClassToFile(String className, byte[] classBytes) throws IOException {
        Path path = Paths.get("output/" + className.replace('.', '/') + ".class");
        Files.createDirectories(path.getParent());

        try (FileOutputStream fos = new FileOutputStream(path.toFile())) {
            fos.write(classBytes);
        }
    }
}