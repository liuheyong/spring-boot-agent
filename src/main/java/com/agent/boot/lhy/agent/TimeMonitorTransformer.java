package com.agent.boot.lhy.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.Objects;

/**
 * @author: liuheyng
 * @date: 2021/4/6 22:49
 * @description: TimeMonitorTransformer
 */
public class TimeMonitorTransformer implements ClassFileTransformer {

    private static final String START_TIME = "\nlong startTime = System.currentTimeMillis();\n";
    private static final String END_TIME = "\nlong endTime = System.currentTimeMillis();\n";
    private static final String METHOD_RETURN_VALUE_VAR = "__time_monitor_result";
    private static final String EMPTY = "";

    private String classNameKeyword;

    public TimeMonitorTransformer(String classNameKeyword) {
        this.classNameKeyword = classNameKeyword;
    }

    /**
     * @param classLoader         默认类加载器
     * @param className           类名的关键字 因为还会进行模糊匹配
     * @param classBeingRedefined
     * @param protectionDomain
     * @param classfileBuffer
     * @return
     * @throws IllegalClassFormatException
     */
    public byte[] transform(ClassLoader classLoader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replace("/", ".");
        CtClass ctClass;
        try {
            //使用全称,用于取得字节码类
            ctClass = ClassPool.getDefault().get(className);
            //匹配类的机制是基于类的关键字 这个是客户端传过来的参数 满足就会获取所有的方法 不满足跳过
            if (Objects.equals(classNameKeyword, EMPTY) || (!Objects.equals(classNameKeyword, EMPTY) && className.indexOf(classNameKeyword) != -1)) {
                //所有方法
                CtMethod[] ctMethods = ctClass.getDeclaredMethods();
                //遍历每一个方法
                for (CtMethod ctMethod : ctMethods) {
                    //修改方法的字节码
                    transformMethod(ctMethod, ctClass);
                }
            }
            //重新返回修改后的类
            return ctClass.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 为每一个拦截到的方法 执行一个方法的耗时操作
     *
     * @param ctMethod
     * @param ctClass
     * @throws Exception
     */
    private void transformMethod(CtMethod ctMethod, CtClass ctClass) throws Exception {
        //抽象的方法是不能修改的 或者方法前面加了final关键字
        if ((ctMethod.getModifiers() & Modifier.ABSTRACT) > 0) {
            return;
        }
        //获取原始方法名称
        String methodName = ctMethod.getName();
        String monitorStr = "\nSystem.out.println(\"method " + ctMethod.getLongName() + " cost:\" +(endTime - startTime) +\"ms.\");";
        //实例化新的方法名称
        String newMethodName = methodName + "$impl";
        //设置新的方法名称
        ctMethod.setName(newMethodName);
        //创建新的方法，复制原来的方法 ，名字为原来的名字
        CtMethod newMethod = CtNewMethod.copy(ctMethod, methodName, ctClass, null);

        StringBuilder bodyStr = new StringBuilder();
        //拼接新的方法内容
        bodyStr.append("{");

        //返回类型
        CtClass returnType = ctMethod.getReturnType();

        //是否需要返回
        boolean hasReturnValue = (CtClass.voidType != returnType);

        if (hasReturnValue) {
            String returnClass = returnType.getName();
            bodyStr.append("\n").append(returnClass + " " + METHOD_RETURN_VALUE_VAR + ";");
        }

        bodyStr.append(START_TIME);

        if (hasReturnValue) {
            bodyStr.append("\n").append(METHOD_RETURN_VALUE_VAR + " = ($r)" + newMethodName + "($$);");
        } else {
            bodyStr.append("\n").append(newMethodName + "($$);");
        }

        bodyStr.append(END_TIME);
        bodyStr.append(monitorStr);

        if (hasReturnValue) {
            bodyStr.append("\n").append("return " + METHOD_RETURN_VALUE_VAR + " ;");
        }

        bodyStr.append("}");
        //替换新方法
        newMethod.setBody(bodyStr.toString());
        //增加新方法
        ctClass.addMethod(newMethod);
    }

}
