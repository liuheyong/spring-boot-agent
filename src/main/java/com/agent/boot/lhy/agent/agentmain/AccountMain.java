package com.agent.boot.lhy.agent.agentmain;

/**
 * @author: liuheyng
 * @date: 2021/4/9 17:39
 * @description: AccountMain
 */
public class AccountMain {

    public static void main(String[] args) throws InterruptedException {
        for (;;) {
            new Account().operation();
            Thread.sleep(5000);
        }
    }
}
