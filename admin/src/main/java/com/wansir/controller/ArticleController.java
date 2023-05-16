package com.wansir.controller;


import com.wansir.pojo.dto.AddArticleDto;
import com.wansir.pojo.dto.ArticleVO;
import com.wansir.service.ArticleService;
import com.wansir.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/14 10:37
 */
@Api(tags = "博客查询")
@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "添加博客")
    @PostMapping
    public ResponseResult addArticle(@RequestBody AddArticleDto article){
        articleService.addArticle(article);
        return ResponseResult.okResult();
    }

    @ApiOperation(value = "查询博客列表")
    @GetMapping("/list")
    public ResponseResult listArticle(Integer pageNum, Integer pageSize, String title, String summary){
        return articleService.listArticle(pageNum, pageSize, title, summary);
    }

    @ApiOperation(value = "查询博客")
    @GetMapping("/{id}")
    public ResponseResult getArticle(@PathVariable("id") Integer id){
        return articleService.getArticle(id);
    }


    @PutMapping
    @ApiOperation(value = "更新文章")
    public ResponseResult updateArticle(@RequestBody ArticleVO articleVO){
        articleService.updateArticle(articleVO);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除文章")
    public ResponseResult deleteById(@PathVariable("id") Long id){
        articleService.removeById(id);
        return ResponseResult.okResult();
    }


}
