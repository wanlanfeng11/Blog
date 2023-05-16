package com.wansir.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansir.pojo.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-12 19:54:26
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}

