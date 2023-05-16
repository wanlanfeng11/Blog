package com.wansir.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wansir.pojo.dto.MenuDto;
import com.wansir.pojo.dto.MenuVO;
import com.wansir.pojo.dto.RoutersDto;
import com.wansir.pojo.entity.Menu;

import java.util.List;
import java.util.Map;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-05-12 19:32:12
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<RoutersDto> selectRouterMenuTreeByUserId(Long userId);

    List<MenuDto> getList(String status, String menuName);

    MenuDto getMenuById(Long id);

    void updateMenu(Menu menu);

    void deleteMenuById(Long id);

    List<MenuVO> treeSelect();

    List<String> getMenusByRoleId(Long id);
}
