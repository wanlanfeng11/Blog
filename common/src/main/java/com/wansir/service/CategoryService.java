package com.wansir.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wansir.pojo.entity.Category;
import com.wansir.utils.ResponseResult;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-05-11 09:37:01
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult listAll(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult listAll();
}
