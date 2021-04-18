package com.agent.boot.lhy.agent.agentmain;

import com.alibaba.fastjson.JSON;

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
            Thread.sleep(20000);
            count++;
            System.out.println(JSON.toJSONString(System.getProperties()));
        } while (count <= 5000);
    }

    public static void deal(Integer count) {
        System.out.println("deal handle:" + count);
    }
}
