package com.ry.community.service;

import com.ry.community.dto.CommentDTO;
import com.ry.community.enums.CommentTypeEnum;
import com.ry.community.enums.NotificationStatusEnum;
import com.ry.community.enums.NotificationTypeEnum;
import com.ry.community.exception.CustomizeErrorCodeImpl;
import com.ry.community.exception.CustomizeException;
import com.ry.community.mapper.*;
import com.ry.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 16:50 2019/8/26
 * @Version 1.0
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionMaperCustom questionMaperCustom;
    @Autowired
    private CommentMaperCustom commentMaperCustom;
    @Autowired
    UserMapper userMapper;
    @Autowired
    NotificationMapper notificationMapper;

    @ResponseBody
    @Transactional(rollbackFor = RuntimeException.class)
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCodeImpl.TARGET_PARAM_NOT_FIND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCodeImpl.TYPE_TARGET_ERROR);
        }
        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType())) {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCodeImpl.REPLY_COMMENT_NOT_FIND);
            }
            commentMapper.insert(comment);
            commentMaperCustom.incComment(comment.getParentId());
            //创建通知
            createNotify(comment, dbComment.getCommentator(), NotificationTypeEnum.REPLY_COMMENT, dbComment.getContent());
        } else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCodeImpl.REPLY_QUESTION_NOT_FIND);
            }
            commentMapper.insert(comment);
            questionMaperCustom.incComment(comment.getParentId());
            //创建通知
            createNotify(comment, question.getCreator(), NotificationTypeEnum.REPLY_QUESTION, question.getTitle());
        }
    }

    private void createNotify(Comment comment, Long receiver, NotificationTypeEnum notificationTypeEnum, String content) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationTypeEnum.getType());
        notification.setContent(content);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setOuterid(comment.getParentId());
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTarget(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        List<Comment> comments = commentMapper.selectByExampleWithBLOBs(commentExample);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        //获取去重的评论人ID
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);

        //获取评论人并转换为map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> userList = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //转换comment为commentDTO
        List<CommentDTO> commentDTOList = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOList;
    }
}
