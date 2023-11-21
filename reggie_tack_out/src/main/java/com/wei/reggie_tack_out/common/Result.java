package com.wei.reggie_tack_out.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/*
* 通用返回结果集，服务端响应的结果集最终都会封装成此对象
* */

@Data
public class Result<T> {
    private Integer code;  // 编码， 1 表示成功， 0 或其他数字表示失败
    private T data;  //数据
    private String msg;  //错误信息
    private Map map = new HashMap();  // 动态数据


    /*
    * 当结果成功后，就会调用此方法把数据封装成对象给前端，
    * 我们调用该方法的时候不用 new Result 对象，直接Result.success即可，
    * 因为它是静态的（static）,在这个方法内部已经 new 过了
    * */
    public static <T> Result<T> success(T object) {
        Result<T> r = new Result<T>();
        r.code = 1;
        r.data = object;
        return r;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> r = new Result<>();
        r.code = 0;
        r.msg = msg;
        return r;
    }

    public Result<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
