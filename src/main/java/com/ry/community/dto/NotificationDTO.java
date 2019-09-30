package com.ry.community.dto;

import com.ry.community.model.User;
import lombok.Data;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 17:06 2019/9/30
 * @Version 1.0
 */
@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private User notifier;
    private Integer status;
    private String content;
    private Long outerid;
    private String type;
}
