package com.agent.boot.lhy.agent.premain;

import java.util.List;

/**
 * @author: liuheyng
 * @date: 2021/4/6 22:48
 * @description: DeleteService
 */
public class DeleteService {

    public void delete(List<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            list.remove(i);
        }
    }

}
