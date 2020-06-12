package com.qhx.conditionhelper.core;

/**
 * @Description: 条件类型
 * @Author: red
 * @Time: 2019/12/02 22:46
 */
public enum ConditionType {

    IN("IN", "IN"),
    EQ("=", "EQ"),
    GT(">", "GT"),
    LT("<", "LT"),
    GE(">=", "GE"),
    LE("<=", "LE"),
    NOT_IN("NOT IN", "NOT_IN"),
    BETWEEN("BETWEEN", "BETWEEN");

    private String name;

    private String desc;

    ConditionType(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
