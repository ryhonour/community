package com.ry.community.dto;

import com.ry.community.model.User;
import lombok.Data;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 19:51 2019/8/19
 * @Version 1.0
 */
@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private String tags;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
