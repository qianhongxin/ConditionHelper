package indi.xin.conditionhelper.demo;

import indi.xin.conditionhelper.core.ConditionContext;
import indi.xin.conditionhelper.core.ConditionContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description:
 * @Author: 钱红信
 * @Time: 2018/11/30 17:08
 */
@ControllerAdvice
public class ApplicationControllerAdvice {

    @ModelAttribute
    public void addAttributes(@RequestParam(required = false) String userId) {
        if(userId != null) {
            UserContext userContext = new UserContext();
            userContext.setUserId(userId);
            ConditionContextHolder.userContextThreadLocal.set(userContext);
        }
    }

}
