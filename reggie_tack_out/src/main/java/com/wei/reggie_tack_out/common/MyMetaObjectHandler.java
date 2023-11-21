package com.wei.reggie_tack_out.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/* 按照框架要求编写元数据对象处理器，在此类中统一对公共字段赋值，此类需要实现MetaObjectHandler接口
实现接口之后，重写两个方法，一个是插入时填充，一个是修改时填充
关于字段填充方式，使用metaObject的setValue来实现
关于id的获取，我们之前是存到session里的，但在MyMetaObjectHandler类中不能获得HttpSession对象，所以我们需要用其他方式来获取登录用户Id */

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段填充（create）...");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        //设置创建人id
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段填充（insert）...");
        metaObject.setValue("updateTime", LocalDateTime.now());
        //设置更新人id
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}