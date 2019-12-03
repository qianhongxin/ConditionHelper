package indi.xin.conditionhelper.core.parser;

import indi.xin.conditionhelper.core.ConditionContext;

import java.util.List;

public interface SqlParser {
    String parse(String sql, List<ConditionContext.Condition> conditions);
}
