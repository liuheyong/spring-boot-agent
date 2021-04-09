package com.agent.boot.lhy.agent.premain;

import java.util.LinkedList;
import java.util.List;

/**
 * @author: liuheyng
 * @date: 2021/4/6 22:46
 * @description: InsertService
 */
public class InsertService {

    /**
     * 模拟数据插入
     *
     * @param num
     */
    public void insert2(int num) {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < num; i++) {
            list.add(i);
        }
    }

    public void insert1(int num) {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < num; i++) {
            list.add(i);
        }
    }

    public void insert3(int num) {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < num; i++) {
            list.add(i);
        }
    }

}
