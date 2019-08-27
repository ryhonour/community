package com.ry.community.enums;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 16:45 2019/8/26
 * @Version 1.0
 */
public enum CommentTypeEnum {
    /**
     * QUESTION(1):父类类型为question
     * COMMENT(2):父类类型为comment
     */
    QUESTION(1),
    COMMENT(2);
    private Integer type;

    public static boolean isExist(Integer type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if (commentTypeEnum.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }
}
