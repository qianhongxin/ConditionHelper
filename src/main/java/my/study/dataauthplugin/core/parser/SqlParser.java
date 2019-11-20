package my.study.dataauthplugin.core.parser;

import my.study.dataauthplugin.core.UserContext;

import java.util.List;

public interface SqlParser {
   <T> String parse(String sql, List<T> values, UserContext.Condition condition);
}
