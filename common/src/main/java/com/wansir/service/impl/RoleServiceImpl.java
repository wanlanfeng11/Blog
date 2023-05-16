package com.wansir.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansir.enums.SystemConstants;
import com.wansir.mapper.RoleMapper;
import com.wansir.pojo.dto.AddRoleDto;
import com.wansir.pojo.dto.PageDto;
import com.wansir.pojo.dto.RoleDto;
import com.wansir.pojo.dto.RoleVO;
import com.wansir.pojo.entity.Role;
import com.wansir.pojo.entity.RoleMenu;
import com.wansir.pojo.entity.UserRole;
import com.wansir.service.MenuService;
import com.wansir.service.RoleMenuService;
import com.wansir.service.RoleService;
import com.wansir.service.UserRoleService;
import com.wansir.utils.BeanCopyUtils;
import com.wansir.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-05-12 19:54:28
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息
        return roleMapper.selectRoleKeyByUserId(id);
    }

    /**
     * 查询角色列表
     *
     * @param pageNum  页码
     * @param pageSize 数量
     * @param roleName 角色名称
     * @param status   状态
     * @return
     */
    @Override
    public PageDto getList(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.hasText(roleName), Role::getRoleName, roleName);
        lqw.eq(StringUtils.hasText(status), Role::getStatus, status);
        Page<Role> page = new Page<Role>();
        page(page, lqw);
        List<RoleVO> roleVOS = BeanCopyUtils.copyBeans(page.getRecords(), RoleVO.class);
        return new PageDto(roleVOS, page.getTotal());
    }

    @Override
    @Transactional
    public void changeStatus(RoleDto roleDto) {
        //构建对象
        Role role = new Role();
        role.setStatus(roleDto.getStatus());
        role.setId(roleDto.getRoleId());
        updateById(role);
    }


    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 添加角色
     */
    @Override
    @Transactional
    public void addRole(AddRoleDto roleDto) {
        //1.插入role表
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        save(role);
        //2.插入role_tag表
        //2.1.获取集合
        Long roleId = role.getId();
        List<RoleMenu> roleMenus = roleDto.getMenuIds().stream()
                .map(id -> new RoleMenu(roleId, id))
                .collect(Collectors.toList());
        //2.2.插入role_tag表
        roleMenuService.saveBatch(roleMenus);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        //在role表中移除
        removeById(id);
        //在role_menu中移除
        LambdaQueryWrapper<RoleMenu> lqw = new LambdaQueryWrapper<>();
        lqw.eq(RoleMenu::getRoleId, id);
        roleMenuService.remove(lqw);
    }

    @Override
    public RoleVO getRole(Long id) {
        Role role = getById(id);
        RoleVO roleVO = BeanCopyUtils.copyBean(role, RoleVO.class);
        return roleVO;
    }

    /**
     * 更新角色
     * @param addRoleDto 角色信息
     */
    @Override
    @Transactional
    public void updateRole(AddRoleDto addRoleDto) {
        //1.更新role表
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        this.updateById(role);
        //2.更新role_menu表
        //2.1.删除角色对应的原本的menu
        LambdaQueryWrapper<RoleMenu> lqw = new LambdaQueryWrapper<>();
        lqw.eq(RoleMenu::getRoleId, addRoleDto.getId());
        roleMenuService.remove(lqw);
        //2.2.添加新的menu
        List<RoleMenu> roleMenus = addRoleDto.getMenuIds().stream()
                .map(id -> new RoleMenu(addRoleDto.getId(), id))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
    }

    /**
     * 查询正常状态的roles
     * @return
     */
    @Override
    public List<RoleVO> getRolesByStatus() {
        LambdaQueryWrapper<Role> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Role::getStatus, SystemConstants.ROLE_NOMAL_STATUS);
        List<Role> roles = list(lqw);
        List<RoleVO> roleVOS = BeanCopyUtils.copyBeans(roles, RoleVO.class);
        return roleVOS;
    }

    @Autowired
    private UserRoleService userRoleService;
    @Override
    public List<Long> getRoleIds(Long id) {
        LambdaQueryWrapper<UserRole> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserRole::getUserId, id);
        List<UserRole> userRoles = userRoleService.list(lqw);
        List<Long> roleIds = userRoles.stream()
                .map(userRole -> userRole.getRoleId())
                .collect(Collectors.toList());
        return roleIds;
    }
}
