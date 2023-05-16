package com.wansir.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wansir.pojo.dto.AddArticleDto;
import com.wansir.pojo.dto.ArticleVO;
import com.wansir.pojo.entity.Article;
import com.wansir.utils.ResponseResult;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/10 21:03
 */
public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    void addArticle(AddArticleDto article);

    ResponseResult listArticle(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult getArticle(Integer id);

    void updateArticle(ArticleVO articleVO);
}
