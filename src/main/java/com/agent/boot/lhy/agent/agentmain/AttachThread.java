package com.agent.boot.lhy.agent.agentmain;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author: liuheyng
 * @date: 2021/4/10 9:52
 * @description:
 */
public class AttachThread extends Thread {

    /**
     * 记录程序启动时的 VM 集合
     */
    private final List<VirtualMachineDescriptor> listBefore;
    /**
     * 要加载的agent.jar
     */
    private final String jar;

    private AttachThread(String attachJar, List<VirtualMachineDescriptor> vms) {
        listBefore = vms;
        jar = attachJar;
    }

    public static void main(String[] args) {
        new AttachThread("D:/idea-workspace/spring-boot-agent/target/spring-boot-agent.jar", VirtualMachine.list()).start();
    }

    @Override
    public void run() {
        VirtualMachine vm;
        List<VirtualMachineDescriptor> latestList;
        int count = 0;
        try {
            while (true) {
                latestList = VirtualMachine.list();
                vm = hasTargetVm(latestList);
                if (vm == null) {
                    System.out.println("没有目标 jvm 程序，请手动指定java pid");
                    try {
                        vm = VirtualMachine.attach("46358");
                    } catch (AttachNotSupportedException e) {
                        //System.out.println("拒绝访问 Disconnected from the target VM");
                    }
                }
                Thread.sleep(1000);
                System.out.println(count++);
                if (Objects.nonNull(vm) || count >= 100) {
                    break;
                }
            }
            Objects.requireNonNull(vm).loadAgent(jar, "hello");
            vm.detach();
        } catch (Exception e) {
            System.out.println("异常：" + e);
        }
    }

    /**
     * 判断是否有目标 JVM 程序运行
     */
    private VirtualMachine hasTargetVm(List<VirtualMachineDescriptor> listAfter) throws IOException, AttachNotSupportedException {
        for (VirtualMachineDescriptor vmd : listAfter) {
            if (vmd.displayName().endsWith("TestMain"))
                return VirtualMachine.attach(vmd);
        }
        return null;
    }
}
