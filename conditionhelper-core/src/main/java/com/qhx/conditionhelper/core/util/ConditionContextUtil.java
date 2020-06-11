package com.qhx.conditionhelper.core.util;

import com.qhx.conditionhelper.core.ConditionContext;
import com.qhx.conditionhelper.core.ConditionContextHolder;

public class ConditionContextUtil {

    public static boolean isEmpty() {
        ConditionContext uc = ConditionContextHolder.conditionContextThreadLocal.get();
        if(uc != null && uc.getConditions() != null && !uc.getConditions().isEmpty()) {
            return true;
        }

        return false;
    }

}
