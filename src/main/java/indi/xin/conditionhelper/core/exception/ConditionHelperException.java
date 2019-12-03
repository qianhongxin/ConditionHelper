package indi.xin.conditionhelper.core.exception;

public class ConditionHelperException extends RuntimeException {
    public ConditionHelperException() {
    }

    public ConditionHelperException(String message) {
        super(message);
    }

    public ConditionHelperException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConditionHelperException(Throwable cause) {
        super(cause);
    }

    public ConditionHelperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
