package indi.xin.conditionhelper.core;

public class UserContextHolder {

    public static ThreadLocal<UserContext> userContextThreadLocal = new ThreadLocal<>();

}
