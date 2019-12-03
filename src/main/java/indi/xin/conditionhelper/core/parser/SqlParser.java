package indi.xin.conditionhelper.core.parser;

import indi.xin.conditionhelper.core.Condition;

import java.util.List;

public interface SqlParser {
    String parse(String sql, List<Condition> conditions);
}
