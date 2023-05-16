package com.wansir.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wansir.pojo.dto.RoutersDto;
import com.wansir.pojo.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-12 19:32:11
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<RoutersDto> selectAllRouterMenu();

    List<RoutersDto> selectRouterMenuTreeByUserId(Long userId);
}

