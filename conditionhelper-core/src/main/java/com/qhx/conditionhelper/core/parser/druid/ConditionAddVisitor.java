package com.qhx.conditionhelper.core.parser.druid;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlShowColumnsStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ConditionAddVisitor extends MySqlASTVisitorAdapter {

    /**
     * 需要加条件的表名集合
     **/
    private List<String> targetTableNames;

    /**
     * 记录表名在目标sql中第一次被包含
     **/
    private Map<String, TableAddNumberEnum> targetTableFirstContainsMap = new HashMap<>();

    private Map<String, List<MySqlSelectQueryBlock>> blockMap = new HashMap<>();

    public ConditionAddVisitor(List<String> targetTableNames) {
        this.targetTableNames = targetTableNames;
        for (String targetTableName : targetTableNames) {
            targetTableFirstContainsMap.put(targetTableName, TableAddNumberEnum.ZERO);
        }
    }

    @Override
    public boolean visit(MySqlSelectQueryBlock block) {
        String from = block.getFrom().toString();
        for (String tableName : targetTableNames) {
            // 在tableName后加上“.”，定位表名，防止定位到子串
            // 例如：esd包括了es
            StringBuilder sb = new StringBuilder(tableName);
            sb.append(".");
            String tableNamePoint = sb.toString();

            // 不包含当前的tableName，就结束当前循环
            if(!from.contains(tableNamePoint)) {
                continue;
            }

            TableAddNumberEnum tableAddNumberEnum = targetTableFirstContainsMap.get(tableName);
            List<MySqlSelectQueryBlock> sqlSelectQueryBlocks = blockMap.computeIfAbsent(tableName, k -> new LinkedList<>());

            if(tableAddNumberEnum.equals(TableAddNumberEnum.ZERO)) {
                sqlSelectQueryBlocks.add(block);
                targetTableFirstContainsMap.put(tableName, TableAddNumberEnum.ONE);
                continue;
            }
            if(tableAddNumberEnum.equals(TableAddNumberEnum.ONE)) {
                sqlSelectQueryBlocks.remove(0);
                sqlSelectQueryBlocks.add(block);
                targetTableFirstContainsMap.put(tableName, TableAddNumberEnum.TWO);
                continue;
            }
            if(tableAddNumberEnum.equals(TableAddNumberEnum.TWO)) {
                sqlSelectQueryBlocks.add(block);
            }
        }

        return true;
    }

    public Map<String, List<MySqlSelectQueryBlock>> getBlockMap() {
        return blockMap;
    }

}
