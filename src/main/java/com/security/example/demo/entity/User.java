package com.security.example.demo.entity;

import lombok.Data;

/**
 * @Auther: Allen
 * @Date: 2018/8/9 14:59
 * @Description:
 */
@Data
public class User {

    public User(){

    }

    public User(Long id){
        this.id = id;
    }

    private Long id;
    private String userName;
    private String passWord;

}
