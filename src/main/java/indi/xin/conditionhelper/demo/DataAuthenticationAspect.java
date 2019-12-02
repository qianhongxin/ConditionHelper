package indi.xin.conditionhelper.demo;

import indi.xin.conditionhelper.core.*;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 数据权限切面
 * @author: 32415
 */
@Aspect
@Component
public class DataAuthenticationAspect {

    @Autowired
    private AuthService authService;

    @Before(value = "execution(* indi.xin.conditionhelper.demo.*(..)) && @annotation(sqlCondition)")
    public void getDataAuth(SqlCondition sqlCondition) throws Throwable {
        ConditionContext cc = ConditionContextHolder.userContextThreadLocal.get();
        UserContext uc = (UserContext) cc;

        if (uc != null && !"".equals(uc.getUserId()) && sqlCondition != null) {
            List<Integer> ids = authService.getIds(uc.getUserId());
            if (ids != null && !ids.isEmpty()) {
                List<String> fields = new ArrayList<>();
                List<String> tableNames = new ArrayList<>();
                List<ConditionType> conditionTypes = new ArrayList<>();
                SqlSignature[] signatures = sqlCondition.value();
                Map<Integer, List<Integer>> valueMap = new HashMap<>();
                if(signatures.length > 0) {
                    for (int i = 0; i < signatures.length; i++) {
                        fields.add(signatures[i].field());
                        tableNames.add(signatures[i].tableName());
                        conditionTypes.add(signatures[i].conditionType());
                        valueMap.put(i, ids);
                    }
                }

                uc.init(fields, tableNames, conditionTypes, valueMap);
            }
        }
    }

    @After(value = "execution(* indi.xin.conditionhelper.demo.*(..)) && @annotation(sqlCondition)")
    public void releaseUserContext(SqlCondition sqlCondition) throws Throwable {
        if(sqlCondition != null) {
            ConditionContextHolder.userContextThreadLocal.remove();
        }
    }

}