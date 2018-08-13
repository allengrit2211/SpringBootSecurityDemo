package com.security.example.demo.util;

import lombok.Data;

/**
 * @Auther: Allen
 * @Date: 2018/8/9 15:38
 * @Description:
 */

@Data
public class Msg {

    public Msg(){

    }

    public Msg(String title,String content,String etraInfo){
        this.title = title;
        this.content= content;
        this.etraInfo = etraInfo;
    }

    private String title;
    private String content;
    private String etraInfo;


}
