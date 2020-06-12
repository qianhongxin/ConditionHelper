package com.qhx.conditionhelper.core;

import com.qhx.conditionhelper.core.type.SQLTypeHandler;
import com.qhx.conditionhelper.core.type.SQLTypeHandlerFactory;

import java.util.List;

public class Condition<T> {

    private String field;

    private String tableName;

    private ConditionType conditionType;

    private List<T> values;

    private SQLTypeHandler sqlTypeHandler;

    public Condition(String field, String tableName, ConditionType conditionType, List values) {
        this.field = field;
        this.tableName = tableName;
        this.conditionType = conditionType;
        this.values = values;
        this.sqlTypeHandler = SQLTypeHandlerFactory.createTypeHandler(conditionType);
    }

    public String getTableName() {
        return tableName;
    }

    public String buildConditionSql() {
        assert values != null && !values.isEmpty();

        StringBuilder sb = new StringBuilder();
        sb.append(tableName);
        sb.append(".");
        sb.append(field);
        sb.append(conditionType.getName());
        sb.append(sqlTypeHandler.build(values));

        return sb.toString();
    }

}
