package com.agent.boot.lhy.agent.agentmain;

/**
 * @author: liuheyng
 * @date: 2021/4/10 9:49
 * @description: TestMain
 */
public class TestMain {

    public static void main(String[] args) throws InterruptedException {
        int count = 0;
        do {
            deal(count);
            Thread.sleep(2000);
            count++;
        } while (count <= 50);
    }

    public static void deal(Integer count) {
        System.out.println("deal handle:" + count);
    }
}
