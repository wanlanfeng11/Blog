package com.wansir.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansir.enums.SystemConstants;
import com.wansir.mapper.ArticleMapper;
import com.wansir.pojo.dto.*;
import com.wansir.pojo.entity.Article;
import com.wansir.pojo.entity.ArticleTag;
import com.wansir.pojo.entity.Category;
import com.wansir.service.ArticleService;
import com.wansir.service.ArticleTagService;
import com.wansir.service.CategoryService;
import com.wansir.utils.BeanCopyUtils;
import com.wansir.utils.RedisCache;
import com.wansir.utils.ResponseResult;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/10 21:03
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryService categoryService;


    /**
     * 查询浏览量最高的前10篇文章的信息
     * @return
     */
    @Override
    public ResponseResult hotArticleList() {
        //构造查询条件
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        //查询已发布的文章
        lqw.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览点击量降序排列
        lqw.orderByDesc(Article::getViewCount);
        //分页获取前十名
        Page<Article> page = new Page<>(1, 10);
        Page<Article> articlePage = articleMapper.selectPage(page, lqw);
        List<ArticleDto> articleDtos = BeanCopyUtils.copyBeans(articlePage.getRecords(), ArticleDto.class);
        //返回数据
        return ResponseResult.okResult(articleDtos);

    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要 查询时要和传入的相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0 ,Article::getCategoryId,categoryId);
        // 状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);

        List<Article> articles = page.getRecords();
        //查询categoryName
        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
        //articleId去查询articleName进行设置
//        for (Article article : articles) {
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(category.getName());
//        }

        //封装查询结果
        List<ArticleListDto> articleListVos = BeanCopyUtils.copyBeans(page.getRecords(), ArticleListDto.class);

        PageDto pageDto = new PageDto(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageDto);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEWCOUNT_KEY, id.toString());
        article.setViewCount(viewCount.longValue());
        //转换成Dto
        ArticleDetailDto articleDetailDto = BeanCopyUtils.copyBean(article, ArticleDetailDto.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailDto.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category!=null){
            articleDetailDto.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailDto);
    }


    @Autowired
    private RedisCache redisCache;
    /**
     * 更新浏览量：更新redis中存储的浏览量
     * @param id
     * @return
     */
    @Override
    public ResponseResult updateViewCount(Long id) {
        //存入redis
        redisCache.incrementCacheMapValue(SystemConstants.ARTICLE_VIEWCOUNT_KEY, id.toString(), 1);
        return ResponseResult.okResult();
    }


    @Autowired
    private ArticleTagService articleTagService;
    @Override
    @Transactional
    public void addArticle(AddArticleDto articleDto) {
        //存入article表
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);
        Long articleId = article.getId();
        //存入article_tag表
        List<ArticleTag> articleTags = articleDto.getTags()
                .stream()
                .map(tagId -> new ArticleTag(articleId, tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
    }

    @Override
    public ResponseResult listArticle(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StringUtils.hasText(title), Article::getTitle, title);
        lqw.eq(StringUtils.hasText(summary), Article::getSummary, summary);
        Page<Article> page = new Page<>(pageNum, pageSize);
        Page<Article> res = page(page, lqw);
        List<Article> articles = BeanCopyUtils.copyBeans(res.getRecords(), Article.class);
        return ResponseResult.okResult(new PageDto(articles, res.getTotal()));
    }

    @Override
    public ResponseResult getArticle(Integer id) {
        //查询article表
        Article article = getById(id);
        //查询article_tag表
        LambdaQueryWrapper<ArticleTag> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleTag::getArticleId, id);
        List<ArticleTag> list = articleTagService.list(lqw);
        List<Long> tagIds = list.stream().map(articleTag -> articleTag.getTagId()).collect(Collectors.toList());
        ArticleVO articleVO = BeanCopyUtils.copyBean(article, ArticleVO.class);
        articleVO.setTags(tagIds);
        return ResponseResult.okResult(articleVO);
    }

    /**
     * 更新文章内容
     * @param articleVO 文章参数
     */
    @Override
    public void updateArticle(ArticleVO articleVO) {
        //修改article表
        Article article = BeanCopyUtils.copyBean(articleVO, Article.class);
        this.updateById(article);
        //修改article_tag表
        //删除对应的原始tag
        LambdaQueryWrapper<ArticleTag> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleTag::getArticleId, articleVO.getId());
        articleTagService.remove(lqw);
        //添加新的tag
        List<ArticleTag> articleTags = articleVO.getTags()
                .stream()
                .map(tag -> new ArticleTag(articleVO.getId(), tag))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);

    }

}
