package indi.xin.conditionhelper.core.parser.druid;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;

import java.util.*;

public class ConditionAddVisitor extends MySqlASTVisitorAdapter {

    /**
     * 需要加条件的表名集合
     **/
    private List<String> targetTableNames;

    /**
     * 条件表名在目标sql中第一次出现的 from 信息
     **/
    private Map<String, String> targetTableFirstFromMap = new HashMap<>();

    private Map<String, List<MySqlSelectQueryBlock>> blockMap = new HashMap<>();

    public ConditionAddVisitor(List<String> targetTableNames) {
        this.targetTableNames = targetTableNames;
    }

    @Override
    public boolean visit(MySqlSelectQueryBlock block) {
        String from = block.getFrom().toString();
        for (String tableName : targetTableNames) {
            // 在tableName后加上“.”，定位表名，防止定位到子串
            StringBuilder sb = new StringBuilder(tableName);
            sb.append(".");
            String tableNamePoint = sb.toString();

            // 不包含当前的tableName，就结束当前循环
            if(!from.contains(tableNamePoint)) {
                continue;
            }

            String firstFrom = targetTableFirstFromMap.get(tableNamePoint);
            List<MySqlSelectQueryBlock> sqlSelectQueryBlocks = blockMap.get(tableName);
            if(firstFrom != null && isContains(firstFrom, from)) { //fixme 判断是否包含
                // 删除父级，即最外层的block
                if(sqlSelectQueryBlocks != null && !sqlSelectQueryBlocks.isEmpty()) {
                    sqlSelectQueryBlocks.remove(0);
                    continue;
                }
            }

            if (sqlSelectQueryBlocks == null) {
                sqlSelectQueryBlocks = new LinkedList<>();
            }
            sqlSelectQueryBlocks.add(block);
            blockMap.put(tableName, sqlSelectQueryBlocks);

            targetTableFirstFromMap.put(tableNamePoint, from);
        }

        return true;
    }

//    private boolean isContains(String firstFrom, String from) {
//        from.
//    }
//
//    private String format(String sql) {
//        List<String> sqlList = new ArrayList<>();
//        if(sql.contains("\n\t")){
//            String[] sqlArr = sql.split("\n\t");
//            for (String s : sqlArr) {
//                if(s.contains("\n")) {
//                    String[] sqls = s.split("\n");
//                    for (String sql1 : sqls) {
//                        sqlList.add(sql1);
//                    }
//                }else{
//                    sqlList.add(s);
//                }
//            }
//        }else if(sql.contains("\n")) {
//            String[] sqls = sql.split("\n");
//            for (String sql1 : sqls) {
//                sqlList.add(sql1);
//            }
//        }else {
//            sqlList.add(sql);
//        }
//
//        StringBuilder sb = new StringBuilder();
//        if (!sqlList.isEmpty()) {
//            for (String s : sqlList) {
//                sb.append(s);
//                sb.append(" ");
//            }
//        }
//
//        sql = sb.toString();
//
//        return sql;
//    }

    public Map<String, List<MySqlSelectQueryBlock>> getBlockMap() {
        return blockMap;
    }

}
