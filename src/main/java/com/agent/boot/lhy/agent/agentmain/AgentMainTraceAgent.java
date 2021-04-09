package com.agent.boot.lhy.agent.agentmain;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

/**
 * @author: liuheyng
 * @date: 2021/4/9 17:03
 * @description: AgentMainTraceAgent
 */
public class AgentMainTraceAgent {

    public static void agentmain(String agentArgs, Instrumentation inst)
            throws UnmodifiableClassException {
        System.out.println("Agent Main called");
        System.out.println("agentArgs : " + agentArgs);
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain, byte[] classfileBuffer) {
                System.out.println("agentmain load Class  :" + className);
                return classfileBuffer;
            }
        }, true);
        //支持重复转换目标类 还有一个类似的方法 redefineClasses ，
        // 注意，这个方法是在类加载前使用的。类加载后需要使用 retransformClasses 方法。
        inst.retransformClasses(Account.class);
    }
}
