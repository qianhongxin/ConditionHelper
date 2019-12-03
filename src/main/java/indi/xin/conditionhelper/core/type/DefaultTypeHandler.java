package indi.xin.conditionhelper.core.type;

import java.util.List;

/**
 * @author 32415
 * @description: 默认的条件处理handler
 * @time 2019/12/3
 **/
public class DefaultTypeHandler implements SQLTypeHandler {

    @Override
    public <T> String build(List<T> values) {
        return TokenBoxing.boxToken(values.get(0));
    }
}

