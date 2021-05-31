package com.tutorial.kafka.log;

import lombok.Data;

import java.util.Date;

@Data
public class RuntimeInformation {

    private String tag;
    private String applicationName;
    private String className;
    private String method;
    private String requestUrl;
    private String requestMethod;
    private Date begin;
    private Date end;
    private Long cost;
    private String exceptionMessage;
    private String exceptionStack;

    public RuntimeInformation() {
        this.begin = new Date();
    }

    public void complete() {
        this.end = new Date();
        this.cost = this.end.getTime() - this.begin.getTime();
    }

}