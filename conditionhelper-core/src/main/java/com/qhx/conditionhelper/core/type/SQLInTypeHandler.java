package com.qhx.conditionhelper.core.type;

import java.util.List;

/**
 * @Description: 处理 in 条件
 * @Author: red
 * @Time: 2019/12/02 23:30
 */
public class SQLInTypeHandler implements SQLTypeHandler {

    @Override
    public <T> String build(List<T> values) {
        StringBuilder sb = new StringBuilder();

        sb.append("(");
        for (int i = 0; i < values.size(); i++) {
            if(i == 0) {
                sb.append(TokenBoxing.boxToken(values.get(i)));
                continue;
            }
            sb.append(",");
            sb.append(TokenBoxing.boxToken(values.get(i)));
        }
        sb.append(")");

        return sb.toString();
    }

}
