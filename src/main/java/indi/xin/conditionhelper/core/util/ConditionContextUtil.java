package indi.xin.conditionhelper.core.util;

import indi.xin.conditionhelper.core.ConditionContext;
import indi.xin.conditionhelper.core.ConditionContextHolder;

public class ConditionContextUtil {

    public static boolean isEmpty() {
        ConditionContext uc = ConditionContextHolder.conditionContextThreadLocal.get();
        if(uc != null && uc.getConditions() != null && !uc.getConditions().isEmpty()) {
            return true;
        }

        return false;
    }

}
