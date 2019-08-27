package com.ry.community.dto;

import com.ry.community.model.User;
import lombok.Data;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 15:43 2019/8/27
 * @Version 1.0
 */
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModify;
    private Long likeCount;
    private Long commentCount;
    private String content;
    private User user;
}
