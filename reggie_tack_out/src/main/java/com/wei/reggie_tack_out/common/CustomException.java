package com.wei.reggie_tack_out.common;


/*
* 该类用于封装我们的自定义异常
* */

public class CustomException extends RuntimeException {

    public CustomException(String msg) {
        super(msg);
    }
}
