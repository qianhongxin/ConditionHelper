package com.qhx.conditionhelper.core.type;

public class TokenBoxing {

    public static <E> String boxToken(E token) {
        if(token instanceof String) {
            return "'" + token + "'";
        }

        return token.toString();
    }
}
