package indi.xin.conditionhelper.core;

import indi.xin.conditionhelper.core.type.SQLTypeHandler;
import indi.xin.conditionhelper.core.type.SQLTypeHandlerFactory;

import java.util.List;

public class Condition<T> {

    private String field;

    private String tableName;

    private ConditionType conditionType;

    private List<T> values;

    private SQLTypeHandler sqlTypeHandler;

    public Condition(String field, String tableName, ConditionType conditionType, List values) {
        this.setField(field);
        this.setTableName(tableName);
        this.setConditionType(conditionType);
        this.setValues(values);
        this.setSqlTypeHandler(SQLTypeHandlerFactory.createTypeHandler(conditionType));
    }

    public SQLTypeHandler getSqlTypeHandler() {
        return sqlTypeHandler;
    }

    public void setSqlTypeHandler(SQLTypeHandler sqlTypeHandler) {
        this.sqlTypeHandler = sqlTypeHandler;
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String buildConditionSql() {
        assert values != null && !values.isEmpty();

        StringBuilder sb = new StringBuilder();
        sb.append(tableName);
        sb.append(".");
        sb.append(field);
        sb.append(sqlTypeHandler.build(values));

        return sb.toString();
    }

}
