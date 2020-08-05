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
        // 先清理下threadlocal数据，防止上次因为controller级别就报异常了导致userId没清理完毕导致的脏数据。因为tomcat线程池是循环处理请求的，线程是共享的
        ConditionContextHolder.conditionContextThreadLocal.remove();
        if(userId != null) {
            UserContext userContext = new UserContext();
            userContext.setUserId(userId);
            ConditionContextHolder.conditionContextThreadLocal.set(userContext);
        }
    }

}
