package com.wansir.controller;


import com.wansir.pojo.dto.LinkDto;
import com.wansir.pojo.dto.PageDto;
import com.wansir.pojo.dto.PageParameter;
import com.wansir.pojo.entity.Link;
import com.wansir.service.LinkService;
import com.wansir.utils.BeanCopyUtils;
import com.wansir.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/15 10:26
 */
@Api(tags = "后台友链接口")
@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @ApiOperation(value = "查询友链列表")
    @GetMapping("/list")
    public ResponseResult list(PageParameter pageParameter, String name, String status){
        PageDto<LinkDto> pageDto = linkService.listAll(pageParameter, name, status);
        return ResponseResult.okResult(pageDto);
    }

    @ApiOperation(value = "新增友链")
    @PostMapping()
    public ResponseResult addLink(@RequestBody Link link){
        linkService.save(link);
        return ResponseResult.okResult();
    }

    @ApiOperation(value = "根据id查询友链")
    @GetMapping("/{id}")
    public ResponseResult addLink(@PathVariable("id") Long id){
        Link link = linkService.getById(id);
        LinkDto linkDto = BeanCopyUtils.copyBean(link, LinkDto.class);
        return ResponseResult.okResult(linkDto);
    }

    @ApiOperation(value = "修改友链")
    @PutMapping()
    public ResponseResult updateLink(@RequestBody Link link){
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    @ApiOperation(value = "删除友链")
    @DeleteMapping("/{id}")
    public ResponseResult deleteLinkByIds(@PathVariable("id") String ids){
        linkService.deleteByIds(ids);
        return ResponseResult.okResult();
    }
}
