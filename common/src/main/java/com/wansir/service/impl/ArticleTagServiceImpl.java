package com.wansir.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansir.mapper.ArticleTagMapper;
import com.wansir.pojo.entity.ArticleTag;
import com.wansir.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2023-05-14 11:26:27
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
