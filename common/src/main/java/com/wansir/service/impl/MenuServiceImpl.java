package com.wansir.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.wansir.enums.AppHttpCodeEnum;
import com.wansir.enums.SystemConstants;
import com.wansir.exception.SystemException;
import com.wansir.mapper.MenuMapper;
import com.wansir.pojo.dto.MenuDto;
import com.wansir.pojo.dto.MenuVO;
import com.wansir.pojo.dto.RoutersDto;
import com.wansir.pojo.entity.Menu;
import com.wansir.pojo.entity.RoleMenu;
import com.wansir.service.MenuService;
import com.wansir.service.RoleMenuService;
import com.wansir.utils.BeanCopyUtils;
import com.wansir.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-05-12 19:32:13
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if(id == 1L){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<RoutersDto> selectRouterMenuTreeByUserId(Long userId) {
        List<RoutersDto> menus = null;
        //判断是否是管理员
        if(SecurityUtils.isAdmin()){
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
            //否则  获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<RoutersDto> menuTree = builderMenuTree(menus,SystemConstants.MENU_ROOT_ID);
        return menuTree;
    }

    /**
     * 后台获取菜单列表
     * @param status 菜单状态
     * @param menuName  菜单名
     * @return
     */
    @Override
    public List<MenuDto> getList(String status, String menuName) {
        LambdaQueryWrapper<Menu> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StringUtils.hasText(status), Menu::getStatus, status);
        lqw.like(StringUtils.hasText(menuName), Menu::getMenuName, menuName);
        lqw.orderByAsc(Menu::getParentId).orderByAsc(Menu::getOrderNum);
        List<Menu> list = list(lqw);
        List<MenuDto> menuDtos = BeanCopyUtils.copyBeans(list, MenuDto.class);
        return menuDtos;
    }

    @Override
    public MenuDto getMenuById(Long id) {
        Menu menu = getById(id);
        MenuDto menuDto = BeanCopyUtils.copyBean(menu, MenuDto.class);
        return menuDto;
    }

    /**
     * 更新菜单
     * @param menu 菜单数据
     */
    @Override
    public void updateMenu(Menu menu) {
        //检验设置的父菜单id是否与当前id相同
        if(menu.getId().equals(menu.getParentId())){
            //如果相同则报错
            throw new SystemException(AppHttpCodeEnum.MENU_ID_ERRO);
        }
        updateById(menu);
        return ;
    }

    @Override
    public void deleteMenuById(Long id) {
        //根据id查询子菜单
        LambdaQueryWrapper<Menu> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Menu::getParentId, id);
        List<Menu> list = list(lqw);
        //判断当前菜单是否有子菜单
        if(list == null || list.isEmpty()){
            removeById(id);
        }else{
            throw new SystemException(AppHttpCodeEnum.HAVE_MENU);
        }

    }

    /**
     * 查询所有菜单，构建菜单树
     * @return
     */
    @Override
    public List<MenuVO> treeSelect() {
        //查询所有菜单
        LambdaQueryWrapper<Menu> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(Menu::getOrderNum);
        List<Menu> menus = list(lqw);
        // 转为MenuVO
        List<MenuVO> menuVOS = menus.stream()
                .map(menu -> {
                    MenuVO menuVO = BeanCopyUtils.copyBean(menu, MenuVO.class);
                    menuVO.setLabel(menu.getMenuName());
                    return menuVO;
                })
                .collect(Collectors.toList());
        List<MenuVO> res = getChildren(SystemConstants.MENU_ROOT_ID, menuVOS);
        System.out.println(res);
        return res;
    }

    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public List<String> getMenusByRoleId(Long id) {
        //根据id查询roleMenu
        LambdaQueryWrapper<RoleMenu> lqw = new LambdaQueryWrapper<>();
        lqw.eq(RoleMenu::getRoleId, id);
        List<RoleMenu> roleMenus = roleMenuService.list(lqw);
        List<String> menuIds = roleMenus.stream()
                .map(roleMenu -> roleMenu.getMenuId().toString())
                .collect(Collectors.toList());
        //根据menuIds查询menu
        return menuIds;
    }

    private List<MenuVO> getChildren(Long parentId, List<MenuVO> menuVOS) {
        return menuVOS.stream()
                .filter(menuVO -> parentId.equals(menuVO.getParentId()))
                .map(menuVO -> menuVO.setChildren(getChildren(menuVO.getId(), menuVOS)))
                .collect(Collectors.toList());
    }


    private List<RoutersDto> builderMenuTree(List<RoutersDto> menus, Long parentId) {

        List<RoutersDto> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }



    /**
     * 获取存入参数的 子Menu集合
     * @param menu
     * @param menus
     * @return
     */
    private List<RoutersDto> getChildren(RoutersDto menu, List<RoutersDto> menus) {
        List<RoutersDto> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}
