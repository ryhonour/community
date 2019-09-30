package com.ry.community.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 11:55 2019/9/27
 * @Version 1.0
 */
@Data
public class TagDTO {
    private String tagCategoryName;
    private String tagCategoryEnName;
    private List<String> tagList;
}
