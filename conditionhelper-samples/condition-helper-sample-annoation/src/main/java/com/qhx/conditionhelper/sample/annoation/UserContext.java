package com.qhx.conditionhelper.sample.annoation;

import com.qhx.conditionhelper.core.ConditionContext;

/**
 * @Description:
 * @Author: hongxin.qian
 * @Time: 2019/12/02 23:22
 */
public class UserContext extends ConditionContext {

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
