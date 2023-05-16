package com.wansir.controller;


import com.wansir.service.LinkService;
import com.wansir.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/11 11:08
 */
@Api(tags = "友链接口")
@RestController
@RequestMapping("/link")
public class LinkedController{
    @Autowired
    private LinkService linkService;

    @ApiOperation(value = "查询友链")
    @GetMapping("/getAllLink")
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }
}
