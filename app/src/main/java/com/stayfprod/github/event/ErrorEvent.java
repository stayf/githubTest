package com.stayfprod.github.event;

public class ErrorEvent {
    public int errCode;
    public String errDesc;

    public ErrorEvent(int errCode, String errDesc) {
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

    public boolean isServerError() {
        return errCode >= 500;
    }
}
