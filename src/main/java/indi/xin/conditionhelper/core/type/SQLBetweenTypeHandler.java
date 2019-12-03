package indi.xin.conditionhelper.core.type;

import java.util.List;

/**
 * @author hongxin.qian
 * @description: 处理 between 条件
 * @time 2019/12/3
 **/
public class SQLBetweenTypeHandler implements SQLTypeHandler {

    @Override
    public <T> String build(List<T> values) {
        int betweenNum = 2;
        if(values.size() != betweenNum) {
            throw new RuntimeException("the values size mast be 2 for between condition of sql");
        }

        return TokenBoxing.boxToken(values.get(0)) + " and " + TokenBoxing.boxToken(values.get(1));
    }
}
