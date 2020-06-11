package com.qhx.conditionhelper.core;

public class ConditionContextHolder {

    public static ThreadLocal<ConditionContext> conditionContextThreadLocal = new ThreadLocal<>();

}
