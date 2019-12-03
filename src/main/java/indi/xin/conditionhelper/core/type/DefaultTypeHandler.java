package indi.xin.conditionhelper.core.type;

import java.util.List;

/**
 * @author hongxin.qian
 * @description: 默认的条件处理handler
 * @time 2019/12/3
 **/
public class DefaultTypeHandler implements SQLTypeHandler {

    @Override
    public <T> String build(List<T> values) {
        return TokenBoxing.boxToken(values.get(0));
    }
}

