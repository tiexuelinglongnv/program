package com.zll.program.base;

public class BaseBean<T> {

    /**
     * success : true
     * returnCode : 200
     * result : true
     * timeOut : false
     */

    private int Code;
    private T Data;
    private boolean timeOut;

    private String Message;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }


    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getErrorStack() {
        return errorStack;
    }

    public void setErrorStack(String errorStack) {
        this.errorStack = errorStack;
    }

    private String errorStack;






    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public boolean isTimeOut() {
        return timeOut;
    }

    public void setTimeOut(boolean timeOut) {
        this.timeOut = timeOut;
    }
}
