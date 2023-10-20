package performance;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author RomJe
 */
public class ReflectTest {
    public void doTest(TestParam param) {
    }

    @Test
    public void testStaticTime() {
        TestInnerClass innerClass = new TestInnerClass();
        TestParam testParam = new TestParam();
        testParam.setType(100);

        // 先热100万次
        for (int i = 0; i < 10000000; i++) {
            innerClass.handle(testParam);
        }

        long startTime = System.nanoTime();
        for (int i = 0; i < 10000000; i++) {
            innerClass.handle(testParam);
        }
        long endTime = System.nanoTime();
        System.out.println("直接调用总花费时间：" + (endTime - startTime));
    }

    @Test
    public void testReflectTime() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ReflectTest reflectTest = new ReflectTest();
        TestParam testParam = new TestParam();
        testParam.setType(100);

        Class<?> clazz = ReflectTest.class;
        Method method = clazz.getMethod("doTest", TestParam.class);
        // 先热100万次
        for (int i = 0; i < 10000000; i++) {
            method.invoke(reflectTest, testParam);
        }

        long startTime = System.nanoTime();
        for (int i = 0; i < 10000000; i++) {
            method.invoke(reflectTest, testParam);
        }
        long endTime = System.nanoTime();
        System.out.println("反射总花费时间：" + (endTime - startTime));
    }

    @Test
    public void testStaticClass() {
        TestParam testParam = new TestParam();
        testParam.setType(100);

        // 静态代码生成一个代理类
        Class<?> handleClazz = ReflectTest.class;
        Class<?> proxyClass = null;
        ITestHandle testHandle = null;
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass oldClass = pool.get(handleClazz.getName());
            CtClass ctNewClass = pool.makeClass(oldClass.getName() + "$ProxyEvent", oldClass);
            if (ctNewClass.isFrozen()) {
                ctNewClass.defrost();
            }

            CtClass superCt = pool.get(ITestHandle.class.getName());
            ctNewClass.addInterface(superCt);

            String handleMethodStr = proxyStaticCode(handleClazz.getMethod("doTest", TestParam.class));
            CtMethod method = CtMethod.make(handleMethodStr, ctNewClass);
            ctNewClass.addMethod(method);
            proxyClass = ctNewClass.toClass();
            testHandle = (ITestHandle) proxyClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return;
        }

        // 先热100万次
        for (int i = 0; i < 10000000; i++) {
            testHandle.handle(testParam);
        }

        long startTime = System.nanoTime();
        for (int i = 0; i < 10000000; i++) {
            testHandle.handle(testParam);
        }
        long endTime = System.nanoTime();
        System.out.println("字节码总花费时间：" + (endTime - startTime));
    }

    interface ITestHandle {
        void handle(TestParam param);
    }

    static String proxyStaticCode(Method method) {
        StringBuilder sb = new StringBuilder("public void handle(performance.TestParam event) throws Exception {");
        sb.append("int id = $1.getType();");
        sb.append("switch (id) {");

        sb.append("case ").append("1").append(":");
        String methodName = method.getName();
        Class<?>[] paramClassArr = method.getParameterTypes();
        if (paramClassArr.length == 1) {
            sb.append(methodName).append("($$);");
        }
        sb.append("break;");

        sb.append("case ").append("100").append(":");
        sb.append(methodName).append("($$);");
        sb.append("break;");

        sb.append("default: break;");

        sb.append("}");
        sb.append("}");
        return sb.toString();
    }

    class TestInnerClass {
        public void handle(TestParam param) {
            int type = param.getType();
            switch (type) {
                case 1:
                    doTest(param);
                    break;
                case 100:
                    doTest(param);
                    break;
                default:
                    break;
            }
        }
    }
}
