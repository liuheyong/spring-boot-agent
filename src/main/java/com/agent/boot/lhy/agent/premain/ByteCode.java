package com.agent.boot.lhy.agent.premain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

/**
 * @author: liuheyng
 * @date: 2021/4/9 16:09
 * @description: ByteCode
 */
public class ByteCode {

    public CtClass modifyByteCode(String className, String method) throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get(className);
        CtMethod oldMethod = ctClass.getDeclaredMethod(method);
        String oldMethodName = oldMethod.getName();
        String newName = oldMethodName + "$impl";
        oldMethod.setName(newName);

        CtMethod newMethod = CtNewMethod.copy(oldMethod, oldMethodName, ctClass, null);
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\nSystem.out.println(\"start to modify bytecode\");\n");
        sb.append("long start = System.currentTimeMillis();\n");
        sb.append(newName + "($$);\n");
        sb.append("System.out.println(\"call method" + oldMethodName + "took\"+(System.currentTimeMillis()-start))");
        sb.append("}");
        newMethod.setBody(sb.toString());
        ctClass.addMethod(newMethod);
        return ctClass;
    }
}
