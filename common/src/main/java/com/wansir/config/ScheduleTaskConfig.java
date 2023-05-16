package com.wansir.config;


import com.wansir.enums.SystemConstants;
import com.wansir.pojo.entity.Article;
import com.wansir.service.ArticleService;
import com.wansir.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description 定时任务配置类
 * @date 2023/5/12 9:25
 */
@Configuration  //配置类
public class ScheduleTaskConfig {


    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    /**
     * 定时任务：cron：设置定时任务时间的表达式
     */
    @Scheduled(cron = "0/20 * * * * ? ")
    public void updateViewCount(){
        //获取浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.ARTICLE_VIEWCOUNT_KEY);
        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), Long.valueOf(entry.getValue())))
                .collect(Collectors.toList());
        //更新到数据库中
        articleService.updateBatchById(articles);
    }
}
