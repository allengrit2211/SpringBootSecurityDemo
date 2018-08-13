package com.security.example.demo.mapper;

import com.security.example.demo.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Auther: Allen
 * @Date: 2018/8/9 15:06
 * @Description:
 */
@Mapper
public interface PermissionMapper {


    List<Permission> findByAdminUserId(Long id);

    List<Permission> findByAdminUserName(String userName);

    List<Permission> findAll();

}
