package com.security.example.demo.entity;

import lombok.Data;

/**
 * @Auther: Allen
 * @Date: 2018/8/9 15:02
 * @Description:
 */
@Data
public class Permission {

    private Long id;
    private String name;
    private String description;
    private String url;
    private Long pid;

}
