package com.qhx.conditionhelper.core.start;

import com.qhx.conditionhelper.core.Condition;
import com.qhx.conditionhelper.core.ConditionContextHolder;
import com.qhx.conditionhelper.core.ConditionType;

import java.util.ArrayList;
import java.util.List;

public class ConditionHelper {

    private static List<Condition> conditions = new ArrayList<>();

    public static void clearLocalConditions() {
        ConditionContextHolder.conditionContextThreadLocal.remove();
    }

    public static <T> Condition addCondition(String field, String tableName, ConditionType conditionType, List<T> values) {
        validSqlCondition(field, tableName, conditionType, values);

        Condition condition = new Condition(field, tableName, conditionType, values);
        conditions.add(condition);

        return condition;
    }

    public static void startCondition() {
        startConditionInteral();
    }

    private static void startConditionInteral() {
        List<Condition> localConditions = ConditionContextHolder.conditionContextThreadLocal.get().getConditions();

        if (localConditions == null) {
            localConditions = conditions;
        } else {
            localConditions.addAll(conditions);
        }
    }

    public static <T> void startCondition(String field, String tableName, ConditionType conditionType, List<T> values) {
        addCondition(field, tableName, conditionType, values);
        startConditionInteral();
    }

    private static <T> void validSqlCondition(String field, String tableName, ConditionType conditionType, List<T> values) {
        if(field == null || "".equals(field)) {
            throw new IllegalArgumentException("field 不能为空");
        }
        if(tableName == null || "".equals(tableName)) {
            throw new IllegalArgumentException("tableName 不能为空");
        }
        if(conditionType == null) {
            throw new IllegalArgumentException("conditionType 不能为空");
        }
        if(values == null || values.isEmpty()) {
            throw new IllegalArgumentException("values 不能为空");
        }
    }

}
