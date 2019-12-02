package indi.xin.conditionhelper.core.parser;

import indi.xin.conditionhelper.core.UserContext;

import java.util.List;

public class DefaultSqlParser implements SqlParser {

    @Override
    public <T> String parse(String sql, List<T> values, List<UserContext.Condition> conditions) {
        for (int i = 0; i < conditions.size(); i++) {
            sql = parseInteral(sql, values, conditions.get(i));
        }

        return sql;
    }

    public <T> String parseInteral(String sql, List<T> values, UserContext.Condition condition) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 20);
        String field = condition.getField();
        String tableName = condition.getTableName();
        // 获取字段的索引
        int tableNameIndex = sql.indexOf("as " + tableName);
        if(tableNameIndex == -1) {
            tableNameIndex = sql.indexOf("AS " + tableName);
            if(tableNameIndex == -1) {
                tableNameIndex = sql.indexOf(tableName);
            }
        }
        if(tableNameIndex == -1) {
            return sql;
        }
        // 获取距离字段最近的第一个where的索引
        int firstWhereIndex = sql.indexOf("where", tableNameIndex);
        if(firstWhereIndex == -1) {
            firstWhereIndex = sql.indexOf("WHERE", tableNameIndex);
        }
        if(firstWhereIndex == -1) {
            return sql;
        }
        // 截取 字段索引 到 第一个where索引 的字符串
        String splitTarget = sql.substring(tableNameIndex, firstWhereIndex + 5);

        String[] sqlArr = sql.split(splitTarget);
        sqlBuilder.append(sqlArr[0]);
        sqlBuilder.append(splitTarget);
        sqlBuilder.append(" ").append(tableName).append(".").append(field).append(" in (").append(formatIn(values)).append(") and ");
        sqlBuilder.append(sqlArr[1]);

        return sqlBuilder.toString();
    }

    private <T> String formatIn(List<T> values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            sb.append(values.get(i));
            if(values.size() - 1 != i) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}
