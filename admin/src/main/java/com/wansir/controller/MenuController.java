package com.wansir.controller;


import com.wansir.pojo.dto.MenuDto;
import com.wansir.pojo.dto.MenuVO;
import com.wansir.pojo.entity.Menu;
import com.wansir.service.MenuService;
import com.wansir.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description 后台菜单接口
 * @date 2023/5/14 15:08
 */
@Api(tags = "后台菜单接口")
@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    @ApiOperation(value = "查询菜单列表")
    public ResponseResult getList(String status, String menuName){
        List<MenuDto> list = menuService.getList(status, menuName);
        return ResponseResult.okResult(list);
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping()
    public ResponseResult addMenu(@RequestBody Menu menu){
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    @ApiOperation(value = "查询菜单")
    @GetMapping("/{id}")
    public ResponseResult getMenuById(@ApiParam(value = "菜单id") @PathVariable("id") Long id) {
        MenuDto menuDto = menuService.getMenuById(id);
        return ResponseResult.okResult(menuDto);
    }

    @ApiOperation(value = "更新菜单")
    @PutMapping()
    public ResponseResult updateMenu(@RequestBody Menu menu){
        menuService.updateMenu(menu);
        return ResponseResult.okResult();
    }

    @ApiOperation(value = "删除菜单")
    @DeleteMapping("{menuId}")
    public ResponseResult deleteMenuById(@PathVariable("menuId") Long id){
        menuService.deleteMenuById(id);
        return ResponseResult.okResult();
    }

    @ApiOperation(value = "查询菜单树")
    @GetMapping("/treeselect")
    public ResponseResult treeSelect(){
        List<MenuVO> menuVOS = menuService.treeSelect();
        return ResponseResult.okResult(menuVOS);
    }

    @ApiOperation(value = "根据角色id查询菜单树")
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult getMenusByRoleId(@PathVariable("id") Long id){
        HashMap<Object, Object> map = new HashMap<>();
        //查询菜单树
        List<MenuVO> menuVOS = menuService.treeSelect();
        //查询角色菜单列表
        List<String> menuIds = menuService.getMenusByRoleId(id);
        map.put("menus", menuVOS);
        map.put("checkedKeys", menuIds);
        return ResponseResult.okResult(map);
    }
}
