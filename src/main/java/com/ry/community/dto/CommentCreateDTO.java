package com.ry.community.dto;

import lombok.Data;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 16:12 2019/8/26
 * @Version 1.0
 */
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
