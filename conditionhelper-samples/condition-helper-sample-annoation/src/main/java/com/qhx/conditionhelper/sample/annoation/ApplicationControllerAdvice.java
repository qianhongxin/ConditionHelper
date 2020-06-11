package com.qhx.conditionhelper.sample.annoation;

import com.qhx.conditionhelper.core.ConditionContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description:
 * @Author: hongxin.qian
 * @Time: 2018/11/30 17:08
 */
@ControllerAdvice
public class ApplicationControllerAdvice {

    @ModelAttribute
    public void addAttributes(@RequestParam(required = false) String userId) {
        if(userId != null) {
            UserContext userContext = new UserContext();
            userContext.setUserId(userId);
            ConditionContextHolder.conditionContextThreadLocal.set(userContext);
        }
    }

}
