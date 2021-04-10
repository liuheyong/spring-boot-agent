package com.agent.boot.lhy.agent.agentmain;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * @author: liuheyng
 * @date: 2021/4/10 9:45
 * @description:
 */
public class InstrumentAttach {

    public static void agentmain(String agentArgs, Instrumentation inst) throws UnmodifiableClassException {
        System.out.println("我是两个参数的 Java Agent agentmain, agentArgs:" + agentArgs);
        inst.addTransformer(new CustomAttachTransformer(agentArgs), true);
        // 指定需要转化的类
        inst.retransformClasses(TestMain.class);
    }

    public static void agentmain(String agentArgs) {
        System.out.println("我是一个参数的 Java Agent agentmain");
    }
}
