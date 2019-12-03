package indi.xin.conditionhelper.core.type;

import java.util.List;

/**
 * @Description: 解析条件类型
 * @Author: red
 * @Time: 2019/12/02 23:29
 */
public interface SQLTypeHandler {

    <T> String build(List<T> values);
}
