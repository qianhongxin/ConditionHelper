package indi.xin.conditionhelper.demo;

import indi.xin.conditionhelper.core.DataAuthentication;
import indi.xin.conditionhelper.core.SqlSignature;
import indi.xin.conditionhelper.core.UserContext;
import indi.xin.conditionhelper.core.UserContextHolder;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 数据权限切面
 * @author: 32415
 */
@Aspect
@Component
public class DataAuthenticationAspect {

    @Autowired
    private AuthService authService;

    @Before(value = "execution(* my.study.dataauthplugin.demo.*(..)) && @annotation(dataAuthentication)")
    public void getDataAuth(DataAuthentication dataAuthentication) throws Throwable {
        UserContext uc = UserContextHolder.userContextThreadLocal.get();
        if (uc != null && !"".equals(uc.getUserId()) && dataAuthentication != null) {
            List<Integer> ids = authService.getIds(uc.getUserId());
            if (ids != null && !ids.isEmpty()) {
                List<String> fields = new ArrayList<>();
                List<String> tableNames = new ArrayList<>();
                SqlSignature[] signatures = dataAuthentication.value();
                if(signatures.length > 0) {
                    for (int i = 0; i < signatures.length; i++) {
                        fields.add(signatures[i].field());
                        tableNames.add(signatures[i].tableName());
                    }
                }

                uc.init(ids, fields, tableNames);
            }
        }
    }

    @After(value = "execution(* my.study.dataauthplugin.demo.*(..)) && @annotation(dataAuthentication)")
    public void releaseUserContext(DataAuthentication dataAuthentication) throws Throwable {
        if(dataAuthentication != null) {
            UserContextHolder.userContextThreadLocal.remove();
        }
    }

}