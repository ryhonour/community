package com.ry.community.service;

import com.ry.community.dto.NotificationDTO;
import com.ry.community.dto.PaginationDTO;
import com.ry.community.enums.NotificationStatusEnum;
import com.ry.community.enums.NotificationTypeEnum;
import com.ry.community.exception.CustomizeErrorCodeImpl;
import com.ry.community.exception.CustomizeException;
import com.ry.community.mapper.CommentMapper;
import com.ry.community.mapper.NotificationMapper;
import com.ry.community.mapper.UserMapper;
import com.ry.community.model.*;
import com.ry.community.util.PageUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 17:02 2019/9/30
 * @Version 1.0
 */
@Service
public class NotificationService {
    @Autowired
    NotificationMapper notificationMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CommentMapper commentMapper;

    public PaginationDTO<NotificationDTO> list(Integer currentPage, Integer size, User user) {
        ArrayList<NotificationDTO> notificationDTOList = new ArrayList<>();
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(user.getId());
        notificationExample.setOrderByClause("gmt_create DESC");
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);
        PageUtil pageUtil = new PageUtil(totalCount, size, currentPage);
        RowBounds rowBounds = new RowBounds(pageUtil.getOffset(), size);
        List<Notification> notificationList = notificationMapper.selectByExampleWithBLOBsWithRowbounds(notificationExample, rowBounds);
        if (notificationList.size() == 0) {
            return paginationDTO;
        }
        //获取去重的User id
        Set<Long> userIdSet = notificationList.stream().map(notification -> notification.getNotifier()).collect(Collectors.toSet());
        ArrayList<Long> userIdList = new ArrayList<>(userIdSet);
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIdList);
        List<User> userList = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(u -> u.getId(), u -> u));

        for (Notification notification : notificationList) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setNotifier(userMap.get(notification.getNotifier()));
            notificationDTO.setType(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOList.add(notificationDTO);
        }

        paginationDTO.setTList(notificationDTOList);
        paginationDTO.setPagination(pageUtil.getTotalPage(), pageUtil.getCurrentPage());
        return paginationDTO;
    }

    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus())
                .andReceiverEqualTo(userId);
        long count = notificationMapper.countByExample(notificationExample);
        return count;
    }

    public Long getOuterId(Long id) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        //更改未读状态
        Notification record = new Notification();
        record.setId(id);
        record.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKeySelective(record);

        if (notification.getType() == NotificationTypeEnum.REPLY_QUESTION.getType()) {
            //该回复对应的为问题
            return notification.getOuterid();
        } else if (notification.getType() == NotificationTypeEnum.REPLY_COMMENT.getType()) {
            //该回复对应的为评论
            Comment comment = commentMapper.selectByPrimaryKey(notification.getOuterid());
            if (comment == null) {
                throw new CustomizeException(CustomizeErrorCodeImpl.COMMENT_NOT_FIND);
            }
            return comment.getParentId();
        }
        return null;
    }
}
