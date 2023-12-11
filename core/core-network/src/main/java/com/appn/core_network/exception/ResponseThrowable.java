package com.appn.core_network.exception;


/**
 * 自定义业务异常
 */
public class ResponseThrowable extends Exception {
    public int code;

    public String tMessage;

    public ResponseThrowable(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public ResponseThrowable(int code, String message) {
        this.code = code;
        this.tMessage = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return tMessage;
    }

    public void setMessage(String message) {
        this.tMessage = message;
    }
}
