package com.wansir.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wansir.pojo.entity.UserRole;


/**
 * 用户和角色关联表(UserRole)表服务接口
 *
 * @author makejava
 * @since 2023-05-14 22:55:03
 */
public interface UserRoleService extends IService<UserRole> {

    void deleteByUserId(Long id);
}
