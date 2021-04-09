package com.agent.boot.lhy.agent.agentmain;

import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.List;

/**
 * @author: liuheyng
 * @date: 2021/4/9 17:05
 * @description: JVMTIThread
 */
public class JVMTIThread {

    public static void main(String[] args) throws IOException, AgentLoadException, AgentInitializationException, AttachNotSupportedException {
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        for (VirtualMachineDescriptor vmd : list) {
            if (vmd.displayName().endsWith("AccountMain")) {
                VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
                virtualMachine.loadAgent("E:\\self\\demo\\out\\artifacts\\test\\test.jar ", "cxs");
                System.out.println("ok");
                virtualMachine.detach();
            }
        }
    }
}
