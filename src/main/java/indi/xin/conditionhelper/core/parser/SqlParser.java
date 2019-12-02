package indi.xin.conditionhelper.core.parser;

import indi.xin.conditionhelper.core.UserContext;

import java.util.List;

public interface SqlParser {
   <T> String parse(String sql, List<T> values, List<UserContext.Condition> conditions);
}
