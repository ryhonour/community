package com.ry.community.controller;

import com.ry.community.dto.CommentCreateDTO;
import com.ry.community.dto.CommentDTO;
import com.ry.community.dto.ResultDTO;
import com.ry.community.enums.CommentTypeEnum;
import com.ry.community.exception.CustomizeErrorCodeImpl;
import com.ry.community.model.Comment;
import com.ry.community.model.User;
import com.ry.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 16:04 2019/8/26
 * @Version 1.0
 */
@Controller
public class CommentController {
    @Autowired
    CommentService commentService;

    /**
     * 提交评论
     */
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCodeImpl.USER_NOT_FIND);
        }

        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCodeImpl.COMMENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModify(comment.getGmtCreate());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        comment.setCommentCount(0L);
        commentService.insert(comment);
        return ResultDTO.okOf();
    }

    /**
     * 获取二级评论
     */
    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id) {
        List<CommentDTO> commentDTOList = commentService.listByTarget(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOList);
    }
}
