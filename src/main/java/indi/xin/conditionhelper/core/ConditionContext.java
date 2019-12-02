package indi.xin.conditionhelper.core;

import indi.xin.conditionhelper.core.type.SQLTypeHandler;
import indi.xin.conditionhelper.core.type.SQLTypeHandlerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConditionContext {

    private List<Condition> conditions;

    public <T> void init(List<String> fields, List<String> tableNames,
                     List<ConditionType> conditionTypes, Map<Integer, List<T>> valueMap) {
        if(fields.size() != tableNames.size()) {
            throw new RuntimeException("条件设置错误");
        }

        if(fields.size() > 0) {
            List<Condition> conditions = new ArrayList<>();
            for (int i = 0; i < fields.size(); i++) {
                Condition condition = new Condition();
                condition.setField(fields.get(i));
                condition.setTableName(tableNames.get(i));
                condition.setConditionType(conditionTypes.get(i));
                condition.setValues(valueMap.get(i));
                condition.setSqlTypeHandler(SQLTypeHandlerFactory.build(conditionTypes.get(i)));

                conditions.add(condition);
            }
            this.setConditions(conditions);
        }

    }

    public class Condition<T> {

        private String field;

        private String tableName;

        private ConditionType conditionType;

        private List<T> values;

        private SQLTypeHandler sqlTypeHandler;

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

        public String buildConditionSql(List<T> values) {
            assert values != null && !values.isEmpty();

            StringBuilder sb = new StringBuilder();
            sb.append(tableName);
            sb.append(".");
            sb.append(field);
            sb.append(conditionType.getValue()); //fixme

            //fixme
            sb.append("(");
            for (int i = 0; i < values.size(); i++) {
                if(i == 0) {
                    sb.append(values.get(i));
                    continue;
                }
                sb.append(",");
                sb.append(values.get(i));
            }
            sb.append(")");

            return sb.toString();
        }
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

}
