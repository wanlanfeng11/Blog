package com.wansir.initializa;


import com.wansir.enums.SystemConstants;
import com.wansir.pojo.entity.Article;
import com.wansir.service.ArticleService;
import com.wansir.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description 执行浏览量初始化，将其从数据库缓存到redis中，其中的run方法在spring容器创建后会自动执行
 * @date 2023/5/12 9:20
 */
@Component
public class RedisRunner implements CommandLineRunner {
    /**
     * 注入articleService
     */
    @Autowired
    private ArticleService articleService;

    /**
     * 注入redis工具类
     */
    @Autowired
    private RedisCache redisCache;

    /**
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        //查询所有博客信息
        List<Article> list = articleService.list();
        //建立 id（博客id） 与 viewCount（浏览量）的映射关系
        /*list.stream().collect(Collectors.toMap(new Function<Article, Long>() {
            @Override
            public Long apply(Article article) {
                return article.getId();
            }
        }, new Function<Article, Integer>() {
            @Override
            public Integer apply(Article article) {
                return article.getViewCount().intValue();
            }
        }));*/
        Map<String, Integer> viewCountMap = list.stream().collect(Collectors.toMap(
                article -> article.getId().toString(), article -> article.getViewCount().intValue()));
        //存储到redis
        redisCache.setCacheMap(SystemConstants.ARTICLE_VIEWCOUNT_KEY, viewCountMap);

    }
}
