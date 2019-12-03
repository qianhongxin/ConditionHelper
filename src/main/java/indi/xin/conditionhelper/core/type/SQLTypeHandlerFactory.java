package indi.xin.conditionhelper.core.type;

import indi.xin.conditionhelper.core.ConditionType;

/**
 * @Description: typeHandler创建工厂
 * @Author: red
 * @Time: 2019/12/02 23:43
 */
public class SQLTypeHandlerFactory {

    public static SQLTypeHandler createTypeHandler(ConditionType conditionType) {
        if(conditionType == null) {
            return new DefaultTypeHandler();
        }

        switch (conditionType){
            case IN:
                return new SQLInTypeHandler();
            case NOT_IN:
                return new SQLInTypeHandler();
            case BETWEEN:
                return new SQLBetweenTypeHandler();
            default:
                return new DefaultTypeHandler();
        }
    }

}
