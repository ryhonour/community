package com.ry.community.enums;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 17:38 2019/9/27
 * @Version 1.0
 */
public enum ProfilesTypeEnum {
    /**
     * 个人档案中我的问题、我的通知的枚举类
     */
    QUESTIONS("questions"),
    NOTIFICATIONS("notification");

    private String type;

    ProfilesTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }

    public Boolean equals(String type) {
        if (this.type.equals(type)) {
            return true;
        }
        return false;
    }
}
