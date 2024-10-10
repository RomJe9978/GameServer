package com.games.framework.utils;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import net.bytebuddy.jar.asm.ClassReader;
import net.bytebuddy.jar.asm.ClassVisitor;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
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

    private static final String CLASS_FILE_EXTENSION = ".class";

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
                .defineMethod(CONSUMER_ACCEPT_METHOD_NAME, void.class, Visibility.PUBLIC)
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

    /**
     * 获取类中，在指定“方法”中直接被使用的“成员变量”
     *
     * <p> “直接使用”表示，不包括那些没有经过参数传递并且在嵌套方法中出现的字段
     * <p> 该接口不会获取所有静态字段，{@code static}修饰的不被认为是实例字段
     * <p> 如果有父类，指定方法中直接使用父类字段，也会被包括在结果中
     *
     * @param clazz      指定类，不允许为{@code null}
     * @param methodName 指定方法名，不允许为{@code null}
     * @return 在方法中直接出现的字段名称集合。不会为{@code null}
     * @throws IOException 类资源获取异常
     */
    public static Set<String> listFieldsInMethod(Class<?> clazz, String methodName) throws IOException {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(methodName);

        String fileName = clazz.getSimpleName() + CLASS_FILE_EXTENSION;
        try (InputStream classInputStream = clazz.getResourceAsStream(fileName)) {
            if (Objects.isNull(classInputStream)) {
                return Collections.emptySet();
            }

            ClassReader classReader = new ClassReader(classInputStream);
            Set<String> usedFieldSet = new HashSet<>();

            classReader.accept(new ClassVisitor(Opcodes.ASM9) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor,
                                                 String signature, String[] exceptions) {
                    if (name.equals(methodName)) {
                        return new MethodVisitor(Opcodes.ASM9) {
                            @Override
                            public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                                if (opcode == Opcodes.PUTFIELD || opcode == Opcodes.GETFIELD) {
                                    usedFieldSet.add(name);
                                }
                                super.visitFieldInsn(opcode, owner, name, descriptor);
                            }
                        };
                    }
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }
            }, 0);
            return usedFieldSet;
        }
    }

    /**
     * 检查子类指定方法中是否调用了继承自父类的同名方法，即{@code super.method()}
     *
     * @param childClass  子类，不允许为{@code null}
     * @param parentClass 父类，不允许为{@code null}
     * @param methodName  指定方法名称，不允许为{@code null}
     * @return 只有当子类指定方法中调用了继承父类的方法时候，返回{@code true}
     * @throws IOException 类资源获取异常
     */
    public static boolean checkSuperMethodCall(Class<?> childClass, Class<?> parentClass, String methodName)
            throws IOException {
        Objects.requireNonNull(childClass);
        Objects.requireNonNull(parentClass);
        Objects.requireNonNull(methodName);

        String childClassName = childClass.getName().replace('.', '/');
        String parentClassName = parentClass.getName().replace('.', '/');

        final boolean[] isCalled = {false};
        ClassReader classReader = new ClassReader(childClassName);
        classReader.accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor,
                                             String signature, String[] exceptions) {
                if (name.equals(methodName)) {
                    return new MethodVisitor(Opcodes.ASM9) {
                        @Override
                        public void visitMethodInsn(int opcode, String owner, String name,
                                                    String descriptor, boolean isInterface) {
                            // 检查是否调用了父类的同名方法
                            if (opcode == Opcodes.INVOKESPECIAL
                                    && methodName.equals(name) && parentClassName.equals(owner)) {
                                isCalled[0] = true;
                            }
                            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                        }
                    };
                }
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        }, 0);

        return isCalled[0];
    }
}