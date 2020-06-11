package com.qhx.conditionhelper.core.parser.druid;

import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLSelectListCache;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.util.JdbcConstants;
import com.qhx.conditionhelper.core.Condition;
import com.qhx.conditionhelper.core.exception.SqlParseException;
import com.qhx.conditionhelper.core.parser.SqlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DruidSqlParser implements SqlParser {

    private static final Logger logger = LoggerFactory.getLogger(DruidSqlParser.class);

    private ConditionAddVisitor visitor;

    private SQLSelectStatement sqlSelectStatement;

    public void parseSqlBlocks(String sql, List<Condition> conditions) {
        assert conditions != null && !conditions.isEmpty();

        List<String> tableNames = new ArrayList<>();
        conditions.forEach(condition ->  tableNames.add(condition.getTableName()));

        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, JdbcConstants.MYSQL);
        sqlSelectStatement = (SQLSelectStatement) parser.parseStatement();
        visitor = new ConditionAddVisitor(tableNames);
        sqlSelectStatement.accept(visitor);
    }

    public String parseInteral(List<Condition> conditions) {
        ConditionAddVisitor visitor = getVisitor();
        Map<String, List<MySqlSelectQueryBlock>> blockMap = visitor.getBlockMap();

        conditions.forEach(condition -> {
            String conditionSql = condition.buildConditionSql();
            List<MySqlSelectQueryBlock> queryBlocks = blockMap.get(condition.getTableName());
            if(queryBlocks != null && !queryBlocks.isEmpty()) {
                queryBlocks.forEach(queryBlock -> queryBlock.addCondition(conditionSql));
            }
        });

        return sqlSelectStatement.toString();
    }

    @Override
    public String parse(String sql, List<Condition> conditions) {
        try {
            parseSqlBlocks(sql, conditions);
            return parseInteral(conditions);
        }catch (Exception e) {
            throw new SqlParseException("druid sql parser parse exception");
        }

    }

    public ConditionAddVisitor getVisitor() {
        return visitor;
    }
}
