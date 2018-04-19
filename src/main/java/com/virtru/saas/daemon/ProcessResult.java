package com.virtru.saas.daemon;


public class ProcessResult {
    private String returnObject;
    private Throwable exception;
    private boolean success;

    public String getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(String returnObject) {
        this.returnObject = returnObject;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
