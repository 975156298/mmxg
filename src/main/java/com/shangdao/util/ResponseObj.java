package com.shangdao.util;

public class ResponseObj{
    private Integer statusCode;
    private String message;
    private Object data;

    public ResponseObj() {
        this.statusCode=0;
        this.message="成功";
    }

    public ResponseObj(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public ResponseObj(Object data) {
        this.statusCode=0;
        this.message="成功";
        this.data = data;
    }

    public ResponseObj(Integer statusCode, String message, Object data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
