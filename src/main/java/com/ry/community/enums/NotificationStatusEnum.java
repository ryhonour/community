package com.ry.community.enums;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 16:32 2019/9/30
 * @Version 1.0
 */
public enum NotificationStatusEnum {
    /**
     * UNREAD:未读，READ:已读
     */
    UNREAD(0), READ(1);

    private int status;

    NotificationStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
