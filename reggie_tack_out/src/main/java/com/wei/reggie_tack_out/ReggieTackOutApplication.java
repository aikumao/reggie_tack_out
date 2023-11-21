package com.wei.reggie_tack_out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@ServletComponentScan  //这样才会扫描过滤器里的 @WebFilter 注解，从而扫描到我们创建的过滤器
@EnableTransactionManagement
@SpringBootApplication
public class ReggieTackOutApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggieTackOutApplication.class, args);
        log.info("项目启动成功");
    }

}
