package indi.xin.conditionhelper.demo;

import indi.xin.conditionhelper.core.ConditionContext;

/**
 * @Description:
 * @Author: red
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
