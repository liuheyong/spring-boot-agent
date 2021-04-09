package com.agent.boot.lhy.agent.premain;

import java.lang.instrument.Instrumentation;

/**
 * @author: liuheyng
 * @date: 2021/4/6 22:49
 * @description: TimeMonitorAgent  agent的入口类  主类
 */
public class TimeMonitorAgent {

    //premain 这个方法名称是固定写法 不能写错或修改
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("execute insert method interceptor....");
        System.out.println("agentArgs参数：" + agentArgs);
        //添加自定义类转换器
        inst.addTransformer(new TimeMonitorTransformer(agentArgs));
    }
}
