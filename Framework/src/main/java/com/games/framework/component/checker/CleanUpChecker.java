package com.games.framework.component.checker;

import com.games.framework.utils.ByteBuddyUtil;
import com.romje.model.BoolResult;
import com.romje.utils.EmptyUtil;
import com.romje.utils.ReflectionUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * “清除操作”检查器
 *
 * @author liu xuan jie
 */
public class CleanUpChecker {

    /**
     * 指定一组类，检查每个类中的指定方法是否完成了对需要清除的字段进行了清理操作
     *
     * <p> 检查思路：指定清理方法中是否直接调用了需要清理的字段，这个思路是一个折中思
     * 路，因为不确认指定对象的某个字段清理之后的值是多少，有些时候并不是类型的默认值，
     * 详细解释见{@link #checkDeclaredFieldCleared(Class, String)}
     *
     * <p> 对每一个类会检查其自身的所有字段，以及清理方法中是否调用了父类的清理方法，
     * 例如：A继承B，B继承C。则检查A的时候会检查A自身的字段以及A中是否调用了B的清理方
     * 法。而在检查B的时候，会检查B自身字段，以及是否在B中调用了C的清理方法，详细解释
     * 见{@link #checkCleanUp(Class, String)}
     *
     * @param classList  指定需要检查的类集合，列表为空，直接返回成功
     * @param methodName 指定清理方法的方法名
     * @return 如果检查不通过，则{@link BoolResult#message()}中可以获取失败描述信息
     * @throws IOException 类资源获取异常
     */
    public static BoolResult checkCleanUp(List<Class<?>> classList, String methodName) throws IOException {
        if (EmptyUtil.isEmpty(classList)) {
            return BoolResult.success();
        }

        if (EmptyUtil.isEmpty(methodName)) {
            return BoolResult.fail("check clean up method name is empty!");
        }

        for (Class<?> clazz : classList) {
            BoolResult boolResult = checkCleanUp(clazz, methodName);
            if (boolResult.isFail()) {
                return boolResult;
            }
        }
        return BoolResult.success();
    }

    /**
     * 1.检查类自身内部的字段，要求类自身内部必须有显式声明的指定名称的清理方法，哪怕是空方法
     * 2.检查是否调用了父类的清除方法,只关心一层父类即可，更高层的父类由直接关联的子类检查即可
     */
    private static BoolResult checkCleanUp(Class<?> clazz, String methodName) throws IOException {
        if (!ReflectionUtil.hasDeclaredMethod(clazz, methodName)) {
            return BoolResult.fail(clazz.getName() + " not has clean up method explicitly!");
        }

        BoolResult result = checkDeclaredFieldCleared(clazz, methodName);
        if (result.isFail()) {
            return result;
        }

        Class<?> parentClass = clazz.getSuperclass();
        if (ReflectionUtil.hasPublicMethod(parentClass, methodName)) {
            boolean isCalled = ByteBuddyUtil.checkSuperMethodCall(clazz, parentClass, methodName);
            if (!isCalled) {
                return BoolResult.fail(clazz.getName() + " non called super clean up method!");
            }
        }

        return BoolResult.success();
    }

    /**
     * 检查类自身（不包括继承）的字段，是否在清理方法中被直接调用了
     *
     * <p> 静态字段，不需要被检查
     * <p> 使用{@link NonClear}注解的字段也不需要被检查
     *
     * @param clazz      类信息
     * @param methodName 清理方法名称
     * @return 如果失败，失败信息从{@link BoolResult#message()}中获取
     * @throws IOException 类资源获取失败
     */
    private static BoolResult checkDeclaredFieldCleared(Class<?> clazz, String methodName) throws IOException {
        Field[] fields = clazz.getDeclaredFields();
        if (EmptyUtil.isEmpty(fields)) {
            return BoolResult.success();
        }

        List<String> nonUsedList = new ArrayList<>();
        Set<String> usedFieldSet = ByteBuddyUtil.listFieldsInMethod(clazz, methodName);
        for (Field field : fields) {
            int modifier = field.getModifiers();
            if (Modifier.isStatic(modifier)) {
                continue;
            }

            if (field.isAnnotationPresent(NonClear.class)) {
                continue;
            }

            if (!usedFieldSet.contains(field.getName())) {
                nonUsedList.add(field.getName());
            }
        }

        String failMessage = clazz.getName() + " has non clear field:" + nonUsedList.toString();
        return EmptyUtil.nonEmpty(nonUsedList) ? BoolResult.fail(failMessage) : BoolResult.success();
    }
}