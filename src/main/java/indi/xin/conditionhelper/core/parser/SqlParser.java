package indi.xin.conditionhelper.core.parser;

import indi.xin.conditionhelper.core.ConditionContext;

import java.util.List;

public interface SqlParser {
   <T> String parse(String sql, List<T> values, List<ConditionContext.Condition> conditions);
}
