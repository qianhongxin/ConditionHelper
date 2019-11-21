package indi.xin.conditionhelper.core.parser.druid;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.util.JdbcConstants;
import indi.xin.conditionhelper.core.UserContext;
import indi.xin.conditionhelper.core.parser.SqlParser;

import java.util.List;

public class DruidSqlParser implements SqlParser {

    @Override
    public <T> String parse(String sql, List<T> values, UserContext.Condition condition) {
        String dbType = JdbcConstants.MYSQL;
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

        ConditionAddVisitor visitor = new ConditionAddVisitor();
        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
        }

        SQLTableSource tableSource = visitor.getAliasMap().get("a");
        return null;
    }

    public static void main(String[] args) {
        String sql = "select id, name, teach from A a where id = 1 and name=2 and teach = 232";
        String dbType = JdbcConstants.MYSQL;
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

        ConditionAddVisitor visitor = new ConditionAddVisitor();
        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
        }

        SQLTableSource tableSource = visitor.getAliasMap().get("a");
        System.out.println(sql);
    }

}
