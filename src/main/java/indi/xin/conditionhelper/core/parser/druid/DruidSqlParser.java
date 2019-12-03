package indi.xin.conditionhelper.core.parser.druid;

import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.util.JdbcConstants;
import indi.xin.conditionhelper.core.ConditionContext;
import indi.xin.conditionhelper.core.parser.SqlParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DruidSqlParser implements SqlParser {

    private ConditionAddVisitor visitor;

    private SQLSelectStatement sqlSelectStatement;

    public void parseSqlBlocks(String sql, List<ConditionContext.Condition> conditions) {
        assert conditions != null && !conditions.isEmpty();

        List<String> tableNames = new ArrayList<>();
        conditions.forEach(condition ->  tableNames.add(condition.getTableName()));

        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, JdbcConstants.MYSQL);
        sqlSelectStatement = (SQLSelectStatement) parser.parseStatement();
        visitor = new ConditionAddVisitor(tableNames);
        sqlSelectStatement.accept(visitor);

    }

    public String parseInteral(List<ConditionContext.Condition> conditions) {
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

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT\n" +
                "  rs.*,\n" +
                "  wi.warehouse_name,\n" +
                "  gi.name `goods_name`,\n" +
                "  gi.goods_code,\n" +
                "  dss.name `position_name`\n" +
                "FROM ((SELECT\n" +
                "         esd.id,\n" +
                "         esd.is_deleted,\n" +
                "         esd.order_no,\n" +
                "         esd.in_time `stock_time`,\n" +
                "         esd.position,\n" +
                "         esd.goods_id,\n" +
                "         esd.count,\n" +
                "         esd.order_type,\n" +
                "         esd.price,\n" +
                "         esd.warehouse_id,\n" +
                "         es.related_order `related_order`,\n" +
                "         1 `order_operation`\n" +
                "       FROM enter_stock_detail AS esd LEFT JOIN enter_stock AS es ON esd.order_no = es.order_no\n" +
                "         where esd.goods_id = 1\n" +
                "          )\n" +
                "      UNION ALL (\n" +
                "        SELECT\n" +
                "          lsd.id,\n" +
                "          lsd.is_deleted,\n" +
                "          lsd.order_no,\n" +
                "          lsd.out_time,\n" +
                "          lsd.position,\n" +
                "          lsd. goods_id,\n" +
                "          lsd.count,\n" +
                "          lsd.order_type,\n" +
                "          lsd.price,\n" +
                "          lsd.warehouse_id,\n" +
                "          ls.related_order `related_order`,\n" +
                "          2\n" +
                "        FROM leave_stock_detail AS lsd LEFT JOIN leave_stock AS ls ON lsd.order_no = ls.order_no\n" +
                "          where\n" +
                "          lsd.goods_id = 1\n" +
                "\n" +
                "      )) rs LEFT JOIN warehouse_information wi ON wi.id = rs.warehouse_id\n" +
                "            LEFT JOIN goods_information gi ON gi.id = rs.goods_id\n" +
                "            LEFT JOIN dic_shipping_space dss ON dss.id = rs.position AND dss.is_deleted = 0\n" +
                "            LEFT JOIN dic_goods_category dgc ON dgc.id = gi.goods_category_id AND dgc.is_deleted = 0\n" +
                "where\n" +
                "  gi.goods_code = 1\n";


        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, JdbcConstants.MYSQL);
        SQLSelectStatement stmt = (SQLSelectStatement) parser.parseStatement();

        List<String> names = new ArrayList<>();
        names.add("ls");
        names.add("es");
        ConditionAddVisitor visitor = new ConditionAddVisitor(names);
        stmt.accept(visitor);

        Map<String, List<MySqlSelectQueryBlock>> aliasMap = visitor.getBlockMap();

        List<MySqlSelectQueryBlock> blocks = aliasMap.get("ls");
        if(blocks != null && !blocks.isEmpty()) {
            for (MySqlSelectQueryBlock block : blocks) {
                block.addCondition("ls.id = 0");
            }
        }
        System.out.println(stmt.toString());
    }

    @Override
    public String parse(String sql, List<ConditionContext.Condition> conditions) {
        parseSqlBlocks(sql, conditions);
        return parseInteral(conditions);
    }

    public ConditionAddVisitor getVisitor() {
        return visitor;
    }
}
