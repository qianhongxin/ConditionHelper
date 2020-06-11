package com.qhx.conditionhelper.core.parser;

import com.qhx.conditionhelper.core.Condition;

import java.util.List;

public interface SqlParser {
    String parse(String sql, List<Condition> conditions);
}
