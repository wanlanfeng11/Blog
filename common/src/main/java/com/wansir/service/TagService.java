package com.wansir.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wansir.pojo.dto.PageDto;
import com.wansir.pojo.dto.TagDto;
import com.wansir.pojo.entity.Tag;
import com.wansir.utils.ResponseResult;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-05-12 12:02:57
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageDto> pageTagList(Integer pageNum, Integer pageSize, TagDto tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult updateTag(Tag tag);

    ResponseResult getTagById(Long id);

    ResponseResult deleteTagById(String ids);

    ResponseResult listAllTag();

}
