package com.wansir.test;


import com.wansir.mapper.MenuMapper;
import com.wansir.mapper.RoleMapper;
import com.wansir.pojo.dto.RoutersDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/12 20:35
 */
@SpringBootTest
public class MapperTest {
    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Test
    void selectPermsByUserIdTest(){
        List<String> perms = menuMapper.selectPermsByUserId(6L);
        for (String perm : perms) {
            System.out.println(perm);
        }
    }

    @Test
    void selectRoleKeyByUserIdTest(){
        List<String> roles = roleMapper.selectRoleKeyByUserId(1L);
        for (String role : roles) {
            System.out.println(role);
        }
    }

    @Test
    void selectRouterMenuTreeByUserIdTest(){
        List<RoutersDto> routersDtos = menuMapper.selectRouterMenuTreeByUserId(2L);
        for (RoutersDto routersDto : routersDtos) {
            System.out.println(routersDto);
        }
    }
}
