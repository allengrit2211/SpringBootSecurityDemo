package com.security.example.demo.mapper;

import com.security.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Auther: Allen
 * @Date: 2018/8/9 15:07
 * @Description:
 */

@Mapper
public interface UserMapper {
    public User findByUserName(String userName);
}
