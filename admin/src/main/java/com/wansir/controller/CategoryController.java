package com.wansir.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson.JSON;
import com.wansir.enums.AppHttpCodeEnum;
import com.wansir.pojo.dto.CategoryDto;
import com.wansir.pojo.dto.EasyExcelVO;
import com.wansir.pojo.entity.Category;
import com.wansir.service.CategoryService;
import com.wansir.utils.BeanCopyUtils;
import com.wansir.utils.ResponseResult;
import com.wansir.utils.WebUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/13 14:25
 */
@Api(tags = "文章分类接口")
@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    @ApiOperation(value = "获取文章分类列表")
    @GetMapping("/listAllCategory")
    public ResponseResult getAllCategory(){
        return categoryService.listAll();
    }

    @ApiOperation(value = "获取文章分类列表：分页版")
    @GetMapping("/list")
    public ResponseResult getCategoryList(Integer pageNum, Integer pageSize, String name, String status){
        return categoryService.listAll(pageNum, pageSize, name, status);
    }

    @PreAuthorize("@permissionService.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){

        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx", response);

            //获取需要导出的数据
            List<Category> list = categoryService.list();
            List<EasyExcelVO> easyExcelVOS = BeanCopyUtils.copyBeans(list, EasyExcelVO.class);
            //将数据写入Excel
            EasyExcel.write(response.getOutputStream(), EasyExcelVO.class).autoCloseStream(Boolean.FALSE).sheet("模板").doWrite(easyExcelVOS);
            //
        } catch (Exception e) {
            // 重置response
            response.reset();
            //写入错误相关信息
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(responseResult));
        }

    }

    @ApiOperation(value = "添加文章分类")
    @PostMapping
    public ResponseResult addCategory(@RequestBody Category category){
        categoryService.save(category);
        return ResponseResult.okResult();
    }

    @ApiOperation(value = "根据id查询分类")
    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable("id") Long id){
        Category category = categoryService.getById(id);
        CategoryDto categoryDto = BeanCopyUtils.copyBean(category, CategoryDto.class);
        return ResponseResult.okResult(categoryDto);
    }

    @ApiOperation(value = "更新分类")
    @PutMapping()
    public ResponseResult updateCategory(@RequestBody Category category){
        categoryService.updateById(category);
        return ResponseResult.okResult();
    }

    @ApiOperation(value = "删除分类")
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategoryById(@PathVariable("id") Long id){
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }


}
