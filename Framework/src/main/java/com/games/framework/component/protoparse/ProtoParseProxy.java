package com.games.framework.component.protoparse;

import com.google.protobuf.GeneratedMessageV3;
import com.romje.model.BoolResult;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;

import java.util.Map;

/**
 * "Proto Buffer"消息相关的转换类
 *
 * @author liu xuan jie
 */
public enum ProtoParseProxy implements IProtoParser {

    INSTANCE;

    private IProtoParser realParser;

    @Override
    public GeneratedMessageV3 parseFrom(int packetId, byte[] bytes) {
        return this.realParser.parseFrom(packetId, bytes);
    }

    /**
     * 将消息映射关系注册进最终的“转换类”，内部使用字节码动态生成代理类
     *
     * @param idProtoMap key：消息Id，value：Id对应的具体协议类
     * @return 任何失败返回{@code false},失败信息从{@link BoolResult#message()}中获取
     */
    public BoolResult registerProxy(Map<Integer, Class<? extends GeneratedMessageV3>> idProtoMap) {
        try {
            Class<?> clazz = generateProtoParser(idProtoMap);
            this.realParser = (IProtoParser) clazz.getConstructor().newInstance();
            return BoolResult.success();
        } catch (Exception e) {
            return BoolResult.fail("Generate proto parser exception:" + e.getMessage());
        }
    }

    /**
     * 字节码技术，运行时动态生成转换类
     */
    private Class<?> generateProtoParser(Map<Integer, Class<? extends GeneratedMessageV3>> idProtoMap) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass oldClass = pool.get(IProtoParser.class.getName());
        CtClass ctNewClass = pool.makeClass(oldClass.getName() + "$PROXY");
        if (ctNewClass.isFrozen()) {
            ctNewClass.defrost();
        }

        // 引入需要的类
        pool.importPackage("com.google.protobuf.GeneratedMessageV3");
        for (Map.Entry<Integer, Class<? extends GeneratedMessageV3>> entry : idProtoMap.entrySet()) {
            pool.importPackage(entry.getValue().getName());
        }

        // 添加无参数构造方法
        CtConstructor newConstructor = new CtConstructor(new CtClass[0], ctNewClass);
        newConstructor.setBody("{}");
        ctNewClass.addConstructor(newConstructor);

        CtClass superCt = pool.get(IProtoParser.class.getName());
        ctNewClass.addInterface(superCt);
        String handleMethodStr = generateMethod(idProtoMap);
        CtMethod method = CtMethod.make(handleMethodStr, ctNewClass);
        ctNewClass.addMethod(method);

        ctNewClass.writeFile("output/");
        return ctNewClass.toClass();
    }

    private String generateMethod(Map<Integer, Class<? extends GeneratedMessageV3>> idProtoMap) {
        StringBuilder methodStr = new StringBuilder();
        methodStr.append("public GeneratedMessageV3 parseFrom(int packetId, byte[] bytes) throws Exception {");
        methodStr.append("switch($1) {");
        for (Map.Entry<Integer, Class<? extends GeneratedMessageV3>> entry : idProtoMap.entrySet()) {
            methodStr.append("case ").append(entry.getKey()).append(":")
                    .append("return ").append(entry.getValue().getName()).append(".parseFrom($2);");
        }
        methodStr.append("default: return null;");
        methodStr.append("}");
        methodStr.append("}");
        return methodStr.toString();
    }
}