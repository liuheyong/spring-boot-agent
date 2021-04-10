package com.agent.boot.lhy.agent.agentmain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author: liuheyng
 * @date: 2021/4/10 9:37
 * @description: CustomAttachTransformer
 */
public class CustomAttachTransformer implements ClassFileTransformer {

    // 被处理的方法列表
    private final static Map<String, List<String>> METHOD_MAP = new ConcurrentHashMap<>();

    private static final String DEFAULT_METHOD = "com.agent.boot.lhy.agent.agentmain.TestMain.deal";

    private static final String CLASS_REGEX = "^(\\w+\\.)+[\\w]+$";

    private static final Pattern CLASS_PATTERN = Pattern.compile(CLASS_REGEX);

    private CustomAttachTransformer() {
        add(DEFAULT_METHOD);
    }

    public CustomAttachTransformer(String methodString) {
        this();
        if (!CLASS_PATTERN.matcher(methodString).matches()) {
            System.out.println("string:" + methodString + " not a method string");
            return;
        }
        add(methodString);
    }

    public void add(String methodString) {
        String className = methodString.substring(0, methodString.lastIndexOf("."));
        String methodName = methodString.substring(methodString.lastIndexOf(".") + 1);
        List<String> list = METHOD_MAP.computeIfAbsent(className, k -> new ArrayList<>());
        list.add(methodName);
        System.out.println(METHOD_MAP.toString());
    }

    // 重写此方法
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        className = className.replace("/", ".");
        byte[] byteCode = null;
        System.out.println("agent: targetClassName:" + className);
        if (METHOD_MAP.containsKey(className)) {
            // 判断加载的class的包路径是不是需要监控的类
            CtClass ctClass;
            try {
                ClassPool classPool = ClassPool.getDefault();
                // 将要修改的类的classpath加入到ClassPool中，否则找不到该类
                classPool.appendClassPath(new LoaderClassPath(loader));
                ctClass = classPool.get(className);
                for (String methodName : METHOD_MAP.get(className)) {
                    System.out.println("agent: targetMethodName:" + methodName);
                    CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);// 得到这方法实例
                    ctMethod.addLocalVariable("begin", CtClass.longType);
                    ctMethod.addLocalVariable("end", CtClass.longType);
                    ctMethod.insertBefore("begin = System.currentTimeMillis();");
                    ctMethod.insertAfter("end = System.currentTimeMillis();");
                    ctMethod.insertAfter("System.out.println(\"方法" + ctMethod.getName() + "耗时\"+ (end - begin) +\"ms\");");
                }
                byteCode = ctClass.toBytecode();
                // ClassPool中删除该类
                ctClass.detach();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        return byteCode;
    }
}
