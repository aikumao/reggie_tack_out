package com.wei.reggie_tack_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.reggie_tack_out.common.Result;
import com.wei.reggie_tack_out.entity.Employee;
import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {
    /* 登录 */
    Result<Employee> login(HttpServletRequest request, Employee employee);

    /* 退出登录 */
    Result<String> loginOut(HttpServletRequest request);
}
