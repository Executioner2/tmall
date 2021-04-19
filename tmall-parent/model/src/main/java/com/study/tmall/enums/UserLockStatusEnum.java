package com.study.tmall.enums;

/**
 * Copyright@1205878539@qq.com
 * Author:2Executioner
 * Date:2021-04-11 17:41
 * Versions:1.0.0
 * Description:
 */
public enum UserLockStatusEnum {
    LOCK(0, "锁定"),
    UNLOCK(1, "正常")
    ;

    private Integer status;
    private String name;

    UserLockStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    /**
     * 根据状态获取状态名
     * @param status
     * @return
     */
    public static String getStatusNameByStatus(Integer status) {
        UserLockStatusEnum[] values = UserLockStatusEnum.values();
        for (UserLockStatusEnum obj : values) {
            if (status.intValue() == obj.getStatus().intValue()) {
                return obj.getName();
            }
        }
        return "";
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
