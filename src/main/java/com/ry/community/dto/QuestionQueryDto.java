package com.ry.community.dto;

import lombok.Data;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 11:17 2019/10/7
 * @Version 1.0
 */
@Data
public class QuestionQueryDto {
    private String search;
    private Integer size;
    private Integer page;
}
