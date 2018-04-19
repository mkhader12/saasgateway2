package com.virtru.saas.exception;


public class AppException extends Throwable {
    private int errorCode;
    private String errorMsg;

    public AppException(int errCd, String errMsg) {
        this.errorCode = errCd;
        this.errorMsg = errMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
