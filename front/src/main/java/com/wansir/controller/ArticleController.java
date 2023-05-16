package com.wansir.controller;


import com.wansir.pojo.entity.Article;
import com.wansir.service.ArticleService;
import com.wansir.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description 文章接口
 * @date 2023/5/10 21:01
 */
@RestController
@RequestMapping("/article")
@Api(tags = "文章接口")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 热门文章列表查询
     * @return
     */
    @GetMapping("/hotArticleList")
    @ApiOperation(value = "热点文章查询")
    public ResponseResult hotArticleList(){

        ResponseResult result =  articleService.hotArticleList();
        return result;
    }


    @GetMapping("/articleList")
    @ApiOperation(value = "文章列表查询")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "文章详情查询")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }


    @PutMapping("/updateViewCount/{id}")
    @ApiOperation(value = "更新文章浏览量")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
