package my.study.dataauthplugin.core;

public class UserContextHolder {

    public static ThreadLocal<UserContext> userContextThreadLocal = new ThreadLocal<>();

}
