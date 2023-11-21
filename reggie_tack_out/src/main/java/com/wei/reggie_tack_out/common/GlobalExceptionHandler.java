package com.wei.reggie_tack_out.common;

/* 全局异常处理器 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})  // 加了 @RestController、@Controller 注解的类都会被 GlobalExceptionHandler 处理
@ResponseBody   // 将我们的结果封装成 Json 对象
@Slf4j   // 日志
public class GlobalExceptionHandler {
    /* 异常处理方法(添加用户名相同时的异常) */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        log.error(exception.getMessage());
        //如果包含Duplicate entry，则说明有条目重复
        if (exception.getMessage().contains("Duplicate entry")) {
            //对字符串切片
            String[] split = exception.getMessage().split(" ");
            //字符串格式是固定的，所以这个位置必然是username
            String username = split[2];
            //拼串作为错误信息返回
            return Result.error("用户名" + username + "已存在");
        }
        return Result.error("未知错误");
    }

    /* 处理 删除分类管理信息 关联了菜品分类或套餐分类 */
    @ExceptionHandler(CustomException.class)
    public Result<String> exceptionHandler(CustomException exception) {
        log.error(exception.getMessage());
        return Result.error(exception.getMessage());
    }
}
