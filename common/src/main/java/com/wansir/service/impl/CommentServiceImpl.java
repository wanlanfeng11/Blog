package com.wansir.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wansir.enums.AppHttpCodeEnum;
import com.wansir.enums.SystemConstants;
import com.wansir.exception.SystemException;
import com.wansir.mapper.CommentMapper;
import com.wansir.pojo.dto.CommentDto;
import com.wansir.pojo.dto.PageDto;
import com.wansir.pojo.entity.Comment;
import com.wansir.service.CommentService;
import com.wansir.service.UserService;
import com.wansir.utils.BeanCopyUtils;
import com.wansir.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-05-11 16:43:07
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(String commenType, Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //对articleId进行判断
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commenType), Comment::getArticleId,articleId);
        //根评论 rootId为-1
        queryWrapper.eq(Comment::getRootId,-1);
        //评论类型
        queryWrapper.eq(Comment::getType, commenType);

        //分页查询
        Page<Comment> page = new Page(pageNum,pageSize);
        page(page,queryWrapper);

        List<CommentDto> commentDtoList = toCommentDtoList(page.getRecords());
        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentDto commentDto : commentDtoList) {
            //查询对应的子评论
            List<CommentDto> children = getChildren(commentDto.getId());
            //赋值
            commentDto.setChildren(children);
        }

        return ResponseResult.okResult(new PageDto(commentDtoList,page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能为空
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }

        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 根据根评论的id查询所对应的子评论的集合
     * @param id 根评论的id
     * @return
     */
    private List<CommentDto> getChildren(Long id) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);

        List<CommentDto> commentDtos = toCommentDtoList(comments);
        return commentDtos;
    }

    private List<CommentDto> toCommentDtoList(List<Comment> list){
        List<CommentDto> commentDtos = BeanCopyUtils.copyBeans(list, CommentDto.class);
        //遍历Dto集合
        for (CommentDto commentDto : commentDtos) {
            //通过creatyBy查询用户的昵称并赋值
            String nickName = userService.getById(commentDto.getCreateBy()).getNickName();
            commentDto.setUsername(nickName);
            //通过toCommentUserId查询用户的昵称并赋值
            //如果toCommentUserId不为-1才进行查询
            if(commentDto.getToCommentUserId()!=-1){
                String toCommentUserName = userService.getById(commentDto.getToCommentUserId()).getNickName();
                commentDto.setToCommentUserName(toCommentUserName);
            }
        }
        return commentDtos;
    }
}
