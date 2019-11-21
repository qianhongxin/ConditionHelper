package indi.xin.conditionhelper.core.parser.druid;

import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;

import java.util.HashMap;
import java.util.Map;

public class ConditionAddVisitor extends MySqlASTVisitorAdapter {

    private Map<String, SQLTableSource> aliasMap = new HashMap<>();

    @Override
    public boolean visit(SQLExprTableSource x) {
        String alias = x.getName().getSimpleName();
        aliasMap.put(alias, x);
        return true;
    }

    public Map<String, SQLTableSource> getAliasMap() {
        return aliasMap;
    }

}
