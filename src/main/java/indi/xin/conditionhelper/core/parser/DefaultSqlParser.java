package indi.xin.conditionhelper.core.parser;

import indi.xin.conditionhelper.core.Condition;
import indi.xin.conditionhelper.core.ConditionContext;
import indi.xin.conditionhelper.core.exception.SqlParseException;

import java.util.List;

/**
 * @author hongxin.qian
 * @description: 默认的sql条件拼接器
 * @time 2019/12/3
 **/
public class DefaultSqlParser implements SqlParser {

    @Override
    public String parse(String sql, List<Condition> conditions) {
        try {
            for (int i = 0; i < conditions.size(); i++) {
                sql = parseInteral(sql, conditions.get(i));
            }

            return sql;
        }catch (Exception e) {
            throw new SqlParseException("default sql parser parse exception");
        }
    }

    /**
     * @Description: 将要拼接的条件放到where后的第一个条件
     *
     * @author: hongxin.qian
     * @time: 2019/12/3 10:25
     */
    public String parseInteral(String sql, Condition condition) {
        StringBuilder sqlBuilder = new StringBuilder( sql.length() + 20);

        // 定位目标字符串
        String splitTarget = indexTarget(condition.getTableName(), sql);

        String[] sqlArr = sql.split(splitTarget);
        sqlBuilder.append(sqlArr[0]);
        sqlBuilder.append(splitTarget);
        sqlBuilder.append(" ").append(condition.buildConditionSql()).append(" and ");
        sqlBuilder.append(sqlArr[1]);

        return sqlBuilder.toString();
    }

    /**
     * @Description: 定位目标字符串
     *
     * @author: hongxin.qian
     * @time: 2019/12/3 10:25
     */
    private String indexTarget(String tableName, String sql) {
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
        return sql.substring(tableNameIndex, firstWhereIndex + 5);
    }

}
