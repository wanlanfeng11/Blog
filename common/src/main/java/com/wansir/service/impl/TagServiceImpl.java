package com.wansir.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansir.mapper.TagMapper;
import com.wansir.pojo.dto.PageDto;
import com.wansir.pojo.dto.TagDto;
import com.wansir.pojo.entity.Tag;
import com.wansir.service.TagService;
import com.wansir.utils.BeanCopyUtils;
import com.wansir.utils.ResponseResult;
import com.wansir.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2023-05-12 12:02:57
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageDto> pageTagList(Integer pageNum, Integer pageSize, TagDto tagListDto) {
        //分页查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());

        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        //封装数据返回
        PageDto pageDto = new PageDto(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageDto);
    }

    @Override
    public ResponseResult addTag(Tag tag) {
        //获取用户id
        Long userId = SecurityUtils.getUserId();
        //补充数据
        tag.setCreateBy(userId);
        tag.setCreateTime(new Date());
        boolean save = save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateTag(Tag tag) {
        Long userId = SecurityUtils.getUserId();
        tag.setUpdateBy(userId);
        tag.setUpdateTime(new Date());
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTagById(Long id) {
        Tag tag = getById(id);
        TagDto tagDto = BeanCopyUtils.copyBean(tag, TagDto.class);
        return ResponseResult.okResult(tagDto);
    }

    @Override
    public ResponseResult deleteTagById(String ids) {
        String[] idstr = ids.split(",");
        for (String id : idstr) {
            boolean b = this.removeById(Integer.valueOf(id));
        }

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllTag() {
        List<Tag> list = list();
        List<TagDto> tagDtos = BeanCopyUtils.copyBeans(list, TagDto.class);
        return ResponseResult.okResult(tagDtos);
    }
}
