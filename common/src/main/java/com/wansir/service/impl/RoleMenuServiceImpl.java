package com.wansir.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansir.mapper.RoleMenuMapper;
import com.wansir.pojo.entity.RoleMenu;
import com.wansir.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2023-05-14 20:33:17
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
