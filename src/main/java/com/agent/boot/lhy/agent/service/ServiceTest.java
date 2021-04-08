package com.agent.boot.lhy.agent.service;

import com.agent.boot.lhy.agent.DeleteService;
import com.agent.boot.lhy.agent.InsertService;

import java.util.LinkedList;
import java.util.List;

/**
 * @author: liuheyng
 * @date: 2021/4/6 23:46
 * @description: ServiceTest
 */
public class ServiceTest {

    public static void main(String[] args) {
        //插入服务
        InsertService insertService = new InsertService();
        //删除服务
        DeleteService deleteService = new DeleteService();

        System.out.println("....begin insert....");
        insertService.insert1(1040);
        insertService.insert2(20000);
        insertService.insert3(30203);
        System.out.println(".....end insert.....");
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < 29440; i++) {
            list.add(i);
        }
        System.out.println(".....begin delete......");
        deleteService.delete(list);
        System.out.println("......end delete........");
    }
}
