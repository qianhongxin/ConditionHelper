package indi.xin.conditionhelper.core.parser.druid;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import indi.xin.conditionhelper.core.UserContext;
import indi.xin.conditionhelper.core.parser.SqlParser;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

public class DruidSqlParser implements SqlParser {

    @Override
    public <T> String parse(String sql, List<T> values, UserContext.Condition condition) {
        StringBuilder sb = new StringBuilder();

        String dbType = JdbcConstants.MYSQL;
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        MySqlOutputVisitor outputVisitor = new MySqlOutputVisitor(sb);
        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
            stmt.accept(outputVisitor);
        }

        TableStat.Column column = new TableStat.Column("lsgd", "shopId");
        TableStat.Condition conditionIn = new TableStat.Condition(column, "in");
        conditionIn.addValue(1);
        conditionIn.addValue(2);

        visitor.getConditions().add(conditionIn);

        return outputVisitor.toString();
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
        String dbType = JdbcConstants.MYSQL;
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        MySqlOutputVisitor outputVisitor = new MySqlOutputVisitor(sb);
        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
            stmt.accept(outputVisitor);
        }

        System.out.println("getTables:" + visitor.getTables());
        System.out.println("getParameters:" + visitor.getParameters());
        System.out.println("getOrderByColumns:" + visitor.getOrderByColumns());
        System.out.println("getGroupByColumns:" + visitor.getGroupByColumns());
        System.out.println("---------------------------------------------------------------------------");

        TableStat.Column column = new TableStat.Column("leave_stock_detail", "shopId");
        TableStat.Condition condition = new TableStat.Condition(column, "in");
        condition.addValue(1);
        condition.addValue(2);
        visitor.getConditions().add(condition);


    }

}
