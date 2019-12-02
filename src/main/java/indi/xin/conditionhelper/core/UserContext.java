package indi.xin.conditionhelper.core;

import java.util.ArrayList;
import java.util.List;

public class UserContext {

    private String userId;

    private List<Integer> ids;

    private List<Condition> conditions;

    public void init(List<Integer> ids, List<String> fields, List<String> tableNames) {
        this.setIds(ids);
        if(fields.size() != tableNames.size()) {
            throw new RuntimeException("条件设置错误");
        }

        if(fields.size() > 0) {
            List<Condition> conditions = new ArrayList<>();
            for (int i = 0; i < fields.size(); i++) {
                Condition condition = new Condition();
                condition.setField(fields.get(i));
                condition.setTableName(tableNames.get(i));
                conditions.add(condition);
            }
            this.setConditions(conditions);
        }

    }

    public class Condition<T> {
        private String field;

        private String tableName;

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
            sb.append(" in ");

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
