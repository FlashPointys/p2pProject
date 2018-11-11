package com.flashpoint.p2p.model.vo;

import java.io.Serializable;

public class ResultObject implements Serializable {
    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
