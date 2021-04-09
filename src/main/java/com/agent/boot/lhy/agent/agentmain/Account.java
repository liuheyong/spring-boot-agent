package com.agent.boot.lhy.agent.agentmain;

/**
 * @author: liuheyng
 * @date: 2021/4/9 17:41
 * @description: Account
 */
public class Account {

    private String username;
    private String password;

    public void operation() {
        System.out.println("operation.....");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
