package com.wansir.service.impl;


import com.wansir.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description 权限服务
 * @date 2023/5/14 14:24
 */
@Service
public class PermissionService {
    /**
     * 校验用户是否具有某个权限
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission){
        //如果是超级管理员，默认具有所有权限
        if(SecurityUtils.isAdmin()){
            return true;
        }
        //否则， 获取当前用户所具有的权限，判断是否存在permission
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
