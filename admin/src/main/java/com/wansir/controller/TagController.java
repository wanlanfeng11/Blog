package com.wansir.controller;





import com.wansir.pojo.dto.PageDto;
import com.wansir.pojo.dto.TagDto;
import com.wansir.pojo.entity.Tag;
import com.wansir.service.TagService;
import com.wansir.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * @author wanlanfeng
 * @version 1.0
 * @description TODO
 * @date 2023/5/12 12:06
 */

@RestController
@RequestMapping("/content/tag")
@Api(tags = "标签接口")
public class TagController {
    @Autowired
    private TagService tagService;

    @ApiOperation(value = "查询标签列表")
    @GetMapping("/list")
    public ResponseResult<PageDto> list(Integer pageNum, Integer pageSize, TagDto tagDto){
        return tagService.pageTagList(pageNum,pageSize,tagDto);
    }

    @ApiOperation(value = "添加标签")
    @PostMapping
    public ResponseResult addTag(@RequestBody Tag tag){
        return tagService.addTag(tag);
    }

    @ApiOperation(value = "修改标签")
    @PutMapping()
    public ResponseResult updateTagById(@RequestBody Tag tag){
        return tagService.updateTag(tag);
    }

    @ApiOperation(value = "查询标签")
    @GetMapping("/{id}")
    public ResponseResult getTagById(@PathVariable("id") Long id){
        return tagService.getTagById(id);
    }

    @ApiOperation(value = "删除标签")
    @DeleteMapping("/{id}")
    public ResponseResult deleteTagById(@PathVariable("id") String ids){
        return tagService.deleteTagById(ids);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        return tagService.listAllTag();
    }


}
