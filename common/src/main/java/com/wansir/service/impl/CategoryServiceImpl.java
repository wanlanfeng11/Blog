package com.wansir.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansir.enums.SystemConstants;
import com.wansir.mapper.CategoryMapper;
import com.wansir.pojo.dto.CategoryDto;
import com.wansir.pojo.dto.PageDto;
import com.wansir.pojo.entity.Article;
import com.wansir.pojo.entity.Category;
import com.wansir.service.ArticleService;
import com.wansir.service.CategoryService;
import com.wansir.utils.BeanCopyUtils;
import com.wansir.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.xml.ws.Response;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-05-11 09:37:02
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;
    @Override
    public ResponseResult getCategoryList() {
        //查询文章表  状态为已发布的文章
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章的分类id，并且去重
        Set<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());

        //查询分类表
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream().
                filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //封装vo
        List<CategoryDto> categoryVos = BeanCopyUtils.copyBeans(categories, CategoryDto.class);

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listAll() {
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Category::getStatus, SystemConstants.STATUS_NORMAL);
        List<Category> list = list(lqw);
        List<CategoryDto> categoryDtos = BeanCopyUtils.copyBeans(list, CategoryDto.class);
        return ResponseResult.okResult(categoryDtos);
    }

    @Override
    public ResponseResult listAll(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(StringUtils.hasText(status), Category::getStatus, status);
        lqw.like(StringUtils.hasText(name), Category::getName, name);
        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page, lqw);
        List<CategoryDto> categoryDtos = BeanCopyUtils.copyBeans(page.getRecords(), CategoryDto.class);
        PageDto pageDto = new PageDto(categoryDtos, page.getTotal());
        return ResponseResult.okResult(pageDto);
    }
}
